package net.mcreator.supercoffi.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.Item.Properties;

public class PrismItem extends AxeItem {
   private static final ToolMaterial TOOL_MATERIAL = new ToolMaterial(
      BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 100, 50.0F, 0.0F, 2, TagKey.create(Registries.ITEM, Identifier.parse("supercoffi:prism_repair_items"))
   );

   public PrismItem(Properties properties) {
      super(TOOL_MATERIAL, 49.0F, 46.0F, properties.fireResistant());
   }
}
