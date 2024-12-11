package com.ssblur.scriptor.word.subject

import com.ssblur.scriptor.api.word.Subject
import com.ssblur.scriptor.block.ScriptorBlocks
import com.ssblur.scriptor.blockentity.RuneBlockEntity
import com.ssblur.scriptor.color.CustomColors.getColor
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.network.server.TraceNetwork
import com.ssblur.scriptor.word.Spell
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import java.util.concurrent.CompletableFuture

class RuneSubject : Subject(), InventorySubject {
    override fun getTargets(caster: Targetable, spell: Spell): CompletableFuture<List<Targetable>> {
        val result = CompletableFuture<List<Targetable>>()
        if (caster is EntityTargetable && caster.targetEntity is Player) {
            val player = caster.targetEntity as Player
            TraceNetwork.requestTraceData(player) { target: Targetable ->
                val color = getColor(spell.deduplicatedDescriptorsForSubjects())
                val pos = target.targetBlockPos
                val level = caster.level

                if (!level.getBlockState(pos).canBeReplaced()) return@requestTraceData

                level.setBlockAndUpdate(pos, ScriptorBlocks.RUNE.get().defaultBlockState())
                val entity = level.getBlockEntity(pos)
                if (entity is RuneBlockEntity) {
                    entity.owner = player
                    entity.future = result
                    entity.spell = spell
                    entity.color = color
                    entity.setChanged()
                }
            }
        } else {
            val color = getColor(spell.deduplicatedDescriptorsForSubjects())

            val pos = caster.targetBlockPos
            val level = caster.level

            if (!level.getBlockState(pos).canBeReplaced()) {
                result.complete(listOf())
                return result
            }

            level.setBlockAndUpdate(pos, ScriptorBlocks.RUNE.get().defaultBlockState())
            val entity = level.getBlockEntity(pos)
            if (entity is RuneBlockEntity) {
                entity.future = result
                entity.spell = spell
                entity.color = color
                entity.setChanged()
            }
        }
        return result
    }

    override fun cost(): Cost {
        return Cost(1.0, COSTTYPE.ADDITIVE)
    }

    override fun castOnItem(spell: Spell, player: Player, slot: ItemStack) {
        // add a one-time cast
    }
}
