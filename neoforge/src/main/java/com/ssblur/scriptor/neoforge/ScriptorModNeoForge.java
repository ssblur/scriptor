package com.ssblur.scriptor.neoforge;

import com.ssblur.scriptor.ScriptorMod;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;

@Mod(ScriptorMod.MOD_ID)
public final class ScriptorModNeoForge {
  public ScriptorModNeoForge(IEventBus bus) {
    bus.addListener(ScriptorModClientEvents::register);
    ScriptorMod.INSTANCE.init();
    if (FMLEnvironment.dist == Dist.CLIENT) {
      ScriptorMod.INSTANCE.clientInit();
      NeoForge.EVENT_BUS.register(ScriptorModClientEvents.class);
    }
  }
}
