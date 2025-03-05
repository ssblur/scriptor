package com.ssblur.scriptor.neoforge;

import com.ssblur.scriptor.ScriptorMod;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(ScriptorMod.MOD_ID)
public final class ScriptorModNeoForge {
  public ScriptorModNeoForge(@SuppressWarnings("unused") IEventBus bus) {
    ScriptorMod.INSTANCE.init();
    if (FMLEnvironment.dist == Dist.CLIENT) {
      ScriptorMod.INSTANCE.clientInit();
    }
  }
}
