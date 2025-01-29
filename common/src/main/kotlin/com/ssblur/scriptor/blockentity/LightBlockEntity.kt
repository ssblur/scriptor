package com.ssblur.scriptor.blockentity

import com.ssblur.scriptor.color.interfaces.Colorable
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class LightBlockEntity(blockPos: BlockPos, blockState: BlockState) :
    BlockEntity(ScriptorBlockEntities.LIGHT.get(), blockPos, blockState), Colorable {
    var colorInternal: Int = 0
    fun getColor(): Int {
        return colorInternal
    }

    override fun setColor(color: Int) {
        this.colorInternal = color
        setChanged()
        if (level != null) level!!.sendBlockUpdated(blockPos, blockState, blockState, 2)
    }

    override fun getUpdatePacket(): Packet<ClientGamePacketListener>? {
        return ClientboundBlockEntityDataPacket.create(this)
    }

    override fun getUpdateTag(provider: HolderLookup.Provider): CompoundTag {
        val tag = super.getUpdateTag(provider)
        tag.putInt("scriptor:color", colorInternal)
        return tag
    }

    public override fun loadAdditional(tag: CompoundTag, provider: HolderLookup.Provider) {
        super.loadAdditional(tag, provider)
        colorInternal = tag.getInt("scriptor:color")
        setChanged()
    }

    override fun saveAdditional(tag: CompoundTag, provider: HolderLookup.Provider) {
        super.saveAdditional(tag, provider)

        tag.putInt("scriptor:color", colorInternal)
    }
}
