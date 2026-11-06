package net.mcreator.supercoffi.world.teleporter;

import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.mcreator.supercoffi.init.SupercoffiModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.BlockUtil.FoundRectangle;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.StatePredicate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.mutable.MutableInt;

public class IrondPortalShape {
   private static final int MIN_WIDTH = 2;
   public static final int MAX_WIDTH = 21;
   private static final int MIN_HEIGHT = 3;
   public static final int MAX_HEIGHT = 21;
   private static final StatePredicate FRAME = (state, level, pos) -> state.is(Blocks.OAK_LOG);
   private static final float SAFE_TRAVEL_MAX_ENTITY_XY = 4.0F;
   private static final double SAFE_TRAVEL_MAX_VERTICAL_DELTA = 1.0;
   private final Axis axis;
   private final Direction rightDir;
   private final int numPortalBlocks;
   private final BlockPos bottomLeft;
   private final int height;
   private final int width;

   public IrondPortalShape(Axis axis, int portalBlockCount, Direction rightDir, BlockPos bottomLeft, int width, int height) {
      this.axis = axis;
      this.numPortalBlocks = portalBlockCount;
      this.rightDir = rightDir;
      this.bottomLeft = bottomLeft;
      this.width = width;
      this.height = height;
   }

   public static Optional<IrondPortalShape> findEmptyPortalShape(LevelAccessor level, BlockPos pos, Axis preferredAxis) {
      return findPortalShape(level, pos, shape -> shape.isValid() && shape.numPortalBlocks == 0, preferredAxis);
   }

   public static Optional<IrondPortalShape> findPortalShape(LevelAccessor level, BlockPos pos, Predicate<IrondPortalShape> isValid, Axis preferredAxis) {
      Optional<IrondPortalShape> firstAxis = Optional.of(findAnyShape(level, pos, preferredAxis)).filter(isValid);
      if (firstAxis.isPresent()) {
         return firstAxis;
      }

      Axis otherAxis = preferredAxis == Axis.X ? Axis.Z : Axis.X;
      return Optional.of(findAnyShape(level, pos, otherAxis)).filter(isValid);
   }

   public static IrondPortalShape findAnyShape(BlockGetter level, BlockPos pos, Axis axis) {
      Direction rightDir = axis == Axis.X ? Direction.WEST : Direction.SOUTH;
      BlockPos bottomLeft = calculateBottomLeft(level, rightDir, pos);
      if (bottomLeft == null) {
         return new IrondPortalShape(axis, 0, rightDir, pos, 0, 0);
      }

      int width = calculateWidth(level, bottomLeft, rightDir);
      if (width == 0) {
         return new IrondPortalShape(axis, 0, rightDir, bottomLeft, 0, 0);
      }

      MutableInt portalBlockCountOutput = new MutableInt();
      int height = calculateHeight(level, bottomLeft, rightDir, width, portalBlockCountOutput);
      return new IrondPortalShape(axis, portalBlockCountOutput.intValue(), rightDir, bottomLeft, width, height);
   }

   @Nullable
   private static BlockPos calculateBottomLeft(BlockGetter level, Direction rightDir, BlockPos pos) {
      int minY = Math.max(level.getMinY(), pos.getY() - 21);

      while (pos.getY() > minY && isEmpty(level.getBlockState(pos.below()))) {
         pos = pos.below();
      }

      Direction leftDir = rightDir.getOpposite();
      int edge = getDistanceUntilEdgeAboveFrame(level, pos, leftDir) - 1;
      return edge < 0 ? null : pos.relative(leftDir, edge);
   }

   private static int calculateWidth(BlockGetter level, BlockPos bottomLeft, Direction rightDir) {
      int width = getDistanceUntilEdgeAboveFrame(level, bottomLeft, rightDir);
      return width >= 2 && width <= 21 ? width : 0;
   }

   private static int getDistanceUntilEdgeAboveFrame(BlockGetter level, BlockPos pos, Direction direction) {
      MutableBlockPos blockPos = new MutableBlockPos();

      for (int width = 0; width <= 21; width++) {
         blockPos.set(pos).move(direction, width);
         BlockState blockState = level.getBlockState(blockPos);
         if (!isEmpty(blockState)) {
            if (FRAME.test(blockState, level, blockPos)) {
               return width;
            }
            break;
         }

         BlockState belowState = level.getBlockState(blockPos.move(Direction.DOWN));
         if (!FRAME.test(belowState, level, blockPos)) {
            break;
         }
      }

      return 0;
   }

   private static int calculateHeight(BlockGetter level, BlockPos bottomLeft, Direction rightDir, int width, MutableInt portalBlockCount) {
      MutableBlockPos pos = new MutableBlockPos();
      int height = getDistanceUntilTop(level, bottomLeft, rightDir, pos, width, portalBlockCount);
      return height >= 3 && height <= 21 && hasTopFrame(level, bottomLeft, rightDir, pos, width, height) ? height : 0;
   }

   private static boolean hasTopFrame(BlockGetter level, BlockPos bottomLeft, Direction rightDir, MutableBlockPos pos, int width, int height) {
      for (int i = 0; i < width; i++) {
         MutableBlockPos framePos = pos.set(bottomLeft).move(Direction.UP, height).move(rightDir, i);
         if (!FRAME.test(level.getBlockState(framePos), level, framePos)) {
            return false;
         }
      }

      return true;
   }

   private static int getDistanceUntilTop(
      BlockGetter level, BlockPos bottomLeft, Direction rightDir, MutableBlockPos pos, int width, MutableInt portalBlockCount
   ) {
      for (int height = 0; height < 21; height++) {
         pos.set(bottomLeft).move(Direction.UP, height).move(rightDir, -1);
         if (!FRAME.test(level.getBlockState(pos), level, pos)) {
            return height;
         }

         pos.set(bottomLeft).move(Direction.UP, height).move(rightDir, width);
         if (!FRAME.test(level.getBlockState(pos), level, pos)) {
            return height;
         }

         for (int i = 0; i < width; i++) {
            pos.set(bottomLeft).move(Direction.UP, height).move(rightDir, i);
            BlockState state = level.getBlockState(pos);
            if (!isEmpty(state)) {
               return height;
            }

            if (state.is((Block)SupercoffiModBlocks.IROND_PORTAL.get())) {
               portalBlockCount.increment();
            }
         }
      }

      return 21;
   }

   private static boolean isEmpty(BlockState state) {
      return state.isAir() || state.is((Block)SupercoffiModBlocks.IROND_PORTAL.get());
   }

   public boolean isValid() {
      return this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
   }

   public void createPortalBlocks(LevelAccessor level) {
      BlockState portalState = (BlockState)((Block)SupercoffiModBlocks.IROND_PORTAL.get()).defaultBlockState().setValue(NetherPortalBlock.AXIS, this.axis);
      BlockPos.betweenClosed(this.bottomLeft, this.bottomLeft.relative(Direction.UP, this.height - 1).relative(this.rightDir, this.width - 1))
         .forEach(pos -> level.setBlock(pos, portalState, 18));
   }

   public boolean isComplete() {
      return this.isValid() && this.numPortalBlocks == this.width * this.height;
   }

   public static Vec3 getRelativePosition(FoundRectangle largestRectangleAround, Axis axis, Vec3 position, EntityDimensions dimensions) {
      double width = (double)largestRectangleAround.axis1Size - dimensions.width();
      double height = (double)largestRectangleAround.axis2Size - dimensions.height();
      BlockPos bottomMin = largestRectangleAround.minCorner;
      double relativeRight;
      if (width > 0.0) {
         double bottomStart = bottomMin.get(axis) + dimensions.width() / 2.0;
         relativeRight = Mth.clamp(Mth.inverseLerp(position.get(axis) - bottomStart, 0.0, width), 0.0, 1.0);
      } else {
         relativeRight = 0.5;
      }

      double relativeUp;
      if (height > 0.0) {
         Axis heightAxis = Axis.Y;
         relativeUp = Mth.clamp(Mth.inverseLerp(position.get(heightAxis) - bottomMin.get(heightAxis), 0.0, height), 0.0, 1.0);
      } else {
         relativeUp = 0.0;
      }

      Axis forwardAxis = axis == Axis.X ? Axis.Z : Axis.X;
      double relativeForward = position.get(forwardAxis) - (bottomMin.get(forwardAxis) + 0.5);
      return new Vec3(relativeRight, relativeUp, relativeForward);
   }

   public static Vec3 findCollisionFreePosition(Vec3 bottomCenter, ServerLevel serverLevel, Entity entity, EntityDimensions dimensions) {
      if (!(dimensions.width() > 4.0F) && !(dimensions.height() > 4.0F)) {
         double halfHeight = dimensions.height() / 2.0;
         Vec3 center = bottomCenter.add(0.0, halfHeight, 0.0);
         VoxelShape allowedCenters = Shapes.create(
            AABB.ofSize(center, dimensions.width(), 0.0, dimensions.width()).expandTowards(0.0, 1.0, 0.0).inflate(1.0E-6)
         );
         Optional<Vec3> collisionFreePosition = serverLevel.findFreePosition(
            entity, allowedCenters, center, dimensions.width(), dimensions.height(), dimensions.width()
         );
         Optional<Vec3> collisionFreeBottomCenter = collisionFreePosition.map(vec -> vec.subtract(0.0, halfHeight, 0.0));
         return collisionFreeBottomCenter.orElse(bottomCenter);
      } else {
         return bottomCenter;
      }
   }
}
