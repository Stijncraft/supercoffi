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

public abstract class PrismarmorItem extends Item {
   public static ArmorMaterial ARMOR_MATERIAL = new ArmorMaterial(
      15,
      Map.of(ArmorType.BOOTS, 100, ArmorType.LEGGINGS, 100, ArmorType.CHESTPLATE, 100, ArmorType.HELMET, 100, ArmorType.BODY, 100),
      9,
      BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.EMPTY),
      0.0F,
      0.0F,
      TagKey.create(Registries.ITEM, Identifier.parse("supercoffi:prismarmor_repair_items")),
      ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.parse("supercoffi:prismarmor"))
   );

   private PrismarmorItem(Properties properties) {
      super(properties);
   }

   public static class Boots extends PrismarmorItem {
      public Boots(Properties properties) {
         super(properties.humanoidArmor(ARMOR_MATERIAL, ArmorType.BOOTS));
      }
   }

   public static class Chestplate extends PrismarmorItem {
      public Chestplate(Properties properties) {
         super(properties.humanoidArmor(ARMOR_MATERIAL, ArmorType.CHESTPLATE));
      }
   }

   public static class Helmet extends PrismarmorItem {
      public Helmet(Properties properties) {
         super(properties.humanoidArmor(ARMOR_MATERIAL, ArmorType.HELMET));
      }
   }

   public static class Leggings extends PrismarmorItem {
      public Leggings(Properties properties) {
         super(properties.humanoidArmor(ARMOR_MATERIAL, ArmorType.LEGGINGS));
      }
   }
}
