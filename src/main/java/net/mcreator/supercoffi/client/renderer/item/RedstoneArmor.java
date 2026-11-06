package net.mcreator.supercoffi.client.renderer.item;

import net.mcreator.supercoffi.init.SupercoffiModItems;
import net.minecraft.client.resources.model.EquipmentClientInfo.Layer;
import net.minecraft.client.resources.model.EquipmentClientInfo.LayerType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(Dist.CLIENT)
public class RedstoneArmor {
   @SubscribeEvent
   public static void registerItemExtensions(RegisterClientExtensionsEvent event) {
      event.registerItem(new IClientItemExtensions() {
         private final Identifier armorTexture = Identifier.parse("supercoffi:textures/models/armor/redstone_armor_layer_1.png");

         public Identifier getArmorTexture(ItemStack stack, LayerType type, Layer layer, Identifier original) {
            return this.armorTexture;
         }
      }, new Item[]{(Item)SupercoffiModItems.REDSTONE_HELMET.get()});
      event.registerItem(new IClientItemExtensions() {
         private final Identifier armorTexture = Identifier.parse("supercoffi:textures/models/armor/redstone_armor_layer_1.png");

         public Identifier getArmorTexture(ItemStack stack, LayerType type, Layer layer, Identifier original) {
            return this.armorTexture;
         }
      }, new Item[]{(Item)SupercoffiModItems.REDSTONE_CHESTPLATE.get()});
      event.registerItem(new IClientItemExtensions() {
         private final Identifier armorTexture = Identifier.parse("supercoffi:textures/models/armor/redstone_armor_layer_2.png");

         public Identifier getArmorTexture(ItemStack stack, LayerType type, Layer layer, Identifier original) {
            return this.armorTexture;
         }
      }, new Item[]{(Item)SupercoffiModItems.REDSTONE_LEGGINGS.get()});
      event.registerItem(new IClientItemExtensions() {
         private final Identifier armorTexture = Identifier.parse("supercoffi:textures/models/armor/redstone_armor_layer_1.png");

         public Identifier getArmorTexture(ItemStack stack, LayerType type, Layer layer, Identifier original) {
            return this.armorTexture;
         }
      }, new Item[]{(Item)SupercoffiModItems.REDSTONE_BOOTS.get()});
   }
}
