package com.ssblur.scriptor.neoforge;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.events.neoforge.ScriptorEventsExpectPlatformImpl;
import dev.architectury.platform.Platform;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(ScriptorMod.MOD_ID)
public final class ScriptorModNeoForge {
    public ScriptorModNeoForge(IEventBus bus) {
      bus.addListener(ScriptorModClientEvents::register);
      ScriptorMod.init();
      if(Platform.getEnv() == Dist.CLIENT)
        NeoForge.EVENT_BUS.register(ScriptorEventsExpectPlatformImpl.class);
    }
}
