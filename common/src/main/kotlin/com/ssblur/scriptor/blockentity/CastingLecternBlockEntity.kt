package com.ssblur.scriptor.blockentity

import com.ssblur.scriptor.block.CastingLecternBlock
import com.ssblur.scriptor.config.ScriptorConfig
import com.ssblur.scriptor.data.saved_data.DictionarySavedData
import com.ssblur.scriptor.helpers.LimitedBookSerializer
import com.ssblur.scriptor.helpers.targetable.LecternTargetable
import com.ssblur.scriptor.item.casters.CasterCrystal
import com.ssblur.scriptor.network.client.ParticleNetwork
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.ContainerHelper
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.max

class CastingLecternBlockEntity(blockPos: BlockPos, blockState: BlockState) :
    BlockEntity(ScriptorBlockEntities.CASTING_LECTERN.get(), blockPos, blockState) {
    var items: NonNullList<ItemStack>
    var focusTarget: Int = 0
    var cooldown: Int = 0

    init {
        items = NonNullList.withSize(2, ItemStack.EMPTY)
    }

    var spellbook: ItemStack
        get() = items[SPELLBOOK_SLOT]
        set(itemStack) {
            items[SPELLBOOK_SLOT] = itemStack
            if (level != null) level!!.sendBlockUpdated(blockPos, blockState, blockState, 3)
            setChanged()
        }

    var focus: ItemStack
        get() = items[CASTING_FOCUS_SLOT]
        set(itemStack) {
            items[CASTING_FOCUS_SLOT] = itemStack
            if (level != null) level!!.sendBlockUpdated(blockPos, blockState, blockState, 3)
            setChanged()
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
        items = NonNullList.withSize(2, ItemStack.EMPTY)
        ContainerHelper.loadAllItems(tag, items, provider)
        setChanged()
    }

    override fun saveAdditional(tag: CompoundTag, provider: HolderLookup.Provider) {
        super.saveAdditional(tag, provider)
        ContainerHelper.saveAllItems(tag, items, provider)
    }

    fun tick() {
        if (level == null || level!!.isClientSide) return
        val server = level as ServerLevel
        cooldown = max(0.0, (cooldown - 1).toDouble()).toInt()
        if (level!!.getDirectSignalTo(blockPos) == 0 && !spellbook.isEmpty && cooldown == 0) {
            val item = spellbook
            val tag = item.get(DataComponents.WRITTEN_BOOK_CONTENT)
            if (tag != null) {
                val spell = DictionarySavedData.computeIfAbsent(server).parse(LimitedBookSerializer.decodeText(tag))
                if (spell != null) {
                    if (spell.cost() > ScriptorConfig.CASTING_LECTERN_MAX_COST()) {
                        ParticleNetwork.fizzle(level!!, blockPos)
                        server.playSound(
                            null,
                            this.blockPos,
                            SoundEvents.FIRE_EXTINGUISH,
                            SoundSource.BLOCKS,
                            1.0f,
                            server.getRandom().nextFloat() * 0.4f + 0.8f
                        )
                        cooldown += Math.round(200.0 * (ScriptorConfig.CASTING_LECTERN_COOLDOWN_MULTIPLIER().toDouble() / 100.0)).toInt()
                        return
                    }
                    val state = server.getBlockState(blockPos)
                    val direction = state.getValue(CastingLecternBlock.FACING).opposite
                    val blockPos = this.blockPos
                    val pos = blockPos.center
                    val target = LecternTargetable(getLevel()!!, pos).setFacing(direction)
                    if (focus.item is CasterCrystal) {
                        val crystal = focus.item as CasterCrystal
                        val foci = crystal.getTargetables(focus, server)
                        if (!foci.isNullOrEmpty()) {
                            focusTarget++
                            focusTarget %= foci.size
                            val focus = foci[focusTarget]
                            if (focus != null && focus.targetPos.distanceTo(target.targetPos) <= 16 && focus.level === level) target.finalTargetable =
                                focus
                        }
                    }
                    spell.cast(target)
                    cooldown += Math.round(
                        spell.cost() * 10.0 * (ScriptorConfig.CASTING_LECTERN_COOLDOWN_MULTIPLIER().toDouble() / 100.0)
                    ).toInt()
                }
            }
        }
    }


    companion object {
        const val SPELLBOOK_SLOT: Int = 0
        const val CASTING_FOCUS_SLOT: Int = 1

        fun tick(level: Level, blockEntity: BlockEntity) {
            if (level.isClientSide) return
            if (blockEntity is CastingLecternBlockEntity) blockEntity.tick()
        }
    }
}
