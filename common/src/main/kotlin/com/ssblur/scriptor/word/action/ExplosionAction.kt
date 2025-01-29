package com.ssblur.scriptor.word.action

import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.descriptor.power.StrengthDescriptor
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.ExplosionDamageCalculator
import net.minecraft.world.level.Level

class ExplosionAction : Action() {
    internal class ExplosionActionDamageCalculator(val caster: Targetable) : ExplosionDamageCalculator() {
        override fun getEntityDamageAmount(explosion: Explosion, entity: Entity): Float {
            if (caster is EntityTargetable && caster.targetEntity === entity) return super.getEntityDamageAmount(
                explosion,
                entity
            ) * 0.15f
            return super.getEntityDamageAmount(explosion, entity) * 0.3f
        }
    }

    override fun apply(caster: Targetable, targetable: Targetable, descriptors: Array<Descriptor>) {
        if (targetable.level.isClientSide) return
        var strength = 2.0
        for (d in descriptors) {
            if (d is StrengthDescriptor) strength += d.strengthModifier()
        }

        val level = targetable.level as ServerLevel
        val pos = targetable.targetPos

        val power = Math.pow(strength, 0.7).toFloat()

        level.explode(
            null,
            null,
            ExplosionActionDamageCalculator(caster),
            pos.x,
            pos.y + .25,
            pos.z,
            power,
            false,
            Level.ExplosionInteraction.TNT
        )
    }

    override fun cost(): Cost {
        return Cost(16.0, COSTTYPE.ADDITIVE)
    }
}
