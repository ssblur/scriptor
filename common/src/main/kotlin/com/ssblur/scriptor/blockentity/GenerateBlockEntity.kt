package com.ssblur.scriptor.blockentity

import com.ssblur.scriptor.block.EngravingBlock
import com.ssblur.scriptor.block.GenerateBlock
import com.ssblur.scriptor.block.ScriptorBlocks
import com.ssblur.scriptor.config.ScriptorConfig
import com.ssblur.scriptor.data.saved_data.DictionarySavedData
import com.ssblur.scriptor.resources.Engravings
import com.ssblur.scriptor.resources.VillagerEngravings
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class GenerateBlockEntity(blockPos: BlockPos, blockState: BlockState):
  BlockEntity(ScriptorBlockEntities.GENERATE.get(), blockPos, blockState) {
  companion object {
    fun generateEngraving(level: ServerLevel, pos: BlockPos) {
      val random = level.random

      val engraving = Engravings.getRandomEngraving()
      val words = DictionarySavedData.computeIfAbsent(level)
        .generate(engraving.generateSpell()).string
        .split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
      val x = if (random.nextBoolean()) 1 else -1
      val z = if (random.nextBoolean()) 1 else -1
      var curPos = pos
      var first = true
      var axis: Boolean

      for (word in words) {
        engrave(level, curPos, word, first)
        first = false
        axis = random.nextBoolean()
        curPos = curPos.offset(
          if (axis) x else 0,
          0,
          if (!axis) z else 0
        )
      }
    }

    // originally did some math here but honestly this is easier, faster, and doesn't break down on big numbers
    val spiral = listOf(
      BlockPos(0, 0, 0),
      BlockPos(1, 0, 0),
      BlockPos(2, 0, 0),
      BlockPos(2, 0, 1),
      BlockPos(2, 0, 2),
      BlockPos(2, 0, 3),
      BlockPos(1, 0, 3),
      BlockPos(0, 0, 3),
      BlockPos(-1, 0, 3),
      BlockPos(-2, 0, 3),
      BlockPos(-3, 0, 3),
      BlockPos(-3, 0, 2),
      BlockPos(-3, 0, 1),
      BlockPos(-3, 0, 0),
      BlockPos(-3, 0, -1),
      BlockPos(-3, 0, -2),
      BlockPos(-3, 0, -3),
    )
    fun generateVillagerEngraving(level: ServerLevel, pos: BlockPos) {
      val engraving = VillagerEngravings.getRandomEngraving()
      val words = DictionarySavedData.computeIfAbsent(level)
        .generate(engraving.generateSpell()).string
        .split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
      var curPos = pos
      var first = true
      var i = 0
      for (word in words) {
        curPos = if(i < spiral.size) pos.offset(spiral[i]) else curPos.offset(BlockPos(1, 0, 0))
        i++
        engrave(level, curPos, word, first)
        first = false
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
    fun <T: BlockEntity?> tick(level: Level, pos: BlockPos, state: BlockState, entity: T) {
      if (level.isClientSide || ScriptorConfig.DO_NOT_GENERATE()) return

      if (state.getValue(GenerateBlock.FEATURE) == GenerateBlock.Feature.ENGRAVING) {
        generateEngraving(level as ServerLevel, pos)
      } else if (state.getValue(GenerateBlock.FEATURE) == GenerateBlock.Feature.VILLAGER_ENGRAVING) {
        generateVillagerEngraving(level as ServerLevel, pos)
      }
    }
  }
}
