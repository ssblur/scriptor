package com.ssblur.scriptor.forge;

import com.ssblur.scriptor.ScriptorMod;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ScriptorMod.MOD_ID)
public class ScriptorModForge {
    public ScriptorModForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(ScriptorMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        ScriptorMod.init();
    }
}
