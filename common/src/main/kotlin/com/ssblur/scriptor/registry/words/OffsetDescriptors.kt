package com.ssblur.scriptor.registry.words

import com.ssblur.scriptor.registry.words.WordRegistry.register
import com.ssblur.scriptor.word.descriptor.target.OffsetDescriptor

@Suppress("unused")
object OffsetDescriptors {
    val MOVE_RIGHT = register("move_right", OffsetDescriptor().right())
    val COPY_RIGHT = register("copy_right", OffsetDescriptor().duplicate().right())
    val MOVE_LEFT = register("move_left", OffsetDescriptor().left())
    val COPY_LEFT = register("copy_left", OffsetDescriptor().duplicate().left())
    val MOVE_FORWARDS = register("move_forwards", OffsetDescriptor().forward())
    val COPY_FORWARDS = register("copy_forwards", OffsetDescriptor().duplicate().forward())
    val MOVE_BACKWARDS = register("move_backwards", OffsetDescriptor().backwards())
    val COPY_BACKWARDS = register("copy_backwards", OffsetDescriptor().duplicate().backwards())
    val MOVE_UP = register("move_up", OffsetDescriptor().up())
    val COPY_UP = register("copy_up", OffsetDescriptor().duplicate().up())
    val MOVE_DOWN = register("move_down", OffsetDescriptor().down())
    val COPY_DOWN = register("copy_down", OffsetDescriptor().duplicate().down())
}
