package net.mcreator.supercoffi.item;

import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;

public abstract class RedstoneItem extends Item {
   public static ArmorMaterial ARMOR_MATERIAL = new ArmorMaterial(
      15,
      Map.of(ArmorType.BOOTS, 2, ArmorType.LEGGINGS, 5, ArmorType.CHESTPLATE, 6, ArmorType.HELMET, 2, ArmorType.BODY, 6),
      9,
      BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.EMPTY),
      0.0F,
      0.0F,
      TagKey.create(Registries.ITEM, Identifier.parse("supercoffi:redstone_repair_items")),
      ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.parse("supercoffi:redstone"))
   );

   private RedstoneItem(Properties properties) {
      super(properties);
   }

   public static class Boots extends RedstoneItem {
      public Boots(Properties properties) {
         super(properties.humanoidArmor(ARMOR_MATERIAL, ArmorType.BOOTS));
      }
   }

   public static class Chestplate extends RedstoneItem {
      public Chestplate(Properties properties) {
         super(properties.humanoidArmor(ARMOR_MATERIAL, ArmorType.CHESTPLATE));
      }
   }

   public static class Helmet extends RedstoneItem {
      public Helmet(Properties properties) {
         super(properties.humanoidArmor(ARMOR_MATERIAL, ArmorType.HELMET));
      }
   }

   public static class Leggings extends RedstoneItem {
      public Leggings(Properties properties) {
         super(properties.humanoidArmor(ARMOR_MATERIAL, ArmorType.LEGGINGS));
      }
   }
}
