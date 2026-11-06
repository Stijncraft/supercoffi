package net.mcreator.supercoffi.block;

import com.mojang.logging.LogUtils;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;
import net.mcreator.supercoffi.world.teleporter.IrondPortalShape;
import net.mcreator.supercoffi.world.teleporter.IrondTeleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.BlockUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.util.BlockUtil.FoundRectangle;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Portal.Transition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.level.portal.TeleportTransition.PostTeleportTransition;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public class IrondPortalBlock extends NetherPortalBlock {
   private static final Logger LOGGER = LogUtils.getLogger();

   public static void portalSpawn(Level world, BlockPos pos) {
      Optional<IrondPortalShape> optional = IrondPortalShape.findEmptyPortalShape(world, pos, Axis.X);
      if (optional.isPresent()) {
         optional.get().createPortalBlocks(world);
      }
   }

   public IrondPortalBlock(Properties properties) {
      super(properties.noCollision().randomTicks().pushReaction(PushReaction.BLOCK).strength(-1.0F).sound(SoundType.GLASS).lightLevel(state -> 0).noLootTable());
   }

   private IrondTeleporter getTeleporter(ServerLevel level) {
      return new IrondTeleporter(level);
   }

   protected BlockState updateShape(
      BlockState state,
      LevelReader level,
      ScheduledTickAccess ticks,
      BlockPos pos,
      Direction directionToNeighbour,
      BlockPos neighbourPos,
      BlockState neighbourState,
      RandomSource random
   ) {
      Axis updateAxis = directionToNeighbour.getAxis();
      Axis axis = (Axis)state.getValue(AXIS);
      boolean wrongAxis = axis != updateAxis && updateAxis.isHorizontal();
      return !wrongAxis && !neighbourState.is(this) && !IrondPortalShape.findAnyShape(level, pos, axis).isComplete()
         ? Blocks.AIR.defaultBlockState()
         : super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
   }

   @Nullable
   public TeleportTransition getPortalDestination(ServerLevel currentLevel, Entity entity, BlockPos portalEntryPos) {
      ResourceKey<Level> newDimension = currentLevel.dimension() == ResourceKey.create(Registries.DIMENSION, Identifier.parse("supercoffi:irond"))
         ? Level.OVERWORLD
         : ResourceKey.create(Registries.DIMENSION, Identifier.parse("supercoffi:irond"));
      ServerLevel newLevel = currentLevel.getServer().getLevel(newDimension);
      if (newLevel == null) {
         return null;
      }

      boolean toNether = newLevel.dimension() == ResourceKey.create(Registries.DIMENSION, Identifier.parse("supercoffi:irond"));
      WorldBorder newWorldBorder = newLevel.getWorldBorder();
      double teleportationScale = DimensionType.getTeleportationScale(currentLevel.dimensionType(), newLevel.dimensionType());
      BlockPos approximateExitPos = newWorldBorder.clampToBounds(entity.getX() * teleportationScale, entity.getY(), entity.getZ() * teleportationScale);
      return this.getExitPortal(newLevel, entity, portalEntryPos, approximateExitPos, toNether, newWorldBorder);
   }

   @Nullable
   private TeleportTransition getExitPortal(
      ServerLevel newLevel, Entity entity, BlockPos portalEntryPos, BlockPos approximateExitPos, boolean toNether, WorldBorder worldBorder
   ) {
      Optional<BlockPos> exitPortalPos = this.getTeleporter(newLevel).findClosestPortalPosition(approximateExitPos, toNether, worldBorder);
      FoundRectangle exitPortal;
      PostTeleportTransition post;
      if (exitPortalPos.isPresent()) {
         BlockPos pos = exitPortalPos.get();
         BlockState portalState = newLevel.getBlockState(pos);
         exitPortal = BlockUtil.getLargestRectangleAround(
            pos, (Axis)portalState.getValue(BlockStateProperties.HORIZONTAL_AXIS), 21, Axis.Y, 21, blockPos -> newLevel.getBlockState(blockPos) == portalState
         );
         post = TeleportTransition.PLAY_PORTAL_SOUND.then(e -> e.placePortalTicket(pos));
      } else {
         Axis sourcePortalAxis = entity.level().getBlockState(portalEntryPos).getOptionalValue(AXIS).orElse(Axis.X);
         Optional<FoundRectangle> createdExit = this.getTeleporter(newLevel).createPortal(approximateExitPos, sourcePortalAxis);
         if (createdExit.isEmpty()) {
            LOGGER.error("Unable to create a portal, likely target out of worldborder");
            return null;
         }

         exitPortal = createdExit.get();
         post = TeleportTransition.PLAY_PORTAL_SOUND.then(TeleportTransition.PLACE_PORTAL_TICKET);
      }

      return getDimensionTransitionFromExit(entity, portalEntryPos, exitPortal, newLevel, post);
   }

   private static TeleportTransition getDimensionTransitionFromExit(
      Entity entity, BlockPos portalEntryPos, FoundRectangle exitPortal, ServerLevel newLevel, PostTeleportTransition postTeleportTransition
   ) {
      BlockState blockState = entity.level().getBlockState(portalEntryPos);
      Axis axis;
      Vec3 offset;
      if (blockState.hasProperty(BlockStateProperties.HORIZONTAL_AXIS)) {
         axis = (Axis)blockState.getValue(BlockStateProperties.HORIZONTAL_AXIS);
         FoundRectangle portalArea = BlockUtil.getLargestRectangleAround(
            portalEntryPos, axis, 21, Axis.Y, 21, pos -> entity.level().getBlockState(pos) == blockState
         );
         offset = entity.getRelativePortalPosition(axis, portalArea);
      } else {
         axis = Axis.X;
         offset = new Vec3(0.5, 0.0, 0.0);
      }

      return createDimensionTransition(newLevel, exitPortal, axis, offset, entity, postTeleportTransition);
   }

   private static TeleportTransition createDimensionTransition(
      ServerLevel newLevel, FoundRectangle foundRectangle, Axis portalAxis, Vec3 offset, Entity entity, PostTeleportTransition postTeleportTransition
   ) {
      BlockPos bottomLeft = foundRectangle.minCorner;
      BlockState blockState = newLevel.getBlockState(bottomLeft);
      Axis axis = blockState.getOptionalValue(BlockStateProperties.HORIZONTAL_AXIS).orElse(Axis.X);
      double width = foundRectangle.axis1Size;
      double height = foundRectangle.axis2Size;
      EntityDimensions dimensions = entity.getDimensions(entity.getPose());
      int outputRotation = portalAxis == axis ? 0 : 90;
      double offsetRight = dimensions.width() / 2.0 + (width - dimensions.width()) * offset.x();
      double offsetUp = (height - dimensions.height()) * offset.y();
      double offsetForward = 0.5 + offset.z();
      boolean xAligned = axis == Axis.X;
      Vec3 targetPos = new Vec3(
         bottomLeft.getX() + (xAligned ? offsetRight : offsetForward),
         bottomLeft.getY() + offsetUp,
         bottomLeft.getZ() + (xAligned ? offsetForward : offsetRight)
      );
      Vec3 collisionFreePos = IrondPortalShape.findCollisionFreePosition(targetPos, newLevel, entity, dimensions);
      return new TeleportTransition(
         newLevel, collisionFreePos, Vec3.ZERO, outputRotation, 0.0F, Relative.union(new Set[]{Relative.DELTA, Relative.ROTATION}), postTeleportTransition
      );
   }

   public int getPortalTransitionTime(ServerLevel world, Entity entity) {
      return 0;
   }

   public Transition getLocalTransition() {
      return Transition.NONE;
   }

   public void randomTick(BlockState blockstate, ServerLevel world, BlockPos pos, RandomSource random) {
   }

   public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
      for (int i = 0; i < 4; i++) {
         double px = pos.getX() + random.nextFloat();
         double py = pos.getY() + random.nextFloat();
         double pz = pos.getZ() + random.nextFloat();
         double vx = (random.nextFloat() - 0.5) / 2.0;
         double vy = (random.nextFloat() - 0.5) / 2.0;
         double vz = (random.nextFloat() - 0.5) / 2.0;
         int j = random.nextInt(4) - 1;
         if (world.getBlockState(pos.west()).getBlock() != this && world.getBlockState(pos.east()).getBlock() != this) {
            px = pos.getX() + 0.5 + 0.25 * j;
            vx = random.nextFloat() * 2.0F * j;
         } else {
            pz = pos.getZ() + 0.5 + 0.25 * j;
            vz = random.nextFloat() * 2.0F * j;
         }

         world.addParticle(ParticleTypes.BUBBLE, px, py, pz, vx, vy, vz);
      }

      if (random.nextInt(110) == 0) {
         world.playLocalSound(
            pos.getX() + 0.5,
            pos.getY() + 0.5,
            pos.getZ() + 0.5,
            (SoundEvent)BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.portal.ambient")),
            SoundSource.BLOCKS,
            0.5F,
            random.nextFloat() * 0.4F + 0.8F,
            false
         );
      }
   }
}
