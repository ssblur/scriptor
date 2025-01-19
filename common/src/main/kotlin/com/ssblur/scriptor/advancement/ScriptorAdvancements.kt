package com.ssblur.scriptor.advancement

import com.ssblur.scriptor.ScriptorMod.location
import com.ssblur.scriptor.ScriptorMod.registerTrigger
import com.ssblur.unfocused.advancement.GenericTrigger

object ScriptorAdvancements {
    val TOME = registerTrigger("tome_collect") { GenericTrigger(location("tome_collect")) }
    val TOME_1 = registerTrigger("tome1_collect") { GenericTrigger(location("tome1_collect")) }
    val TOME_2 = registerTrigger("tome2_collect") { GenericTrigger(location("tome2_collect")) }
    val TOME_3 = registerTrigger("tome3_collect") { GenericTrigger(location("tome3_collect")) }
    val TOME_4 = registerTrigger("tome4_collect") { GenericTrigger(location("tome4_collect")) }
    val COMPLEX_SPELL = registerTrigger("complex_spell") { GenericTrigger(location("complex_spell")) }
    val FIZZLE = registerTrigger("fizzle") { GenericTrigger(location("fizzle")) }
    val COMMUNITY = registerTrigger("community") { GenericTrigger(location("community")) }

    fun register() {}
}
