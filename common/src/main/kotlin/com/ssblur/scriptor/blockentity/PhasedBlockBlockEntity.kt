package com.ssblur.scriptor.blockentity

import com.mojang.datafixers.util.Pair
import com.ssblur.scriptor.block.ScriptorBlocks
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
import kotlin.math.min

class PhasedBlockBlockEntity(blockPos: BlockPos, blockState: BlockState) :
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
            level!!.setBlock(blockPos, phasedBlockState!!, 22)
            if (data != null && !level!!.isClientSide) {
                val entity = loadStatic(blockPos, phasedBlockState!!, data!!, level!!.registryAccess())
                if (entity != null) level!!.setBlockEntity(entity)
            }
        }
    }

    override fun getUpdatePacket(): Packet<ClientGamePacketListener>? {
        return ClientboundBlockEntityDataPacket.create(this)
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
        BlockState.CODEC.decode(NbtOps.INSTANCE, state).result()
            .ifPresent { result: Pair<BlockState?, Tag> -> phasedBlockState = result.first }

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
            if (level == null) return 0f

            if (created == -1L) created = level!!.gameTime

            val f = (ANIM_DURATION.toFloat()) / 5f
            val anim = min(
                ANIM_DURATION.toDouble(),
                (level!!.gameTime - created).toDouble()
            ).toLong()
            if (countdown == -1) return anim / ANIM_DURATION.toFloat()
            return (min(anim.toDouble(), (countdown * f).toDouble()) / ANIM_DURATION.toFloat()).toFloat()
        }

    companion object {
        const val ANIM_DURATION: Int = 5
        fun <T : BlockEntity?> tick(entity: T) {
            if (entity is PhasedBlockBlockEntity) entity.tick()
        }

        @JvmStatic
        @JvmOverloads
        fun phase(level: Level, pos: BlockPos, duration: Int = 5) {
            if (level.getBlockEntity(pos) is PhasedBlockBlockEntity) {
                (level.getBlockEntity(pos) as PhasedBlockBlockEntity).countdown = duration
                return
            }

            val state = level.getBlockState(pos)
            val entity = level.getBlockEntity(pos)

            if (state.`is`(ScriptorBlocks.DO_NOT_PHASE) || state.liquid() || state.isAir) return

            val newState = ScriptorBlocks.PHASED_BLOCK.get().defaultBlockState()
            val newEntity = PhasedBlockBlockEntity(pos, newState)
            newEntity.phasedBlockState = state
            if (entity != null) newEntity.data = entity.saveWithFullMetadata(level.registryAccess())
            newEntity.countdown = duration

            level.removeBlockEntity(pos)
            level.setBlock(pos, newState, 22)
            level.setBlockEntity(newEntity)

            level.sendBlockUpdated(pos, newState, newState, 7)
        }
    }
}
