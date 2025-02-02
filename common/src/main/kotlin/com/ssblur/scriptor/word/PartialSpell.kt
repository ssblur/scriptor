package com.ssblur.scriptor.word

import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor

class PartialSpell(val action: Action, vararg val descriptors: Descriptor) {
  fun deduplicatedDescriptors(): Array<Descriptor> {
    val out = ArrayList<Descriptor>()
    for (descriptor in descriptors) {
      if (descriptor.allowsDuplicates() || !out.contains(descriptor)) out.add(descriptor)
    }
    return out.toTypedArray()
  }
}
