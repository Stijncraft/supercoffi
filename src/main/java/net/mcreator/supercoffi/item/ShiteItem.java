package net.mcreator.supercoffi.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.Item.Properties;

public class ShiteItem extends Item {
   private static final ToolMaterial TOOL_MATERIAL = new ToolMaterial(
      BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 100, 4.0F, 0.0F, 2, TagKey.create(Registries.ITEM, Identifier.parse("supercoffi:shite_repair_items"))
   );

   public ShiteItem(Properties properties) {
      super(properties.sword(TOOL_MATERIAL, 499.0F, -3.0F).fireResistant().setNoCombineRepair());
   }

   public ItemStackTemplate getCraftingRemainder(ItemInstance itemInstance) {
      return new ItemStackTemplate(this);
   }
}
