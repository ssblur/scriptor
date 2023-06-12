package com.ssblur.scriptor.block;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.item.Spellbook;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ScriptorBlocks {
  public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ScriptorMod.MOD_ID, Registries.BLOCK);

  public static final RegistrySupplier<Block> RUNE = BLOCKS.register("rune", RuneBlock::new);
  public static final RegistrySupplier<Block> LIGHT = BLOCKS.register("light", LightBlock::new);
  public static final RegistrySupplier<Block> CASTING_LECTERN = BLOCKS.register("casting_lectern", CastingLecternBlock::new);

  public static void register() {
    BLOCKS.register();
  }
}
