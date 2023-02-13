package com.ssblur.scriptor.block;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.block.blockstates.HorizontalFacing;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class ScriptorBlocks {
  public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ScriptorMod.MOD_ID, Registry.BLOCK_REGISTRY);

  public static final RegistrySupplier<Block> RUNE = BLOCKS.register("rune", RuneBlock::new);
  public static final RegistrySupplier<Block> WRITING_DESK = BLOCKS.register("writing_desk", WritingDeskBlock::new);

  public static void register() {
    BLOCKS.register();
  }

  public static EnumProperty<HorizontalFacing> HorizontalProperty = EnumProperty.create("facing", HorizontalFacing.class);
}
