package net.mcreator.supercoffi.init;

import net.mcreator.supercoffi.client.renderer.SoulskeletonRenderer;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers;

@EventBusSubscriber(Dist.CLIENT)
public class SupercoffiModEntityRenderers {
   @SubscribeEvent
   public static void registerEntityRenderers(RegisterRenderers event) {
      event.registerEntityRenderer((EntityType)SupercoffiModEntities.SOULSKELETON.get(), SoulskeletonRenderer::new);
   }
}
