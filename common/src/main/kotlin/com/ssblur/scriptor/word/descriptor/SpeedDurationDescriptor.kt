package com.ssblur.scriptor.word.descriptor

import com.ssblur.scriptor.word.descriptor.duration.SimpleDurationDescriptor

class SpeedDurationDescriptor(cost: Int, duration: Double, var speed: Double) :
    SimpleDurationDescriptor(cost, duration), SpeedDescriptor {
    override fun speedModifier(): Double {
        return speed
    }
}
