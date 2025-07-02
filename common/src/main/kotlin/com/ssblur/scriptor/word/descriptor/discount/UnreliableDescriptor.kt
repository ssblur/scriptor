package com.ssblur.scriptor.word.descriptor.discount

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.descriptor.CastDescriptor

class UnreliableDescriptor: Descriptor(), CastDescriptor {
  override fun cost() = Cost(0.3, COSTTYPE.MULTIPLICATIVE)

  override fun cannotCast(caster: Targetable?): Boolean {
    return caster?.level?.random?.nextBoolean() ?: false
  }

  override fun allowsDuplicates() = true
}
