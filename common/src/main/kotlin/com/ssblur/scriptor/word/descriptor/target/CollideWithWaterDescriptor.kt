package com.ssblur.scriptor.word.descriptor.target

import com.ssblur.scriptor.api.word.Descriptor

object CollideWithWaterDescriptor: Descriptor() {
    override fun cost(): Cost = Cost.add(0.15)
}