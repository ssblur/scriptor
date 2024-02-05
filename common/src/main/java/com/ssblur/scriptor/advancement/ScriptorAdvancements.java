package com.ssblur.scriptor.advancement;

import com.ssblur.scriptor.ScriptorMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

public class ScriptorAdvancements {
  public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(ScriptorMod.MOD_ID, Registries.TRIGGER_TYPE);


  public static final RegistrySupplier<GenericScriptorTrigger> TOME = TRIGGERS.register(
    "tome_collect",
    () -> new GenericScriptorTrigger(new ResourceLocation(ScriptorMod.MOD_ID, "tome_collect"))
  );
  public static final RegistrySupplier<GenericScriptorTrigger> TOME_1 = TRIGGERS.register(
    "tome1_collect",
    () -> new GenericScriptorTrigger(new ResourceLocation(ScriptorMod.MOD_ID, "tome1_collect"))
  );
  public static final RegistrySupplier<GenericScriptorTrigger> TOME_2 = TRIGGERS.register(
    "tome2_collect",
    () -> new GenericScriptorTrigger(new ResourceLocation(ScriptorMod.MOD_ID, "tome2_collect"))
  );
  public static final RegistrySupplier<GenericScriptorTrigger> TOME_3 = TRIGGERS.register(
    "tome3_collect",
    () -> new GenericScriptorTrigger(new ResourceLocation(ScriptorMod.MOD_ID, "tome3_collect"))
  );
  public static final RegistrySupplier<GenericScriptorTrigger> TOME_4 = TRIGGERS.register(
    "tome4_collect",
    () -> new GenericScriptorTrigger(new ResourceLocation(ScriptorMod.MOD_ID, "tome4_collect"))
  );
  public static final RegistrySupplier<GenericScriptorTrigger> COMPLEX_SPELL = TRIGGERS.register(
    "complex_spell",
    () -> new GenericScriptorTrigger(new ResourceLocation(ScriptorMod.MOD_ID, "complex_spell"))
  );
  public static final RegistrySupplier<GenericScriptorTrigger> FIZZLE =  TRIGGERS.register(
    "fizzle",
    () -> new GenericScriptorTrigger(new ResourceLocation(ScriptorMod.MOD_ID, "fizzle"))
  );
  public static final RegistrySupplier<GenericScriptorTrigger> COMMUNITY = TRIGGERS.register(
    "community",
    () -> new GenericScriptorTrigger(new ResourceLocation(ScriptorMod.MOD_ID, "community"))
  );

  public static void register() {
    TRIGGERS.register();
  }
}
