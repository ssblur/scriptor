package com.ssblur.scriptor.blockentity

import com.ssblur.scriptor.block.EngravingBlock
import com.ssblur.scriptor.block.GenerateBlock
import com.ssblur.scriptor.block.ScriptorBlocks
import com.ssblur.scriptor.config.ScriptorConfig
import com.ssblur.scriptor.data.saved_data.DictionarySavedData
import com.ssblur.scriptor.helpers.ShapeHelper
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
      val engraving = Engravings.getRandomEngraving()
      val words = DictionarySavedData.computeIfAbsent(level)
        .generate(engraving.generateSpell()).string
        .split(" ".toRegex()).filter { !it.isEmpty() }.toTypedArray()
      level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())
      val spiral = ShapeHelper.invertedSpiral().iterator()
      var first = true
      for (word in words) {
        val n = spiral.next().let { pos.offset(it.x, 0, it.y) }
        engrave(level, n, word, first)
        first = false
      }
    }

    fun generateVillagerEngraving(level: ServerLevel, pos: BlockPos) {
      val engraving = VillagerEngravings.getRandomEngraving()
      val words = DictionarySavedData.computeIfAbsent(level)
        .generate(engraving.generateSpell()).string
        .split(" ".toRegex()).filter { !it.isEmpty() }.toTypedArray()
      level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())
      val spiral = ShapeHelper.spiral().iterator()
      var first = true
      for (word in words) {
        val n = spiral.next().let { pos.offset(it.x, 0, it.y) }
        println(n)
        engrave(level, n, word, first)
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
