package com.ssblur.scriptor.word.descriptor.discount

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.descriptor.CastDescriptor
import net.minecraft.world.entity.LivingEntity

class OnFireDescriptor : Descriptor(), CastDescriptor {
    override fun cost() = Cost(0.7, COSTTYPE.MULTIPLICATIVE)

    override fun cannotCast(caster: Targetable?): Boolean {
        if (caster is EntityTargetable && caster.targetEntity is LivingEntity) return !(caster.targetEntity as LivingEntity).isOnFire()
        return true
    }
}
