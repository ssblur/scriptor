package com.ssblur.scriptor.word.descriptor.discount

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.descriptor.CastDescriptor
import net.minecraft.world.entity.LivingEntity

class HealthyDescriptor : Descriptor(), CastDescriptor {
    override fun cost() = Cost(0.8, COSTTYPE.MULTIPLICATIVE)

    override fun cannotCast(caster: Targetable?): Boolean {
        if (caster is EntityTargetable && caster.targetEntity is LivingEntity) {
            val living = caster.targetEntity as LivingEntity
            return living.getHealth() < living.getMaxHealth()
        }
        return true
    }
}
