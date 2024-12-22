package com.ssblur.scriptor.word.descriptor.power

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.effect.ScriptorEffects.MUTE
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.descriptor.CastDescriptor
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity

class OverwhelmingStrengthDescriptor : Descriptor(), CastDescriptor, StrengthDescriptor {
    override fun cost(): Cost {
        return COST
    }

    override fun strengthModifier(): Double {
        return 20.0
    }

    override fun cannotCast(caster: Targetable?): Boolean {
        if (caster is EntityTargetable && caster.targetEntity is LivingEntity) {
            val living = caster.targetEntity as LivingEntity
            living.addEffect(MobEffectInstance(MUTE.ref(), 20 * 60))
            return false
        }
        return true
    }

    companion object {
        var COST: Cost = Cost(0.0, COSTTYPE.ADDITIVE)
    }
}
