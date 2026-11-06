package net.mcreator.supercoffi.block;

import net.mcreator.supercoffi.init.SupercoffiModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StarbuksBlock extends Block {
   public StarbuksBlock(Properties properties) {
      super(properties.sound(SoundType.NETHER_BRICKS).strength(2.0F, 10.0F).noOcclusion().isRedstoneConductor((bs, br, bp) -> false).replaceable());
   }

   public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      return Shapes.empty();
   }

   public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData, Player entity) {
      return new ItemStack((ItemLike)SupercoffiModBlocks.STARBUKS.get());
   }
}
