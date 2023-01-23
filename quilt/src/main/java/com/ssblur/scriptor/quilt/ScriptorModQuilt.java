package com.ssblur.scriptor.quilt;

import com.ssblur.scriptor.fabriclike.ScriptorModFabricLike;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class ScriptorModQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        ScriptorModFabricLike.init();
    }
}
