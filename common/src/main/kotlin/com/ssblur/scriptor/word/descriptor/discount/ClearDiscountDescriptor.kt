package com.ssblur.scriptor.word.descriptor.discount

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.descriptor.CastDescriptor

class ClearDiscountDescriptor: Descriptor(), CastDescriptor {
  override fun cost() = Cost(0.9, COSTTYPE.MULTIPLICATIVE)
  override fun cannotCast(caster: Targetable?) = caster!!.level.isRainingAt(caster.targetBlockPos)
  override fun allowsDuplicates() = false
}
