package com.ssblur.scriptor.word.descriptor.discount

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.descriptor.CastDescriptor
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity

class WeakDescriptor : Descriptor(), CastDescriptor {
    override fun cost(): Cost {
        return Cost(0.9, COSTTYPE.MULTIPLICATIVE)
    }

    override fun cannotCast(caster: Targetable?): Boolean {
        if (caster is EntityTargetable && caster.targetEntity is LivingEntity) return !(caster.targetEntity as LivingEntity).hasEffect(MobEffects.WEAKNESS)
        return true
    }
}
