package com.ssblur.scriptor.blockentity

import com.ssblur.scriptor.block.EngravingBlock
import com.ssblur.scriptor.block.GenerateBlock
import com.ssblur.scriptor.block.ScriptorBlocks
import com.ssblur.scriptor.config.ScriptorConfig
import com.ssblur.scriptor.data.saved_data.DictionarySavedData
import com.ssblur.scriptor.helpers.ShapeHelper
import com.ssblur.scriptor.item.books.Artifact
import com.ssblur.scriptor.resources.Artifacts
import com.ssblur.scriptor.resources.Engravings
import com.ssblur.scriptor.resources.VillagerEngravings
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.max
import kotlin.math.min

class GenerateBlockEntity(blockPos: BlockPos, blockState: BlockState):
  BlockEntity(ScriptorBlockEntities.GENERATE.get(), blockPos, blockState) {
  companion object {
    fun generateEngraving(level: ServerLevel, pos: BlockPos) {
      val engraving = Engravings.getRandomEngraving()
      val words = DictionarySavedData.computeIfAbsent(level)
        .generate(engraving.generateSpell()).string
        .split(" ".toRegex()).filter { !it.isEmpty() }.toTypedArray()
      level.removeBlockEntity(pos)
      level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())

      var nx = 0
      var nz = 0
      var px = 0
      var pz = 0

      var spiral = ShapeHelper.invertedSpiral().iterator()
      words.forEach { _ ->
        spiral.next().let {
          pos.offset(it.x, 0, it.y)
          nx = min(nx, it.x)
          nz = min(nz, it.y)
          px = max(px, it.x)
          pz = max(pz, it.y)
        }
      }

      nx -= 1
      nz -= 1
      px += 1
      pz += 1
      for(x in nx..px)
        for(z in nz..pz)
          level.setBlockAndUpdate(pos.offset(x, -1, z), stoneBricks(level))

      for(z in nz..pz) {
        if(level.random.nextInt(2) == 0)
          level.setBlockAndUpdate(pos.offset(nx, 0, z), stoneBrickWall(level))
        if(level.random.nextInt(2) == 0)
          level.setBlockAndUpdate(pos.offset(px, 0, z), stoneBrickWall(level))
      }

      for(x in nx..px) {
        if(level.random.nextInt(2) == 0)
          level.setBlockAndUpdate(pos.offset(x, 0, nz), stoneBrickWall(level))
        if(level.random.nextInt(2) == 0)
          level.setBlockAndUpdate(pos.offset(x, 0, pz), stoneBrickWall(level))
      }

      spiral = ShapeHelper.invertedSpiral().iterator()
      var first = true
      for(word in words) {
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

    fun generateArtifact(level: ServerLevel, pos: BlockPos) {
      level.setBlockAndUpdate(pos, Blocks.CHEST.defaultBlockState())
      BlockEntityType.CHEST.create(pos, Blocks.CHEST.defaultBlockState())?.let {
        val item = ItemStack(Artifact.ARTIFACTS.random())
        Artifacts.getRandomArtifact().applyToItem(item, level)
        it.setItem(0, item)
        level.setBlockEntity(it)
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

    fun stoneBricks(level: Level) =
      when(level.random.nextInt(4)) {
        0 -> Blocks.CRACKED_STONE_BRICKS
        1 -> Blocks.MOSSY_STONE_BRICKS
        else -> Blocks.STONE_BRICKS
      }.defaultBlockState()

    fun stoneBrickWall(level: Level) =
      when(level.random.nextInt(4)) {
        0 -> Blocks.MOSSY_STONE_BRICK_WALL
        else -> Blocks.STONE_BRICK_WALL
      }.defaultBlockState()

    @Suppress("unused_parameter")
    fun <T: BlockEntity?> tick(level: Level, pos: BlockPos, state: BlockState, entity: T) {
      if (level.isClientSide || ScriptorConfig.DO_NOT_GENERATE()) return

      when(state.getValue(GenerateBlock.FEATURE)) {
        GenerateBlock.Feature.NONE -> {}
        GenerateBlock.Feature.ENGRAVING -> generateEngraving(level as ServerLevel, pos)
        GenerateBlock.Feature.VILLAGER_ENGRAVING -> generateVillagerEngraving(level as ServerLevel, pos)
        GenerateBlock.Feature.ARTIFACT -> generateArtifact(level as ServerLevel, pos)
      }
    }
  }
}
