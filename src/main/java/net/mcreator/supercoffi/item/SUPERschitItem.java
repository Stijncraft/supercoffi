package net.mcreator.supercoffi.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

public class SUPERschitItem extends Item {
   private static final ToolMaterial TOOL_MATERIAL = new ToolMaterial(
      BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 100, 4.0F, 0.0F, 2, TagKey.create(Registries.ITEM, Identifier.parse("supercoffi:supe_rschit_repair_items"))
   );

   public SUPERschitItem(Properties properties) {
      super(
         TOOL_MATERIAL.applyToolProperties(properties, BlockTags.MINEABLE_WITH_PICKAXE, 19.0F, -2.5F, 0.0F)
            .attributes(
               ItemAttributeModifiers.builder()
                  .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 19.0, Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                  .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.5, Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                  .build()
            )
            .rarity(Rarity.UNCOMMON)
      );
   }

   public boolean isCorrectToolForDrops(ItemStack itemstack, BlockState blockstate) {
      return blockstate.is(BlockTags.MINEABLE_WITH_AXE)
         || blockstate.is(BlockTags.MINEABLE_WITH_HOE)
         || blockstate.is(BlockTags.MINEABLE_WITH_PICKAXE)
         || blockstate.is(BlockTags.MINEABLE_WITH_SHOVEL);
   }

   public boolean canPerformAction(ItemInstance stack, ItemAbility toolAction) {
      return ItemAbilities.DEFAULT_AXE_ACTIONS.contains(toolAction)
         || ItemAbilities.DEFAULT_HOE_ACTIONS.contains(toolAction)
         || ItemAbilities.DEFAULT_SHOVEL_ACTIONS.contains(toolAction)
         || toolAction == ItemAbilities.SWORD_SWEEP;
   }

   public float getDestroySpeed(ItemStack itemstack, BlockState blockstate) {
      return 4.0F;
   }

   public boolean mineBlock(ItemStack itemstack, Level world, BlockState blockstate, BlockPos pos, LivingEntity entity) {
      itemstack.hurtAndBreak(1, entity, entity.getUsedItemHand().asEquipmentSlot());
      return true;
   }

   public void hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
      itemstack.hurtAndBreak(2, entity, entity.getUsedItemHand().asEquipmentSlot());
   }

   public boolean isFoil(ItemStack itemstack) {
      return true;
   }
}
