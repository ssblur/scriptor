package com.ssblur.scriptor.word.subject

import com.ssblur.scriptor.api.word.Subject
import com.ssblur.scriptor.events.network.server.ServerTraceNetwork
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.LecternTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.Spell
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import java.util.concurrent.CompletableFuture

class TouchSubject : Subject() {
    override fun getTargets(caster: Targetable, spell: Spell): CompletableFuture<List<Targetable>> {
        val result = CompletableFuture<List<Targetable>>()
        if (caster is EntityTargetable && caster.targetEntity is Player) {
            ServerTraceNetwork.requestTraceData(caster.targetEntity as Player) { target: Targetable -> result.complete(listOf(target)) }
        } else if (caster is LecternTargetable) {
            val pos = caster.targetBlockPos.relative(caster.facing)
            val entities = caster.level.getEntitiesOfClass(
                LivingEntity::class.java,
                AABB.ofSize(
                    Vec3(
                        pos.x.toDouble(),
                        pos.y.toDouble(),
                        pos.z.toDouble()
                    ),
                    1.0,
                    1.0,
                    1.0
                )
            )

            if (entities.isEmpty()) result.complete(listOf(Targetable(caster.level, pos)))
            else result.complete(entities.stream().map { EntityTargetable(it) }.toList())
        } else {
            result.complete(java.util.List.of(caster.simpleCopy()))
        }
        return result
    }

    override fun cost(): Cost {
        return Cost(1.0, COSTTYPE.ADDITIVE)
    }
}
