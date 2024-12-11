package com.ssblur.scriptor.blockentity

import com.ssblur.scriptor.config.ScriptorGameRules
import com.ssblur.scriptor.data.saved_data.DictionarySavedData
import com.ssblur.scriptor.events.network.client.ParticleNetwork
import com.ssblur.scriptor.helpers.targetable.Targetable
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

open class ChalkBlockEntity : BlockEntity {
    @JvmField
    var word: String
    @JvmField
    var facing: Direction

    constructor(blockPos: BlockPos, blockState: BlockState) : super(
        ScriptorBlockEntities.CHALK.get(),
        blockPos,
        blockState
    ) {
        word = ""
        facing = Direction.EAST
    }

    constructor(blockEntityType: BlockEntityType<*>, blockPos: BlockPos, blockState: BlockState) : super(
        blockEntityType,
        blockPos,
        blockState
    ) {
        word = ""
        facing = Direction.EAST
    }

    fun cast() {
        this.cast(ArrayList(), "", true)
    }

    open fun cast(visited: MutableList<BlockPos>, initialWords: String, primary: Boolean) {
        var words = initialWords
        var continued = false
        visited.add(blockPos)
        if (!visited.contains(blockPos.north()) && level!!.getBlockEntity(blockPos.north()) is ChalkBlockEntity) {
            (level!!.getBlockEntity(blockPos.north()) as ChalkBlockEntity).cast(visited, "$words $word", primary)
            continued = true
        }

        if (!visited.contains(blockPos.south()) && level!!.getBlockEntity(blockPos.south()) is ChalkBlockEntity) {
            (level!!.getBlockEntity(blockPos.south()) as ChalkBlockEntity).cast(visited, "$words $word", !continued && primary)
            continued = true
        }

        if (!visited.contains(blockPos.east()) && level!!.getBlockEntity(blockPos.east()) is ChalkBlockEntity) {
            (level!!.getBlockEntity(blockPos.east()) as ChalkBlockEntity).cast(visited, "$words $word", !continued && primary)
            continued = true
        }

        if (!visited.contains(blockPos.west()) && level!!.getBlockEntity(blockPos.west()) is ChalkBlockEntity) {
            (level!!.getBlockEntity(blockPos.west()) as ChalkBlockEntity).cast(visited, "$words $word", !continued && primary)
            continued = true
        }

        if (continued) return

        if (level is ServerLevel) {
            val server = level as ServerLevel
            words = "$words $word"
            val spell = DictionarySavedData.computeIfAbsent(server).parse(words.trim { it <= ' ' })
            if (spell != null && spell.cost() < server.getGameRules().getInt(ScriptorGameRules.CHALK_MAX_COST!!)) {
                val target = Targetable(server, blockPos)
                target.setFacing(facing)
                for (block in visited) server.setBlockAndUpdate(block, Blocks.AIR.defaultBlockState())
                server.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState())
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

    override fun getUpdatePacket(): Packet<ClientGamePacketListener>? {
        return ClientboundBlockEntityDataPacket.create(this)
    }

    override fun getUpdateTag(provider: HolderLookup.Provider): CompoundTag {
        val tag = super.getUpdateTag(provider)
        tag.putString("scriptor:word", word)
        tag.putInt("scriptor:facing", facing.ordinal)
        return tag
    }

    public override fun loadAdditional(tag: CompoundTag, provider: HolderLookup.Provider) {
        super.loadAdditional(tag, provider)
        word = tag.getString("scriptor:word")
        facing = Direction.entries[tag.getInt("scriptor:facing")]
        setChanged()
    }

    override fun saveAdditional(tag: CompoundTag, provider: HolderLookup.Provider) {
        super.saveAdditional(tag, provider)

        tag.putString("scriptor:word", word)
        tag.putInt("scriptor:facing", facing.ordinal)
    }
}
