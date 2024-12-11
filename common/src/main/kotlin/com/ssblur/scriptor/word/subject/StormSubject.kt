package com.ssblur.scriptor.word.subject

import com.ssblur.scriptor.api.word.Subject
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.Spell
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import java.util.*
import java.util.concurrent.CompletableFuture

class StormSubject : Subject() {
    override fun cost(): Cost {
        return Cost(8.0, COSTTYPE.MULTIPLICATIVE)
    }

    override fun getTargets(caster: Targetable, spell: Spell): CompletableFuture<List<Targetable>> {
        val targets = ArrayList<Targetable>()
        val random = Random()
        val radius = 4
        val limit = 12
        val center = caster.targetBlockPos
        var pos: BlockPos

        for (i in 0 until limit) {
            pos = center.offset(
                random.nextInt((radius * 2) - radius),
                3,
                random.nextInt((radius * 2) - radius)
            )
            for (j in 0..2) {
                if (caster.level.getBlockState(pos.below()).canBeReplaced()) pos = pos.below()
                else break
            }

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

            if (entities.size > 0) for (entity in entities) targets.add(
                EntityTargetable(
                    entity
                )
            )
            else targets.add(Targetable(caster.level, pos))
        }

        val result = CompletableFuture<List<Targetable>>()
        result.complete(targets)
        return result
    }
}
