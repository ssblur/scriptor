package com.ssblur.scriptor.word.descriptor.power

import com.ssblur.scriptor.api.word.Descriptor

class SimpleStrengthDescriptor(cost: Int, var strength: Double) : Descriptor(), StrengthDescriptor {
    var cost: Cost = Cost(cost.toDouble(), COSTTYPE.ADDITIVE)
    var allowDuplication: Boolean = false

    fun allowDuplication(): SimpleStrengthDescriptor {
        allowDuplication = true
        return this
    }

  override fun cost() = cost
  override fun strengthModifier() = strength
  override fun allowsDuplicates() = allowDuplication
}
