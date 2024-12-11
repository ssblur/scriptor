package com.ssblur.scriptor.word.descriptor.duration

import com.ssblur.scriptor.api.word.Descriptor

open class SimpleDurationDescriptor(cost: Int, var duration: Double) : Descriptor(), DurationDescriptor {
    var cost: Cost = Cost(cost.toDouble(), COSTTYPE.ADDITIVE)
    var duplicates: Boolean = false

    override fun cost(): Cost {
        return cost
    }

    override fun durationModifier(): Double {
        return duration
    }

    override fun allowsDuplicates(): Boolean {
        return duplicates
    }

    fun allowDuplication(): SimpleDurationDescriptor {
        this.duplicates = true
        return this
    }
}
