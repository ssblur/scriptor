package com.ssblur.scriptor.blockentity

import com.ssblur.scriptor.data.saved_data.DictionarySavedData
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.network.client.ParticleNetwork
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.max

class EngravingBlockEntity(blockPos: BlockPos, blockState: BlockState):
  ChalkBlockEntity(ScriptorBlockEntities.ENGRAVING.get(), blockPos, blockState) {
  var cooldown: Int = 0
  override fun cast(visited: MutableList<BlockPos>, initialWords: String, primary: Boolean) {
    var words = initialWords
    if (cooldown > 0) {
      ParticleNetwork.fizzle(level!!, blockPos)
      level!!.playSound(
        null,
        this.blockPos,
        SoundEvents.FIRE_EXTINGUISH,
        SoundSource.BLOCKS,
        1.0f,
        level!!.getRandom().nextFloat() * 0.4f + 0.8f
      )
      return
    }

    var continued = false
    level!!
    visited.add(blockPos)
    if (!visited.contains(blockPos.north()) && level!!.getBlockEntity(blockPos.north()) is EngravingBlockEntity) {
      (level!!.getBlockEntity(blockPos.north()) as EngravingBlockEntity).cast(visited, "$words $word", primary)
      continued = true
    }

    if (!visited.contains(blockPos.south()) && level!!.getBlockEntity(blockPos.south()) is EngravingBlockEntity) {
      (level!!.getBlockEntity(blockPos.south()) as EngravingBlockEntity).cast(
        visited,
        "$words $word",
        !continued && primary
      )
      continued = true
    }

    if (!visited.contains(blockPos.east()) && level!!.getBlockEntity(blockPos.east()) is EngravingBlockEntity) {
      (level!!.getBlockEntity(blockPos.east()) as EngravingBlockEntity).cast(
        visited,
        "$words $word",
        !continued && primary
      )
      continued = true
    }

    if (!visited.contains(blockPos.west()) && level!!.getBlockEntity(blockPos.west()) is EngravingBlockEntity) {
      (level!!.getBlockEntity(blockPos.west()) as EngravingBlockEntity).cast(
        visited,
        "$words $word",
        !continued && primary
      )
      continued = true
    }

    if (continued) return

    if (level is ServerLevel) {
      val server = level as ServerLevel
      words = "$words $word"
      val spell = DictionarySavedData.computeIfAbsent(server).parse(words.trim { it <= ' ' })
      if (spell != null) {
        val target = Targetable(server, blockPos)
        target.setFacing(facing)
        for (block in visited) if (server.getBlockEntity(block) is EngravingBlockEntity) {
          val engraving = server.getBlockEntity(block) as EngravingBlockEntity
          engraving.cooldown += (spell.cost() * 20).toInt()
        }
        this.cooldown = (this.cooldown + (spell.cost() * 20)).toInt()
        spell.cast(target)
      } else if (primary) {
        ParticleNetwork.fizzle(server, visited[0])
        server.playSound(
          null,
          visited[0],
          SoundEvents.FIRE_EXTINGUISH,
          SoundSource.BLOCKS,
          1.0f,
          server.getRandom().nextFloat() * 0.4f + 0.8f
        )
      }
    }
  }

  companion object {
    fun <T: BlockEntity?> tick(level: Level, entity: T) {
      if (level.isClientSide) return
      if (entity is EngravingBlockEntity) entity.cooldown = max(0.0, (entity.cooldown - 1).toDouble()).toInt()
    }
  }
}
