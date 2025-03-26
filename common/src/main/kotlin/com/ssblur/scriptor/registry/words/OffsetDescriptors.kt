package com.ssblur.scriptor.registry.words

import com.ssblur.scriptor.registry.words.WordRegistry.register
import com.ssblur.scriptor.word.descriptor.target.OffsetDescriptor

@Suppress("unused")
object OffsetDescriptors {
  val MOVE_RIGHT = register("move_right", OffsetDescriptor(1.05).right())
  val COPY_RIGHT = register("copy_right", OffsetDescriptor(1.25).duplicate().right())
  val MOVE_LEFT = register("move_left", OffsetDescriptor(1.05).left())
  val COPY_LEFT = register("copy_left", OffsetDescriptor(1.25).duplicate().left())
  val MOVE_FORWARDS = register("move_forwards", OffsetDescriptor(1.05).forward())
  val COPY_FORWARDS = register("copy_forwards", OffsetDescriptor(1.25).duplicate().forward())
  val MOVE_BACKWARDS = register("move_backwards", OffsetDescriptor(1.05).backwards())
  val COPY_BACKWARDS = register("copy_backwards", OffsetDescriptor(1.25).duplicate().backwards())
  val MOVE_UP = register("move_up", OffsetDescriptor(1.05).up())
  val COPY_UP = register("copy_up", OffsetDescriptor(1.25).duplicate().up())
  val MOVE_DOWN = register("move_down", OffsetDescriptor(1.05).down())
  val COPY_DOWN = register("copy_down", OffsetDescriptor(1.25).duplicate().down())
}
