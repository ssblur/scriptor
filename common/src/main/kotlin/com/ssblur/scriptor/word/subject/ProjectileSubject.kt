package com.ssblur.scriptor.word.subject

import com.ssblur.scriptor.api.word.Subject
import com.ssblur.scriptor.color.CustomColors.getColor
import com.ssblur.scriptor.entity.ScriptorEntities
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.Spell
import com.ssblur.scriptor.word.descriptor.SpeedDescriptor
import com.ssblur.scriptor.word.descriptor.duration.DurationDescriptor
import net.minecraft.world.phys.Vec3
import java.util.concurrent.CompletableFuture

class ProjectileSubject : Subject() {
    override fun cost(): Cost {
        return Cost(3.0, COSTTYPE.ADDITIVE)
    }

    override fun getTargets(caster: Targetable, spell: Spell): CompletableFuture<List<Targetable>> {
        val future = CompletableFuture<List<Targetable>>()

        val color = getColor(spell.deduplicatedDescriptorsForSubjects())
        var duration = 12.0
        var speed = 1.0
        for (d in spell.deduplicatedDescriptorsForSubjects()) {
            if (d is DurationDescriptor) duration += d.durationModifier()
            if (d is SpeedDescriptor) speed *= d.speedModifier()
        }
        speed *= 0.8

        val projectile = checkNotNull(ScriptorEntities.PROJECTILE_TYPE.get().create(caster.level))
        if (caster is EntityTargetable) {
            val entity = caster.targetEntity
            projectile.setPos(entity.eyePosition)
            projectile.deltaMovement = entity.lookAngle.normalize().scale(speed)
            projectile.setOwner(entity)
        } else {
            projectile.setPos(caster.targetPos)
            val normal = caster.facing.normal
            projectile.deltaMovement = Vec3(normal.x.toDouble(), normal.y.toDouble(), normal.z.toDouble()).scale(speed)
        }
        projectile.setOrigin(caster.origin)
        projectile.setDuration(Math.round(10 * duration).toInt())
        projectile.color = color
        projectile.completable = future
        caster.level.addFreshEntity(projectile)

        return future
    }
}
