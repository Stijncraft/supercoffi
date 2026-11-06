package net.mcreator.supercoffi.world.teleporter;

import com.google.common.collect.ImmutableSet;
import java.util.Comparator;
import java.util.Optional;
import net.mcreator.supercoffi.init.SupercoffiModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.BlockUtil.FoundRectangle;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiManager.Occupancy;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber
public class DiamontTeleporter {
   public static Holder<PoiType> poi = null;
   private final ServerLevel level;

   @SubscribeEvent
   public static void registerPointOfInterest(RegisterEvent event) {
      event.register(Registries.POINT_OF_INTEREST_TYPE, registerHelper -> {
         PoiType poiType = new PoiType(ImmutableSet.copyOf(((Block)SupercoffiModBlocks.DIAMONT_PORTAL.get()).getStateDefinition().getPossibleStates()), 0, 1);
         registerHelper.register(Identifier.parse("supercoffi:diamont_portal"), poiType);
         poi = BuiltInRegistries.POINT_OF_INTEREST_TYPE.wrapAsHolder(poiType);
      });
   }

   public DiamontTeleporter(ServerLevel level) {
      this.level = level;
   }

   public Optional<BlockPos> findClosestPortalPosition(BlockPos approximateExitPos, boolean toNether, WorldBorder worldBorder) {
      PoiManager poiManager = this.level.getPoiManager();
      int radius = toNether ? 16 : 128;
      poiManager.ensureLoadedAndValid(this.level, approximateExitPos, radius);
      return poiManager.getInSquare(type -> type.is((ResourceKey)poi.unwrapKey().get()), approximateExitPos, radius, Occupancy.ANY)
         .<BlockPos>map(PoiRecord::getPos)
         .filter(worldBorder::isWithinBounds)
         .filter(pos -> this.level.getBlockState(pos).hasProperty(BlockStateProperties.HORIZONTAL_AXIS))
         .min(Comparator.<BlockPos>comparingDouble(p -> p.distSqr(approximateExitPos)).thenComparingInt(Vec3i::getY));
   }

   public Optional<FoundRectangle> createPortal(BlockPos origin, Axis portalAxis) {
      Direction direction = Direction.get(AxisDirection.POSITIVE, portalAxis);
      double closestFullDistanceSqr = -1.0;
      BlockPos closestFullPosition = null;
      double closestPartialDistanceSqr = -1.0;
      BlockPos closestPartialPosition = null;
      WorldBorder worldBorder = this.level.getWorldBorder();
      int maxPlaceableY = Math.min(this.level.getMaxY(), this.level.getMinY() + this.level.getLogicalHeight() - 1);
      int edgeDistance = 1;
      MutableBlockPos mutable = origin.mutable();

      for (MutableBlockPos columnPos : BlockPos.spiralAround(origin, 16, Direction.EAST, Direction.SOUTH)) {
         int height = Math.min(maxPlaceableY, this.level.getHeight(Types.MOTION_BLOCKING, columnPos.getX(), columnPos.getZ()));
         if (worldBorder.isWithinBounds(columnPos) && worldBorder.isWithinBounds(columnPos.move(direction, 1))) {
            columnPos.move(direction.getOpposite(), 1);

            for (int y = height; y >= this.level.getMinY(); y--) {
               columnPos.setY(y);
               if (this.canPortalReplaceBlock(columnPos)) {
                  int firstEmptyY = y;

                  while (y > this.level.getMinY() && this.canPortalReplaceBlock(columnPos.move(Direction.DOWN))) {
                     y--;
                  }

                  if (y + 4 <= maxPlaceableY) {
                     int deltaY = firstEmptyY - y;
                     if (deltaY <= 0 || deltaY >= 3) {
                        columnPos.setY(y);
                        if (this.canHostFrame(columnPos, mutable, direction, 0)) {
                           double distance = origin.distSqr(columnPos);
                           if (this.canHostFrame(columnPos, mutable, direction, -1)
                              && this.canHostFrame(columnPos, mutable, direction, 1)
                              && (closestFullDistanceSqr == -1.0 || closestFullDistanceSqr > distance)) {
                              closestFullDistanceSqr = distance;
                              closestFullPosition = columnPos.immutable();
                           }

                           if (closestFullDistanceSqr == -1.0 && (closestPartialDistanceSqr == -1.0 || closestPartialDistanceSqr > distance)) {
                              closestPartialDistanceSqr = distance;
                              closestPartialPosition = columnPos.immutable();
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      if (closestFullDistanceSqr == -1.0 && closestPartialDistanceSqr != -1.0) {
         closestFullPosition = closestPartialPosition;
         closestFullDistanceSqr = closestPartialDistanceSqr;
      }

      if (closestFullDistanceSqr == -1.0) {
         int minStartY = Math.max(this.level.getMinY() - -1, 70);
         int maxStartY = maxPlaceableY - 9;
         if (maxStartY < minStartY) {
            return Optional.empty();
         }

         closestFullPosition = new BlockPos(
               origin.getX() - direction.getStepX() * 1, Mth.clamp(origin.getY(), minStartY, maxStartY), origin.getZ() - direction.getStepZ() * 1
            )
            .immutable();
         closestFullPosition = worldBorder.clampToBounds(closestFullPosition);
         Direction clockWise = direction.getClockWise();

         for (int box = -1; box < 2; box++) {
            for (int width = 0; width < 2; width++) {
               for (int height = -1; height < 3; height++) {
                  BlockState blockState = height < 0 ? Blocks.OAK_PLANKS.defaultBlockState() : Blocks.AIR.defaultBlockState();
                  mutable.setWithOffset(
                     closestFullPosition,
                     width * direction.getStepX() + box * clockWise.getStepX(),
                     height,
                     width * direction.getStepZ() + box * clockWise.getStepZ()
                  );
                  this.level.setBlockAndUpdate(mutable, blockState);
               }
            }
         }
      }

      for (int width = -1; width < 3; width++) {
         for (int height = -1; height < 4; height++) {
            if (width == -1 || width == 2 || height == -1 || height == 3) {
               mutable.setWithOffset(closestFullPosition, width * direction.getStepX(), height, width * direction.getStepZ());
               this.level.setBlock(mutable, Blocks.OAK_PLANKS.defaultBlockState(), 3);
            }
         }
      }

      BlockState portalBlockState = (BlockState)((Block)SupercoffiModBlocks.DIAMONT_PORTAL.get())
         .defaultBlockState()
         .setValue(NetherPortalBlock.AXIS, portalAxis);

      for (int width = 0; width < 2; width++) {
         for (int heightx = 0; heightx < 3; heightx++) {
            mutable.setWithOffset(closestFullPosition, width * direction.getStepX(), heightx, width * direction.getStepZ());
            this.level.setBlock(mutable, portalBlockState, 18);
            this.level.getPoiManager().add(mutable, poi);
         }
      }

      return Optional.of(new FoundRectangle(closestFullPosition.immutable(), 2, 3));
   }

   private boolean canHostFrame(BlockPos origin, MutableBlockPos mutable, Direction direction, int offset) {
      Direction clockWise = direction.getClockWise();

      for (int width = -1; width < 3; width++) {
         for (int height = -1; height < 4; height++) {
            mutable.setWithOffset(
               origin, direction.getStepX() * width + clockWise.getStepX() * offset, height, direction.getStepZ() * width + clockWise.getStepZ() * offset
            );
            if (height < 0 && !this.level.getBlockState(mutable).isSolid()) {
               return false;
            }

            if (height >= 0 && !this.canPortalReplaceBlock(mutable)) {
               return false;
            }
         }
      }

      return true;
   }

   private boolean canPortalReplaceBlock(MutableBlockPos pos) {
      BlockState blockstate = this.level.getBlockState(pos);
      return blockstate.canBeReplaced() && blockstate.getFluidState().isEmpty();
   }
}
