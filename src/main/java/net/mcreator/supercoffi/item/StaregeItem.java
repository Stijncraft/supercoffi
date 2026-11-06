package net.mcreator.supercoffi.item;

import java.util.function.Consumer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.component.TooltipDisplay;

public class StaregeItem extends Item {
   private static final ToolMaterial TOOL_MATERIAL = new ToolMaterial(
      BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 100, 4.0F, 0.0F, 2, TagKey.create(Registries.ITEM, Identifier.parse("supercoffi:starege_repair_items"))
   );

   public StaregeItem(Properties properties) {
      super(properties.sword(TOOL_MATERIAL, 19.0F, -2.5F).rarity(Rarity.EPIC).setNoCombineRepair());
   }

   public ItemStackTemplate getCraftingRemainder(ItemInstance itemInstance) {
      return new ItemStackTemplate(this);
   }

   public void appendHoverText(
      ItemStack itemstack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> componentConsumer, TooltipFlag flag
   ) {
      super.appendHoverText(itemstack, context, tooltipDisplay, componentConsumer, flag);
      componentConsumer.accept(Component.translatable("item.supercoffi.starege.description_0"));
   }

   public boolean isFoil(ItemStack itemstack) {
      return true;
   }
}
