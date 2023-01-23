package com.ssblur.scriptor.fabric;

import com.ssblur.scriptor.ScriptorExpectPlatform;
import org.quiltmc.loader.api.QuiltLoader;

import java.nio.file.Path;

public class ScriptorExpectPlatformImpl {
    /**
     * This is our actual method to {@link ScriptorExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return QuiltLoader.getConfigDir();
    }
}
