package net.mcreator.supercoffi.item;

import java.util.List;
import java.util.Optional;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.component.BlocksAttacks.DamageReduction;
import net.minecraft.world.item.component.BlocksAttacks.ItemDamageFunction;

public class ShadowshildItem extends ShieldItem {
   public ShadowshildItem(Properties properties) {
      super(
         properties.repairable(TagKey.create(Registries.ITEM, Identifier.parse("supercoffi:shadowshild_repair_items")))
            .component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK)
            .equippableUnswappable(EquipmentSlot.OFFHAND)
            .delayedComponent(
               DataComponents.BLOCKS_ATTACKS,
               context -> new BlocksAttacks(
                  0.25F,
                  1.0F,
                  List.of(new DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                  new ItemDamageFunction(3.0F, 1.0F, 1.0F),
                  Optional.of(context.getOrThrow(DamageTypeTags.BYPASSES_SHIELD)),
                  Optional.of(SoundEvents.SHIELD_BLOCK),
                  Optional.of(SoundEvents.SHIELD_BREAK)
               )
            )
            .durability(100)
      );
   }
}
