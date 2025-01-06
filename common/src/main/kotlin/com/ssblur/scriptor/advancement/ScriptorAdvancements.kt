package com.ssblur.scriptor.advancement

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.ScriptorMod.registerTrigger

object ScriptorAdvancements {
    val TOME = registerTrigger("tome_collect") { GenericScriptorTrigger(ScriptorMod.location("tome_collect")) }
    val TOME_1 = registerTrigger("tome1_collect") { GenericScriptorTrigger(ScriptorMod.location("tome1_collect")) }
    val TOME_2 = registerTrigger("tome2_collect") { GenericScriptorTrigger(ScriptorMod.location("tome2_collect")) }
    val TOME_3 = registerTrigger("tome3_collect") { GenericScriptorTrigger(ScriptorMod.location("tome3_collect")) }
    val TOME_4 = registerTrigger("tome4_collect") { GenericScriptorTrigger(ScriptorMod.location("tome4_collect")) }
    val COMPLEX_SPELL = registerTrigger("complex_spell") { GenericScriptorTrigger(ScriptorMod.location("complex_spell")) }
    val FIZZLE = registerTrigger("fizzle") { GenericScriptorTrigger(ScriptorMod.location("fizzle")) }
    val COMMUNITY= registerTrigger("community") { GenericScriptorTrigger(ScriptorMod.location("community")) }

    fun register() {}
}
