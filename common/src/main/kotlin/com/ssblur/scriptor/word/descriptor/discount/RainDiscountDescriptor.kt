package com.ssblur.scriptor.word.descriptor.discount

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.descriptor.CastDescriptor

class RainDiscountDescriptor : Descriptor(), CastDescriptor {
    override fun cost(): Cost {
        return Cost(0.7, COSTTYPE.MULTIPLICATIVE)
    }

    override fun cannotCast(caster: Targetable?): Boolean {
        return caster!!.level.isRainingAt(caster.targetBlockPos)
    }

    override fun allowsDuplicates(): Boolean {
        return false
    }
}
