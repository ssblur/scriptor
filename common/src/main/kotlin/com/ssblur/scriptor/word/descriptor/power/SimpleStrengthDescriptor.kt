package com.ssblur.scriptor.word.descriptor.power

import com.ssblur.scriptor.api.word.Descriptor

class SimpleStrengthDescriptor(cost: Int, var strength: Double) : Descriptor(), StrengthDescriptor {
    var cost: Cost = Cost(cost.toDouble(), COSTTYPE.ADDITIVE)
    var allowDuplication: Boolean = false

    override fun cost(): Cost {
        return cost
    }

    override fun strengthModifier(): Double {
        return strength
    }

    fun allowDuplication(): SimpleStrengthDescriptor {
        allowDuplication = true
        return this
    }

    override fun allowsDuplicates(): Boolean {
        return allowDuplication
    }
}
