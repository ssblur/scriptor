package com.ssblur.scriptor.word.descriptor.target

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.Targetable

object CasterDescriptor: Descriptor(), TargetDescriptor {
  override fun modifyTargets(originalTargetables: List<Targetable>, owner: Targetable): List<Targetable> {
    return listOf(owner)
  }

  override fun cost() = Cost(1.0, COSTTYPE.MULTIPLICATIVE)
  override fun replacesSubjectCost() = true
  override fun allowsDuplicates() = true
}
