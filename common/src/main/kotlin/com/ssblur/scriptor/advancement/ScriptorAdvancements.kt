package com.ssblur.scriptor.advancement

import com.ssblur.scriptor.ScriptorMod.registerGenericTrigger

object ScriptorAdvancements {
  val TOME = registerGenericTrigger("tome_collect")
  val TOME_1 = registerGenericTrigger("tome1_collect")
  val TOME_2 = registerGenericTrigger("tome2_collect")
  val TOME_3 = registerGenericTrigger("tome3_collect")
  val TOME_4 = registerGenericTrigger("tome4_collect")
  val COMPLEX_SPELL = registerGenericTrigger("complex_spell")
  val FIZZLE = registerGenericTrigger("fizzle")
  val COMMUNITY = registerGenericTrigger("community")

  fun register() {}
}
