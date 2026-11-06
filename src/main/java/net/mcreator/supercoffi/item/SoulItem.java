package net.mcreator.supercoffi.item;

import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.component.TooltipDisplay;

public class SoulItem extends Item {
   public SoulItem(Properties properties) {
      super(properties.stacksTo(10));
   }

   public void appendHoverText(
      ItemStack itemstack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> componentConsumer, TooltipFlag flag
   ) {
      super.appendHoverText(itemstack, context, tooltipDisplay, componentConsumer, flag);
      componentConsumer.accept(Component.translatable("item.supercoffi.soul.description_0"));
   }
}
