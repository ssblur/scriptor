package com.ssblur.scriptor.blockentity

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.block.ScriptorBlocks
import com.ssblur.scriptor.blockentity.renderers.*
import dev.architectury.platform.Platform
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.fabricmc.api.EnvType
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

object ScriptorBlockEntities {
    val BLOCK_ENTITIES: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(ScriptorMod.MOD_ID, Registries.BLOCK_ENTITY_TYPE)

    val RUNE: RegistrySupplier<BlockEntityType<RuneBlockEntity>> = BLOCK_ENTITIES.register("rune") {
        BlockEntityType.Builder.of(
            { blockPos, blockState -> RuneBlockEntity(blockPos, blockState) },
            ScriptorBlocks.RUNE.get()
        ).build(null)
    }

    val LIGHT: RegistrySupplier<BlockEntityType<LightBlockEntity>> = BLOCK_ENTITIES.register("light") {
        BlockEntityType.Builder.of(
            { blockPos, blockState -> LightBlockEntity(blockPos, blockState) },
            ScriptorBlocks.LIGHT.get()
        ).build(null)
    }

    val CHALK: RegistrySupplier<BlockEntityType<ChalkBlockEntity>> = BLOCK_ENTITIES.register("chalk") {
        BlockEntityType.Builder.of(
            { blockPos, blockState -> ChalkBlockEntity(blockPos, blockState) },
            ScriptorBlocks.CHALK.get()
        ).build(null)
    }

    val ENGRAVING: RegistrySupplier<BlockEntityType<EngravingBlockEntity>> = BLOCK_ENTITIES.register("engraving") {
        BlockEntityType.Builder.of(
            { blockPos, blockState -> EngravingBlockEntity(blockPos, blockState) },
            ScriptorBlocks.ENGRAVING.get()
        ).build(null)
    }

    val CASTING_LECTERN: RegistrySupplier<BlockEntityType<CastingLecternBlockEntity>> = BLOCK_ENTITIES.register("casting_lectern") {
        BlockEntityType.Builder.of(
            { blockPos: BlockPos, blockState: BlockState ->
                CastingLecternBlockEntity(blockPos, blockState)
            }, ScriptorBlocks.CASTING_LECTERN.first.get()
        ).build(null)
    }

    val PHASED_BLOCK: RegistrySupplier<BlockEntityType<PhasedBlockBlockEntity>> = BLOCK_ENTITIES.register("phased_block") {
        BlockEntityType.Builder.of(
            { blockPos: BlockPos?, blockState: BlockState? ->
                PhasedBlockBlockEntity(
                    blockPos!!, blockState!!
                )
            }, ScriptorBlocks.PHASED_BLOCK.get()
        ).build(null)
    }

    val GENERATE: RegistrySupplier<BlockEntityType<GenerateBlockEntity>> = BLOCK_ENTITIES.register("generate") {
        BlockEntityType.Builder.of(
            { blockPos, blockState -> GenerateBlockEntity(blockPos, blockState) },
            ScriptorBlocks.GENERATE.get()
        ).build(null)
    }

    fun register() {
        BLOCK_ENTITIES.register()

        if (!Platform.isForgeLike() && Platform.getEnv() == EnvType.CLIENT) registerRenderers()
    }

    @JvmStatic
    fun registerRenderers() {
        BlockEntityRendererRegistry.register(RUNE.get()) { RuneBlockEntityRenderer(it) }
        BlockEntityRendererRegistry.register(CHALK.get()) { ChalkBlockEntityRenderer(it) }
        BlockEntityRendererRegistry.register(ENGRAVING.get()) { EngravingBlockEntityRenderer(it) }
        BlockEntityRendererRegistry.register(CASTING_LECTERN.get()) { CastingLecternBlockEntityRenderer(it) }
        BlockEntityRendererRegistry.register(LIGHT.get()) { LightBlockEntityRenderer(it) }
        BlockEntityRendererRegistry.register(PHASED_BLOCK.get()) { PhasedBlockBlockEntityRenderer(it) }
    }
}
