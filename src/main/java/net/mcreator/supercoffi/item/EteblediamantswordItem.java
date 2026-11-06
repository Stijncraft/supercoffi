package net.mcreator.supercoffi.item;

import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties.Builder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.component.TooltipDisplay;

public class EteblediamantswordItem extends Item {
   public EteblediamantswordItem(Properties properties) {
      super(properties.food(new Builder().nutrition(4).saturationModifier(40.0F).alwaysEdible().build()));
   }

   public void appendHoverText(
      ItemStack itemstack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> componentConsumer, TooltipFlag flag
   ) {
      super.appendHoverText(itemstack, context, tooltipDisplay, componentConsumer, flag);
      componentConsumer.accept(Component.translatable("item.supercoffi.eteblediamantsword.description_0"));
   }
}
