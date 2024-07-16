package com.ssblur.scriptor.advancement;

import com.ssblur.scriptor.ScriptorMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;

public class ScriptorAdvancements {
  public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(ScriptorMod.MOD_ID, Registries.TRIGGER_TYPE);


  public static final RegistrySupplier<GenericScriptorTrigger> TOME = TRIGGERS.register(
    "tome_collect",
    () -> new GenericScriptorTrigger(ScriptorMod.location("tome_collect"))
  );
  public static final RegistrySupplier<GenericScriptorTrigger> TOME_1 = TRIGGERS.register(
    "tome1_collect",
    () -> new GenericScriptorTrigger(ScriptorMod.location("tome1_collect"))
  );
  public static final RegistrySupplier<GenericScriptorTrigger> TOME_2 = TRIGGERS.register(
    "tome2_collect",
    () -> new GenericScriptorTrigger(ScriptorMod.location("tome2_collect"))
  );
  public static final RegistrySupplier<GenericScriptorTrigger> TOME_3 = TRIGGERS.register(
    "tome3_collect",
    () -> new GenericScriptorTrigger(ScriptorMod.location("tome3_collect"))
  );
  public static final RegistrySupplier<GenericScriptorTrigger> TOME_4 = TRIGGERS.register(
    "tome4_collect",
    () -> new GenericScriptorTrigger(ScriptorMod.location("tome4_collect"))
  );
  public static final RegistrySupplier<GenericScriptorTrigger> COMPLEX_SPELL = TRIGGERS.register(
    "complex_spell",
    () -> new GenericScriptorTrigger(ScriptorMod.location("complex_spell"))
  );
  public static final RegistrySupplier<GenericScriptorTrigger> FIZZLE =  TRIGGERS.register(
    "fizzle",
    () -> new GenericScriptorTrigger(ScriptorMod.location("fizzle"))
  );
  public static final RegistrySupplier<GenericScriptorTrigger> COMMUNITY = TRIGGERS.register(
    "community",
    () -> new GenericScriptorTrigger(ScriptorMod.location("community"))
  );

  public static void register() {
    TRIGGERS.register();
  }
}
