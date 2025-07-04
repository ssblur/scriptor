package com.ssblur.scriptor.blockentity

import com.ssblur.scriptor.block.PhasedBlock
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
import net.minecraft.world.level.material.Fluids
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

    try {
      val state = BlockState.CODEC.encodeStart(NbtOps.INSTANCE, phasedBlockState)
      state.result().ifPresent { result: Tag -> tag.put("blockState", result) }
    } catch (_: NullPointerException) {}
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
    var INVERT_DO_NOT_PHASE = false
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
      if(invalidForPhasing(state, level, pos)) return

      if (level.isClientSide && (phasable(state, level, pos) != INVERT_DO_NOT_PHASE)) return
      if (!level.isClientSide && (phasable(state, level, pos) != ScriptorConfig.INVERT_DO_NOT_PHASE())) return

      var data: CompoundTag? = null
      if(entity != null) data = entity.saveWithFullMetadata(level.registryAccess())
      level.removeBlockEntity(pos)

      var newState = ScriptorBlocks.PHASED_BLOCK.get().defaultBlockState()
      if(level.getFluidState(pos).`is`(Fluids.EMPTY)) newState = newState.setValue(PhasedBlock.WATERLOGGED, false)
      else newState = newState.setValue(PhasedBlock.WATERLOGGED, true)
      level.setBlockAndUpdate(pos, newState)

      val newEntity = PhasedBlockBlockEntity(pos, newState)
      newEntity.level = level
      newEntity.phasedBlockState = state
      newEntity.countdown = duration
      if (data != null) newEntity.data = data
      level.setBlockEntity(newEntity)

      level.sendBlockUpdated(pos, newState, newState, 3)
    }

    fun invalidForPhasing(state: BlockState, level: Level, pos: BlockPos): Boolean {
      @Suppress("DEPRECATION")
      if(state.liquid() || state.isAir) return true
      if(state.getCollisionShape(level, pos).isEmpty) return true
      if(state.getDestroySpeed(level, pos) < 0f) return true
      return false
    }

    fun phasable(state: BlockState, level: Level, pos: BlockPos): Boolean {
      return state.`is`(ScriptorBlocks.DO_NOT_PHASE)
    }
  }
}
