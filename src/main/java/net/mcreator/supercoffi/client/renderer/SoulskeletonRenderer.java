package net.mcreator.supercoffi.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mcreator.supercoffi.entity.SoulskeletonEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.npc.VillagerModel;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
import net.minecraft.client.renderer.entity.state.VillagerRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;

public class SoulskeletonRenderer extends MobRenderer<SoulskeletonEntity, VillagerRenderState, VillagerModel> {
   private final Identifier entityTexture = Identifier.parse("supercoffi:textures/entities/es.png");

   public SoulskeletonRenderer(Context context) {
      super(context, new VillagerModel(context.bakeLayer(ModelLayers.VILLAGER)), 0.5F);
      this.addLayer(new CrossedArmsItemLayer(this));
   }

   public VillagerRenderState createRenderState() {
      return new VillagerRenderState();
   }

   public void extractRenderState(SoulskeletonEntity entity, VillagerRenderState state, float partialTicks) {
      super.extractRenderState(entity, state, partialTicks);
      if (state instanceof HoldingEntityRenderState holdingState) {
         this.itemModelResolver.updateForLiving(holdingState.heldItem, entity.getMainHandItem(), ItemDisplayContext.GROUND, entity);
      }
   }

   public Identifier getTextureLocation(VillagerRenderState state) {
      return this.entityTexture;
   }

   protected void scale(VillagerRenderState state, PoseStack poseStack) {
      poseStack.scale(0.9375F, 0.9375F, 0.9375F);
   }
}
