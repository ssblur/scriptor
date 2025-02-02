package com.ssblur.scriptor.word.descriptor.discount

import com.ssblur.scriptor.api.word.Descriptor

class CheapDescriptor: Descriptor() {
  override fun cost() = Cost(0.5, COSTTYPE.MULTIPLICATIVE)
}
