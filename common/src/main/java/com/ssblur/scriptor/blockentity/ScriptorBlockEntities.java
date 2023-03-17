package com.ssblur.scriptor.blockentity;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.block.RuneBlock;
import com.ssblur.scriptor.block.ScriptorBlocks;
import com.ssblur.scriptor.blockentity.renderers.RuneBlockEntityRenderer;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.EnvType;
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

    if(!Platform.isForge() && Platform.getEnv() == EnvType.CLIENT) registerRenderers();
  }

  public static void registerRenderers() {
    BlockEntityRendererRegistry.register(RUNE.get(), RuneBlockEntityRenderer::new);
  }

}
