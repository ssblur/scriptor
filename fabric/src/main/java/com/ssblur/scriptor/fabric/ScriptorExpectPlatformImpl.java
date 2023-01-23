package com.ssblur.scriptor.fabric;

import com.ssblur.scriptor.ScriptorExpectPlatform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class ScriptorExpectPlatformImpl {
    /**
     * This is our actual method to {@link ScriptorExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
