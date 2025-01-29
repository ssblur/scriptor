package com.ssblur.scriptor.word.descriptor.power

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.descriptor.CastDescriptor

class SolarPowerDescriptor : Descriptor(), CastDescriptor, StrengthDescriptor {
    override fun cost(): Cost {
        return Cost(0.0, COSTTYPE.ADDITIVE_POST)
    }

    override fun cannotCast(caster: Targetable?): Boolean {
        return !caster!!.level.isDay
    }

    override fun strengthModifier(): Double {
        return 1.5
    }

    override fun allowsDuplicates(): Boolean {
        return false
    }
}
