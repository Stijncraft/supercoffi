package net.mcreator.supercoffi.init;

import java.util.function.Function;
import net.mcreator.supercoffi.block.DiamontPortalBlock;
import net.mcreator.supercoffi.block.IrondPortalBlock;
import net.mcreator.supercoffi.block.StarbuksBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredRegister.Blocks;

public class SupercoffiModBlocks {
   public static final Blocks REGISTRY = DeferredRegister.createBlocks("supercoffi");
   public static final DeferredBlock<Block> STARBUKS = register("starbuks", StarbuksBlock::new);
   public static final DeferredBlock<Block> DIAMONT_PORTAL = register("diamont_portal", DiamontPortalBlock::new);
   public static final DeferredBlock<Block> IROND_PORTAL = register("irond_portal", IrondPortalBlock::new);

   private static <B extends Block> DeferredBlock<B> register(String name, Function<Properties, ? extends B> supplier) {
      return REGISTRY.registerBlock(name, supplier);
   }
}
