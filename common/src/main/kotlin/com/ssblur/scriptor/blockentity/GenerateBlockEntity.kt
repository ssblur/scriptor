package com.ssblur.scriptor.blockentity

import com.ssblur.scriptor.block.EngravingBlock
import com.ssblur.scriptor.block.GenerateBlock
import com.ssblur.scriptor.block.ScriptorBlocks
import com.ssblur.scriptor.data.saved_data.DictionarySavedData
import com.ssblur.scriptor.resources.Engravings
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import java.util.*

class GenerateBlockEntity(blockPos: BlockPos, blockState: BlockState) :
    BlockEntity(ScriptorBlockEntities.GENERATE.get(), blockPos, blockState) {
    companion object {
        fun generateEngraving(level: ServerLevel, pos: BlockPos) {
            val random = Random()

            val engraving = Engravings.getRandomEngraving()
            val words = DictionarySavedData.computeIfAbsent(level)
                .generate(engraving.generateSpell())
                .split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val dir = Direction.fromAxisAndDirection(
                if (random.nextBoolean()) Direction.Axis.X else Direction.Axis.Z,
                if (random.nextBoolean()) Direction.AxisDirection.NEGATIVE else Direction.AxisDirection.POSITIVE
            )
            var last = dir
            var curPos = pos
            var forward = false
            var first = true

            for (word in words) {
                if (forward) {
                    engrave(level, curPos, word, false)
                    curPos = pos.relative(dir)
                    forward = false
                    continue
                }

                if (last == dir) {
                    when (random.nextInt(3)) {
                        0 -> last = dir
                        1 -> last = dir.clockWise
                        2 -> last = dir.counterClockWise
                    }
                } else if (random.nextBoolean()) {
                    last = dir
                    forward = true
                }

                engrave(level, curPos, word, first)
                first = false
                curPos = curPos.relative(last)
            }
        }

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

        fun <T : BlockEntity?> tick(level: Level, pos: BlockPos, state: BlockState, entity: T) {
            if (level.isClientSide) return

            if (state.getValue(GenerateBlock.FEATURE) == GenerateBlock.Feature.ENGRAVING) {
                generateEngraving(level as ServerLevel, pos)
            }
        }
    }
}
