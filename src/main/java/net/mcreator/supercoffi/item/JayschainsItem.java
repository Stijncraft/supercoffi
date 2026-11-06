package net.mcreator.supercoffi.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.Item.Properties;

public class JayschainsItem extends Item {
   private static final ToolMaterial TOOL_MATERIAL = new ToolMaterial(
      BlockTags.INCORRECT_FOR_WOODEN_TOOL, 100, 4.0F, 0.0F, 2, TagKey.create(Registries.ITEM, Identifier.parse("supercoffi:jayschains_repair_items"))
   );

   public JayschainsItem(Properties properties) {
      super(properties.pickaxe(TOOL_MATERIAL, 3.0F, -3.0F));
   }

   public boolean isFoil(ItemStack itemstack) {
      return true;
   }
}
