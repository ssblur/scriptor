package com.ssblur.scriptor.blockentity

import com.ssblur.scriptor.block.ScriptorBlocks
import com.ssblur.scriptor.config.ScriptorConfig
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.max
import kotlin.math.min

class PhasedBlockBlockEntity(blockPos: BlockPos, blockState: BlockState):
  BlockEntity(ScriptorBlockEntities.PHASED_BLOCK.get(), blockPos, blockState) {
  var data: CompoundTag? = null
  var phasedBlockState: BlockState? = null
  var countdown: Int
  var created: Long

  init {
    created = -1
    countdown = -1
  }

  fun tick() {
    countdown--
    if (countdown <= 0 && level != null && phasedBlockState != null) {
      if (level!!.isClientSide) return
      level!!.setBlockAndUpdate(blockPos, phasedBlockState!!)
      if (data != null && !level!!.isClientSide) {
        val entity = loadStatic(blockPos, phasedBlockState!!, data!!, level!!.registryAccess())
        if (entity != null) level!!.setBlockEntity(entity)
      } else {
        level!!.removeBlockEntity(blockPos)
      }
    }
  }

  override fun getUpdatePacket(): Packet<ClientGamePacketListener>? =
    ClientboundBlockEntityDataPacket.create(this) { entity, provider ->
      entity.getUpdateTag(provider)
    }

  override fun getUpdateTag(provider: HolderLookup.Provider): CompoundTag {
    val tag = super.getUpdateTag(provider)

    saveAdditional(tag, provider)

    return tag
  }

  public override fun loadAdditional(tag: CompoundTag, provider: HolderLookup.Provider) {
    super.loadAdditional(tag, provider)

    data = tag.getCompound("data")
    val state = tag["blockState"]
    BlockState.CODEC.decode(NbtOps.INSTANCE, state).result().ifPresent { result -> phasedBlockState = result.first }

    setChanged()
  }

  override fun saveAdditional(tag: CompoundTag, provider: HolderLookup.Provider) {
    super.saveAdditional(tag, provider)

    if (data != null) tag.put("data", data!!)

    val state = BlockState.CODEC.encodeStart(NbtOps.INSTANCE, phasedBlockState)
    state.result().ifPresent { result: Tag -> tag.put("blockState", result) }
  }

  val anim: Float
    get() {
      if (level == null) return ANIM_FLOOR

      if (created == -1L) created = level!!.gameTime

      var anim = level!!.gameTime - created
      anim = min(anim, ANIM_DURATION)
      return (anim.toFloat() / ANIM_DURATION) * ANIM_DIFF + ANIM_FLOOR
    }

  companion object {
    const val ANIM_DURATION = 5L
    const val ANIM_FLOOR = 0.2f
    const val ANIM_DIFF = 1f - ANIM_FLOOR
    fun <T: BlockEntity?> tick(entity: T) {
      if (entity is PhasedBlockBlockEntity) entity.tick()
    }

    @JvmStatic
    @JvmOverloads
    fun phase(level: Level, pos: BlockPos, duration: Int = 5) {
      val entity = level.getBlockEntity(pos)
      if (entity is PhasedBlockBlockEntity) {
        entity.countdown = max(duration, entity.countdown)
        return
      }

      val state = level.getBlockState(pos)
      @Suppress("DEPRECATION")
      if ((state.`is`(ScriptorBlocks.DO_NOT_PHASE) != ScriptorConfig.INVERT_DO_NOT_PHASE()) || state.liquid() || state.isAir) return

      val newState = ScriptorBlocks.PHASED_BLOCK.get().defaultBlockState()
      level.setBlockAndUpdate(pos, newState)
      val newEntity = ScriptorBlockEntities.PHASED_BLOCK.get().getBlockEntity(level, pos)!!
      newEntity.phasedBlockState = state
      if (entity != null) newEntity.data = entity.saveWithFullMetadata(level.registryAccess())
      newEntity.countdown = duration

      level.sendBlockUpdated(pos, newState, newState, 3)
    }
  }
}
