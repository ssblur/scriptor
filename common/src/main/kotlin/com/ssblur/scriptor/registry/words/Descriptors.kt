package com.ssblur.scriptor.registry.words

import com.ssblur.scriptor.registry.words.WordRegistry.register
import com.ssblur.scriptor.word.descriptor.SpeedDurationDescriptor
import com.ssblur.scriptor.word.descriptor.duration.SimpleDurationDescriptor
import com.ssblur.scriptor.word.descriptor.target.ChainDescriptor
import com.ssblur.scriptor.word.descriptor.target.NetherDescriptor

@Suppress("unused")
object Descriptors {
  val LONG = register(
    "long",
    SimpleDurationDescriptor(3, 7.0)
  )
  val LONGER = register(
    "longer",
    SimpleDurationDescriptor(6, 17.0)
  )
  val VERY_LONG = register(
    "very_long",
    SimpleDurationDescriptor(65, 120.0)
  )
  val STACKING_LONG =
    register(
      "stacking_long", SimpleDurationDescriptor(
        6,
        7.0
      ).allowDuplication()
    )
  val SLOW = register(
    "slow",
    SpeedDurationDescriptor(2, 4.0, 0.75)
  )
  val FAST = register(
    "fast",
    SpeedDurationDescriptor(2, -4.0, 1.25)
  )
  val CHAIN = register("chain", ChainDescriptor())

  val NETHER = register("nether", NetherDescriptor())
}
