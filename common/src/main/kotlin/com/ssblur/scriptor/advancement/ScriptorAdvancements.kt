package com.ssblur.scriptor.advancement

import com.ssblur.scriptor.ScriptorMod
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.advancements.CriterionTrigger
import net.minecraft.core.registries.Registries

object ScriptorAdvancements {
    val TRIGGERS: DeferredRegister<CriterionTrigger<*>> =
        DeferredRegister.create(ScriptorMod.MOD_ID, Registries.TRIGGER_TYPE)


    @JvmField
    val TOME: RegistrySupplier<GenericScriptorTrigger> = TRIGGERS.register(
        "tome_collect"
    ) { GenericScriptorTrigger(ScriptorMod.location("tome_collect")) }
    @JvmField
    val TOME_1: RegistrySupplier<GenericScriptorTrigger> = TRIGGERS.register(
        "tome1_collect"
    ) { GenericScriptorTrigger(ScriptorMod.location("tome1_collect")) }
    @JvmField
    val TOME_2: RegistrySupplier<GenericScriptorTrigger> = TRIGGERS.register(
        "tome2_collect"
    ) { GenericScriptorTrigger(ScriptorMod.location("tome2_collect")) }
    @JvmField
    val TOME_3: RegistrySupplier<GenericScriptorTrigger> = TRIGGERS.register(
        "tome3_collect"
    ) { GenericScriptorTrigger(ScriptorMod.location("tome3_collect")) }
    @JvmField
    val TOME_4: RegistrySupplier<GenericScriptorTrigger> = TRIGGERS.register(
        "tome4_collect"
    ) { GenericScriptorTrigger(ScriptorMod.location("tome4_collect")) }
    @JvmField
    val COMPLEX_SPELL: RegistrySupplier<GenericScriptorTrigger> = TRIGGERS.register(
        "complex_spell"
    ) { GenericScriptorTrigger(ScriptorMod.location("complex_spell")) }
    @JvmField
    val FIZZLE: RegistrySupplier<GenericScriptorTrigger> = TRIGGERS.register(
        "fizzle"
    ) { GenericScriptorTrigger(ScriptorMod.location("fizzle")) }
    @JvmField
    val COMMUNITY: RegistrySupplier<GenericScriptorTrigger> = TRIGGERS.register(
        "community"
    ) { GenericScriptorTrigger(ScriptorMod.location("community")) }

    fun register() {
        TRIGGERS.register()
    }
}
