package com.ssblur.scriptor.word.subject

import com.ssblur.scriptor.api.word.Subject
import com.ssblur.scriptor.color.CustomColors.getColor
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.LecternTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.network.client.ParticleNetwork
import com.ssblur.scriptor.network.server.TraceNetwork
import com.ssblur.scriptor.word.Spell
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import java.util.concurrent.CompletableFuture

class HitscanSubject : Subject() {
    override fun getTargets(caster: Targetable, spell: Spell): CompletableFuture<List<Targetable>> {
        val result = CompletableFuture<List<Targetable>>()
        if (caster is EntityTargetable && caster.targetEntity is Player) {
            val player = caster.targetEntity as Player
            TraceNetwork.requestExtendedTraceData(player) { target: Targetable ->
                val color = getColor(spell.deduplicatedDescriptorsForSubjects())
                ParticleNetwork.magicTrail(target.level, color, player.eyePosition, target.targetPos)
                result.complete(listOf(target))
            }
        } else if (caster is LecternTargetable) {
            val pos = caster.targetBlockPos.relative(caster.facing)
            val normal = caster.facing.normal
            var x = normal.x * 19
            var z = normal.z * 19
            val entities = caster.level.getEntitiesOfClass(
                LivingEntity::class.java,
                AABB.ofSize(
                    Vec3(
                        pos.x.toDouble(),
                        pos.y.toDouble(),
                        pos.z.toDouble()
                    ),
                    (1 + x).toDouble(),
                    1.0,
                    (1 + z).toDouble()
                )
            )

            if (entities.isEmpty()) {
                var i = 0
                x = caster.facing.stepX
                z = caster.facing.stepZ
                while (i < 19) {
                    if (!caster.level.getBlockState(pos.offset(x * i, 0, z * i)).isAir) {
                        result.complete(listOf(Targetable(caster.level, pos.offset(x * i, 0, z * i))))
                        return result
                    }
                    i++
                }
            } else result.complete(entities.stream().map { EntityTargetable(it) }.toList())
        } else {
            result.complete(listOf(caster.simpleCopy()))
        }
        return result
    }

    override fun cost(): Cost {
        return Cost(8.0, COSTTYPE.ADDITIVE)
    }
}
