package com.ssblur.scriptor.blockentity

import com.ssblur.scriptor.color.interfaces.Colorable
import com.ssblur.scriptor.data.saved_data.DictionarySavedData
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.Spell
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.Vec3i
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.structure.BoundingBox
import net.minecraft.world.phys.AABB
import java.util.*
import java.util.concurrent.CompletableFuture

class RuneBlockEntity(blockPos: BlockPos, blockState: BlockState) :
    BlockEntity(ScriptorBlockEntities.RUNE.get(), blockPos, blockState), Colorable {
    var owner: Entity? = null
    var ownerUUID: UUID? = null
    var spell: Spell? = null // Keep the Spell for Serialization and to rebuild future if reloaded.
    var spellText: String? = null
    var future: CompletableFuture<List<Targetable>>? = null
    var runeColor: Int = 0
    var unloadedSpell: Boolean = true

    override fun getUpdatePacket() = ClientboundBlockEntityDataPacket.create(this)

    override fun getUpdateTag(provider: HolderLookup.Provider): CompoundTag {
        val tag = super.getUpdateTag(provider)
        tag.putInt("scriptor:color", runeColor)
        return tag
    }

    public override fun loadAdditional(tag: CompoundTag, provider: HolderLookup.Provider) {
        super.loadAdditional(tag, provider)
        runeColor = tag.getInt("scriptor:color")

        if (tag.contains("spell")) spellText = tag.getString("spell")
        else unloadedSpell = false

        if (tag.contains("owner")) ownerUUID = UUID.fromString(tag.getString("owner"))

        setChanged()
    }

    override fun saveAdditional(tag: CompoundTag, provider: HolderLookup.Provider) {
        super.saveAdditional(tag, provider)

        if (level is ServerLevel && spell != null)
            tag.putString("spell", DictionarySavedData.computeIfAbsent(level as ServerLevel).generate(spell!!))
        if (owner != null) tag.putString("owner", owner!!.stringUUID)
        else if (ownerUUID != null) tag.putString("owner", ownerUUID.toString())
        tag.putInt("scriptor:color", runeColor)
    }

    fun tick() {
        if (level == null || level!!.isClientSide) return

        if (level!!.gameTime % 40 == 0L && owner == null)
            if (ownerUUID != null && level!!.getPlayerByUUID(ownerUUID!!) != null) {
                owner = level!!.getPlayerByUUID(ownerUUID!!)
                // If the owner is online after this is reloaded, reassign ownership.
                val spell = DictionarySavedData.computeIfAbsent(level as ServerLevel).parse(spellText)
                if (spell != null) {
                    future = if (owner != null) spell.createFuture(EntityTargetable(owner!!))
                    else spell.createFuture(Targetable(this.level!!, this.blockPos))
                }
            }

        if (future == null || future!!.isDone) if (spell != null) {
            future = if (owner != null && owner!!.isAlive) spell!!.createFuture(EntityTargetable(owner!!))
            else spell!!.createFuture(
                Targetable(this.level!!, this.blockPos)
            )
        } else if (spellText != null) {
            spell = DictionarySavedData.computeIfAbsent(level as ServerLevel).parse(spellText)
            if (spell != null) future = if (owner == null) {
                spell!!.createFuture(
                    Targetable(level!!, worldPosition)
                )
            } else {
                spell!!.createFuture(EntityTargetable(owner!!))
            }
        }

        val xMin = if (worldPosition.x >= 0) 0.2 else -0.8
        val zMin = if (worldPosition.z >= 0) 0.2 else -0.8
        val xMax = if (worldPosition.x >= 0) 0.6 else -0.4
        val zMax = if (worldPosition.z >= 0) 0.6 else -0.4
        val box = AABB.of(
            BoundingBox.fromCorners(
                Vec3i(
                    (worldPosition.x + xMin).toInt(),
                    (worldPosition.y + 0.0).toInt(),
                    (worldPosition.z + zMin).toInt()
                ),
                Vec3i(
                    (worldPosition.x + xMax).toInt(),
                    (worldPosition.y + 0.0625).toInt(),
                    (worldPosition.z + zMax).toInt()
                )
            )
        )
        val entities = level!!.getEntities(null, box)
        if (entities.size > 0) {
            val targets: MutableList<Targetable> = ArrayList()
            for (entity in entities) targets.add(
                EntityTargetable(
                    entity
                )
            )
            if (future != null) future!!.complete(targets)
            level!!.removeBlock(worldPosition, true)
        }
    }

    override fun setColor(color: Int) {
        this.runeColor = color
        setChanged()
        if (level != null) level!!.sendBlockUpdated(blockPos, blockState, blockState, 2)
    }

    companion object {
        fun <T : BlockEntity?> tick(level: Level, entity: T) {
            if (level.isClientSide) return
            if (entity is RuneBlockEntity) entity.tick()
        }
    }
}
