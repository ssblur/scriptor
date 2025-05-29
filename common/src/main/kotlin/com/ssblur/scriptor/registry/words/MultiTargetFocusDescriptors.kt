package com.ssblur.scriptor.registry.words

import com.ssblur.scriptor.registry.words.WordRegistry.register
import com.ssblur.scriptor.word.descriptor.focus.entity.BlockOnlyDescriptor
import com.ssblur.scriptor.word.descriptor.focus.entity.EntityOnlyDescriptor

@Suppress("unused")
object MultiTargetFocusDescriptors {
  val ENTITY_ONLY = register(
    "entity_only",
    EntityOnlyDescriptor()
  )
  val BLOCK_ONLY = register(
    "block_only",
    BlockOnlyDescriptor()
  )
}
