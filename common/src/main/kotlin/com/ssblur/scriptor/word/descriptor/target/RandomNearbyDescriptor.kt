package com.ssblur.scriptor.word.descriptor.target

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.Targetable

class RandomNearbyDescriptor(): Descriptor(), TargetDescriptor {
  override fun modifyTargets(originalTargetables: List<Targetable>, owner: Targetable): List<Targetable> {
    val targets = originalTargetables.toMutableList()
    val first = originalTargetables.first()
    targets.add(
      Targetable(first.level, first.targetPos.offsetRandom(first.level.random, 2.0f))
    )
    return targets
  }

  override fun cost() = Cost(1.75, COSTTYPE.MULTIPLICATIVE)
  override fun replacesSubjectCost() = false
  override fun allowsDuplicates() = true
}
