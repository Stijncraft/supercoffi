package net.mcreator.supercoffi.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber
public class SupercoffiModTabs {
   public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "supercoffi");

   @SubscribeEvent
   public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
      if (tabData.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
         tabData.accept((ItemLike)SupercoffiModItems.COFFI.get());
      } else if (tabData.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
         tabData.accept(((Block)SupercoffiModBlocks.STARBUKS.get()).asItem());
      } else if (tabData.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
         tabData.accept((ItemLike)SupercoffiModItems.SHITE.get());
         tabData.accept((ItemLike)SupercoffiModItems.SUPERSCHITE.get());
         tabData.accept((ItemLike)SupercoffiModItems.STAREGE.get());
         tabData.accept((ItemLike)SupercoffiModItems.JAYSCHAINS.get());
         tabData.accept((ItemLike)SupercoffiModItems.SOULSWORD.get());
         tabData.accept((ItemLike)SupercoffiModItems.SUPE_RSCHIT.get());
         tabData.accept((ItemLike)SupercoffiModItems.DIAMONT.get());
         tabData.accept((ItemLike)SupercoffiModItems.IROND.get());
         tabData.accept((ItemLike)SupercoffiModItems.STAF.get());
         tabData.accept((ItemLike)SupercoffiModItems.AXEE.get());
         tabData.accept((ItemLike)SupercoffiModItems.SHADOWSHITE.get());
      } else if (tabData.getTabKey() == CreativeModeTabs.COMBAT) {
         tabData.accept((ItemLike)SupercoffiModItems.STAREGE.get());
         tabData.accept((ItemLike)SupercoffiModItems.ETEBLEDIAMANTSWORD.get());
         tabData.accept((ItemLike)SupercoffiModItems.SOULKEEPER.get());
         tabData.accept((ItemLike)SupercoffiModItems.SOULSWORD.get());
         tabData.accept((ItemLike)SupercoffiModItems.SD.get());
         tabData.accept((ItemLike)SupercoffiModItems.REDSTONE_HELMET.get());
         tabData.accept((ItemLike)SupercoffiModItems.REDSTONE_CHESTPLATE.get());
         tabData.accept((ItemLike)SupercoffiModItems.REDSTONE_LEGGINGS.get());
         tabData.accept((ItemLike)SupercoffiModItems.REDSTONE_BOOTS.get());
         tabData.accept((ItemLike)SupercoffiModItems.SHADOSWORD.get());
         tabData.accept((ItemLike)SupercoffiModItems.SHADOWSHILD.get());
         tabData.accept((ItemLike)SupercoffiModItems.PRISMARMOR_HELMET.get());
         tabData.accept((ItemLike)SupercoffiModItems.PRISMARMOR_CHESTPLATE.get());
         tabData.accept((ItemLike)SupercoffiModItems.PRISMARMOR_LEGGINGS.get());
         tabData.accept((ItemLike)SupercoffiModItems.PRISMARMOR_BOOTS.get());
         tabData.accept((ItemLike)SupercoffiModItems.PRISMPIKAXE.get());
         tabData.accept((ItemLike)SupercoffiModItems.PRISM.get());
      } else if (tabData.getTabKey() == CreativeModeTabs.INGREDIENTS) {
         tabData.accept((ItemLike)SupercoffiModItems.SOUL.get());
      } else if (tabData.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
         tabData.accept((ItemLike)SupercoffiModItems.SOULSKELETON_SPAWN_EGG.get());
      }
   }
}
