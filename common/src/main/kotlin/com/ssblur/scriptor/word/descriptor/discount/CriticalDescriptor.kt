package com.ssblur.scriptor.word.descriptor.discount

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.descriptor.CastDescriptor
import net.minecraft.world.entity.LivingEntity

class CriticalDescriptor : Descriptor(), CastDescriptor {
    override fun cost() = Cost.multiply(0.3)

    override fun cannotCast(caster: Targetable?) =
        !((caster is EntityTargetable && caster.targetEntity is LivingEntity) && (caster.targetEntity as LivingEntity).getHealth() <= 2.0f)
}
