package com.ssblur.scriptor.word.descriptor.duration

import com.ssblur.scriptor.api.word.Descriptor

open class SimpleDurationDescriptor(cost: Int, var duration: Double): Descriptor(), DurationDescriptor {
  var cost: Cost = Cost(cost.toDouble(), COSTTYPE.ADDITIVE)
  var duplicates: Boolean = false

  override fun cost() = cost
  override fun durationModifier() = duration
  override fun allowsDuplicates() = duplicates

  fun allowDuplication(): SimpleDurationDescriptor {
    this.duplicates = true
    return this
  }
}
