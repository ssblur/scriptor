package com.ssblur.scriptor.blockentity

import com.ssblur.scriptor.block.EngravingBlock
import com.ssblur.scriptor.block.GenerateBlock
import com.ssblur.scriptor.block.ScriptorBlocks
import com.ssblur.scriptor.data.saved_data.DictionarySavedData
import com.ssblur.scriptor.resources.Engravings
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class GenerateBlockEntity(blockPos: BlockPos, blockState: BlockState) :
    BlockEntity(ScriptorBlockEntities.GENERATE.get(), blockPos, blockState) {
    companion object {
        fun generateEngraving(level: ServerLevel, pos: BlockPos) {
            val random = level.random

            val engraving = Engravings.getRandomEngraving()
            val words = DictionarySavedData.computeIfAbsent(level)
                .generate(engraving.generateSpell())
                .split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val x = if(random.nextBoolean()) 1 else -1
            val z = if(random.nextBoolean()) 1 else -1
            var curPos = pos
            var first = true
            var axis: Boolean

            for (word in words) {
                engrave(level, curPos, word, first)
                first = false
                axis = random.nextBoolean()
                curPos = curPos.offset(
                    if(axis) x else 0,
                    0,
                    if(!axis) z else 0
                )
            }
        }

        @Suppress("redundantsuppression", "deprecation")
        fun engrave(level: ServerLevel, pos: BlockPos, word: String?, bold: Boolean) {
            if (!level.getBlockState(pos.offset(0, -1, 0)).isSolid) level.setBlockAndUpdate(
                pos.offset(0, -1, 0),
                Blocks.STONE.defaultBlockState()
            )

            var blockState = ScriptorBlocks.ENGRAVING.get().defaultBlockState()
            if (bold) blockState = blockState.setValue(EngravingBlock.HIGHLIGHT, true)
            level.setBlockAndUpdate(pos, blockState)
            val entity = EngravingBlockEntity(pos, blockState)
            entity.word = word!!
            level.setBlockEntity(entity)
        }

        @Suppress("unused_parameter")
        fun <T : BlockEntity?> tick(level: Level, pos: BlockPos, state: BlockState, entity: T) {
            if (level.isClientSide) return

            if (state.getValue(GenerateBlock.FEATURE) == GenerateBlock.Feature.ENGRAVING) {
                generateEngraving(level as ServerLevel, pos)
            }
        }
    }
}
