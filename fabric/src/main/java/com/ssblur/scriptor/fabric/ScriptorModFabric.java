package com.ssblur.scriptor.fabric;

import com.ssblur.scriptor.fabriclike.ScriptorModFabricLike;
import net.fabricmc.api.ModInitializer;

public class ScriptorModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ScriptorModFabricLike.init();
    }
}
