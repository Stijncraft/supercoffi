package net.mcreator.supercoffi.init;

import java.util.function.Function;
import net.mcreator.supercoffi.item.AxeeItem;
import net.mcreator.supercoffi.item.CoffiItem;
import net.mcreator.supercoffi.item.DiamontItem;
import net.mcreator.supercoffi.item.EteblediamantswordItem;
import net.mcreator.supercoffi.item.IrondItem;
import net.mcreator.supercoffi.item.JayschainsItem;
import net.mcreator.supercoffi.item.PrismItem;
import net.mcreator.supercoffi.item.PrismarmorItem;
import net.mcreator.supercoffi.item.PrismpikaxeItem;
import net.mcreator.supercoffi.item.RedstoneItem;
import net.mcreator.supercoffi.item.SUPERschitItem;
import net.mcreator.supercoffi.item.SdItem;
import net.mcreator.supercoffi.item.ShadoswordItem;
import net.mcreator.supercoffi.item.ShadowshildItem;
import net.mcreator.supercoffi.item.ShadowshiteItem;
import net.mcreator.supercoffi.item.ShiteItem;
import net.mcreator.supercoffi.item.SoulItem;
import net.mcreator.supercoffi.item.SoulkeeperItem;
import net.mcreator.supercoffi.item.SoulswordItem;
import net.mcreator.supercoffi.item.StafItem;
import net.mcreator.supercoffi.item.StaregeItem;
import net.mcreator.supercoffi.item.SuperschiteItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredRegister.Items;

public class SupercoffiModItems {
   public static final Items REGISTRY = DeferredRegister.createItems("supercoffi");
   public static final DeferredItem<Item> COFFI = register("coffi", CoffiItem::new);
   public static final DeferredItem<Item> STARBUKS = block(SupercoffiModBlocks.STARBUKS);
   public static final DeferredItem<Item> SHITE = register("shite", ShiteItem::new);
   public static final DeferredItem<Item> SUPERSCHITE = register("superschite", SuperschiteItem::new);
   public static final DeferredItem<Item> STAREGE = register("starege", StaregeItem::new);
   public static final DeferredItem<Item> JAYSCHAINS = register("jayschains", JayschainsItem::new);
   public static final DeferredItem<Item> ETEBLEDIAMANTSWORD = register("eteblediamantsword", EteblediamantswordItem::new);
   public static final DeferredItem<Item> SOULKEEPER = register("soulkeeper", SoulkeeperItem::new);
   public static final DeferredItem<Item> SOULSWORD = register("soulsword", SoulswordItem::new);
   public static final DeferredItem<Item> SUPE_RSCHIT = register("supe_rschit", SUPERschitItem::new);
   public static final DeferredItem<Item> SOUL = register("soul", SoulItem::new);
   public static final DeferredItem<Item> SOULSKELETON_SPAWN_EGG = register(
      "soulskeleton_spawn_egg", properties -> new SpawnEggItem(properties.spawnEgg((EntityType)SupercoffiModEntities.SOULSKELETON.get()))
   );
   public static final DeferredItem<Item> SD = register("sd", SdItem::new);
   public static final DeferredItem<Item> REDSTONE_HELMET = register("redstone_helmet", RedstoneItem.Helmet::new);
   public static final DeferredItem<Item> REDSTONE_CHESTPLATE = register("redstone_chestplate", RedstoneItem.Chestplate::new);
   public static final DeferredItem<Item> REDSTONE_LEGGINGS = register("redstone_leggings", RedstoneItem.Leggings::new);
   public static final DeferredItem<Item> REDSTONE_BOOTS = register("redstone_boots", RedstoneItem.Boots::new);
   public static final DeferredItem<Item> DIAMONT = register("diamont", DiamontItem::new);
   public static final DeferredItem<Item> IROND = register("irond", IrondItem::new);
   public static final DeferredItem<Item> SHADOSWORD = register("shadosword", ShadoswordItem::new);
   public static final DeferredItem<Item> SHADOWSHILD = register("shadowshild", ShadowshildItem::new);
   public static final DeferredItem<Item> PRISMARMOR_HELMET = register("prismarmor_helmet", PrismarmorItem.Helmet::new);
   public static final DeferredItem<Item> PRISMARMOR_CHESTPLATE = register("prismarmor_chestplate", PrismarmorItem.Chestplate::new);
   public static final DeferredItem<Item> PRISMARMOR_LEGGINGS = register("prismarmor_leggings", PrismarmorItem.Leggings::new);
   public static final DeferredItem<Item> PRISMARMOR_BOOTS = register("prismarmor_boots", PrismarmorItem.Boots::new);
   public static final DeferredItem<Item> PRISMPIKAXE = register("prismpikaxe", PrismpikaxeItem::new);
   public static final DeferredItem<Item> PRISM = register("prism", PrismItem::new);
   public static final DeferredItem<Item> STAF = register("staf", StafItem::new);
   public static final DeferredItem<Item> AXEE = register("axee", AxeeItem::new);
   public static final DeferredItem<Item> SHADOWSHITE = register("shadowshite", ShadowshiteItem::new);

   private static <I extends Item> DeferredItem<I> register(String name, Function<Properties, ? extends I> supplier) {
      return REGISTRY.registerItem(name, supplier, Properties::new);
   }

   private static DeferredItem<Item> block(DeferredHolder<Block, Block> block) {
      return block(block, new Properties());
   }

   private static DeferredItem<Item> block(DeferredHolder<Block, Block> block, Properties properties) {
      return REGISTRY.registerItem(block.getId().getPath(), prop -> new BlockItem((Block)block.get(), prop), () -> properties);
   }
}
