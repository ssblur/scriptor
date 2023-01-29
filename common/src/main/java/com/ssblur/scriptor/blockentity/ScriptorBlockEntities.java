package com.ssblur.scriptor.blockentity;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.block.RuneBlock;
import com.ssblur.scriptor.block.ScriptorBlocks;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ScriptorBlockEntities {
  public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ScriptorMod.MOD_ID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);

  public static final RegistrySupplier<BlockEntityType<RuneBlockEntity>> RUNE = BLOCK_ENTITIES.register(
    "rune",
    () -> BlockEntityType.Builder.of(RuneBlockEntity::new, ScriptorBlocks.RUNE.get()).build(null)
  );

  public static void register() {
    BLOCK_ENTITIES.register();
  }
}
