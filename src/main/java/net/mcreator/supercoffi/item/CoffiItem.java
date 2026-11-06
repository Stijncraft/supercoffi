package net.mcreator.supercoffi.item;

import net.minecraft.world.food.FoodProperties.Builder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;

public class CoffiItem extends Item {
   public CoffiItem(Properties properties) {
      super(properties.food(new Builder().nutrition(4).saturationModifier(0.3F).alwaysEdible().build()));
   }
}
