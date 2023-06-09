package com.ssblur.scriptor.effect;

import com.ssblur.scriptor.ScriptorMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;

public class ScriptorEffects {
  public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ScriptorMod.MOD_ID, Registries.MOB_EFFECT);
  public static final RegistrySupplier<MobEffect> HOARSE = EFFECTS.register("hoarse", MuteStatusEffect::new);
  public static final RegistrySupplier<MobEffect> MUTE = EFFECTS.register("mute", MuteStatusEffect::new);

  public static void register() {
    EFFECTS.register();
  }
}
