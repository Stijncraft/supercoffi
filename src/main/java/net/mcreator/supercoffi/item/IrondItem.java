package net.mcreator.supercoffi.item;

import net.mcreator.supercoffi.block.IrondPortalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class IrondItem extends Item {
   public IrondItem(Properties properties) {
      super(properties.durability(64));
   }

   public InteractionResult useOn(UseOnContext context) {
      Player entity = context.getPlayer();
      BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
      ItemStack itemstack = context.getItemInHand();
      Level world = context.getLevel();
      if (!entity.mayUseItemAt(pos, context.getClickedFace(), itemstack)) {
         return InteractionResult.FAIL;
      }

      int x = pos.getX();
      int y = pos.getY();
      int z = pos.getZ();
      boolean success = false;
      if (world.isEmptyBlock(pos)) {
         IrondPortalBlock.portalSpawn(world, pos);
         itemstack.hurtAndBreak(1, entity, context.getHand().asEquipmentSlot());
         success = true;
      }

      return (InteractionResult)(success ? InteractionResult.SUCCESS : InteractionResult.FAIL);
   }
}
