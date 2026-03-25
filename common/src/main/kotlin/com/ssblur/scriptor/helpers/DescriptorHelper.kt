package com.ssblur.scriptor.helpers

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.word.descriptor.power.StrengthDescriptor

object DescriptorHelper {
  fun Array<Descriptor>.strength() = this.sumOf { if(it is StrengthDescriptor) it.strengthModifier() else 0.0 }
}