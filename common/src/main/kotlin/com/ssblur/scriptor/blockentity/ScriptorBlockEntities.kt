package com.ssblur.scriptor.blockentity

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.block.ScriptorBlocks
import com.ssblur.scriptor.blockentity.renderers.*
import com.ssblur.unfocused.rendering.BlockEntityRendering.registerBlockEntityRenderer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

object ScriptorBlockEntities {
    val RUNE = ScriptorMod.registerBlockEntity("rune") {
        BlockEntityType.Builder.of(
            { blockPos, blockState -> RuneBlockEntity(blockPos, blockState) },
            ScriptorBlocks.RUNE.get()
        ).build(null)
    }

    val LIGHT = ScriptorMod.registerBlockEntity("light") {
        BlockEntityType.Builder.of(
            { blockPos, blockState -> LightBlockEntity(blockPos, blockState) },
            ScriptorBlocks.LIGHT.get()
        ).build(null)
    }

    val CHALK = ScriptorMod.registerBlockEntity("chalk") {
        BlockEntityType.Builder.of(
            { blockPos, blockState -> ChalkBlockEntity(blockPos, blockState) },
            ScriptorBlocks.CHALK.get()
        ).build(null)
    }

    val ENGRAVING = ScriptorMod.registerBlockEntity("engraving") {
        BlockEntityType.Builder.of(
            { blockPos, blockState -> EngravingBlockEntity(blockPos, blockState) },
            ScriptorBlocks.ENGRAVING.get()
        ).build(null)
    }

    val CASTING_LECTERN = ScriptorMod.registerBlockEntity("casting_lectern") {
        BlockEntityType.Builder.of(
            { blockPos: BlockPos, blockState: BlockState ->
                CastingLecternBlockEntity(blockPos, blockState)
            }, ScriptorBlocks.CASTING_LECTERN.first.get()
        ).build(null)
    }

    val PHASED_BLOCK = ScriptorMod.registerBlockEntity("phased_block") {
        BlockEntityType.Builder.of(
            { blockPos: BlockPos?, blockState: BlockState? ->
                PhasedBlockBlockEntity(
                    blockPos!!, blockState!!
                )
            }, ScriptorBlocks.PHASED_BLOCK.get()
        ).build(null)
    }

    val GENERATE = ScriptorMod.registerBlockEntity("generate") {
        BlockEntityType.Builder.of(
            { blockPos, blockState -> GenerateBlockEntity(blockPos, blockState) },
            ScriptorBlocks.GENERATE.get()
        ).build(null)
    }

    fun register() {}

    @Environment(EnvType.CLIENT)
    fun registerRenderers() {
        ScriptorMod.registerBlockEntityRenderer(RUNE) { RuneBlockEntityRenderer(it) }
        ScriptorMod.registerBlockEntityRenderer(CHALK) { ChalkBlockEntityRenderer(it) }
        ScriptorMod.registerBlockEntityRenderer(ENGRAVING) { EngravingBlockEntityRenderer(it) }
        ScriptorMod.registerBlockEntityRenderer(CASTING_LECTERN) { CastingLecternBlockEntityRenderer(it) }
        ScriptorMod.registerBlockEntityRenderer(LIGHT) { LightBlockEntityRenderer(it) }
        ScriptorMod.registerBlockEntityRenderer(PHASED_BLOCK) { PhasedBlockBlockEntityRenderer(it) }
    }
}
