package net.mcreator.supercoffi.init;

import net.mcreator.supercoffi.entity.SoulskeletonEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType.Builder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber
public class SupercoffiModEntities {
   public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, "supercoffi");
   public static final DeferredHolder<EntityType<?>, EntityType<SoulskeletonEntity>> SOULSKELETON = register(
      "soulskeleton",
      Builder.of(SoulskeletonEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .fireImmune()
         .sized(0.6F, 1.95F)
   );

   private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String registryname, Builder<T> entityTypeBuilder) {
      return REGISTRY.register(
         registryname, () -> entityTypeBuilder.build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("supercoffi", registryname)))
      );
   }

   @SubscribeEvent
   public static void init(RegisterSpawnPlacementsEvent event) {
      SoulskeletonEntity.init(event);
   }

   @SubscribeEvent
   public static void registerAttributes(EntityAttributeCreationEvent event) {
      event.put((EntityType)SOULSKELETON.get(), SoulskeletonEntity.createAttributes().build());
   }
}
