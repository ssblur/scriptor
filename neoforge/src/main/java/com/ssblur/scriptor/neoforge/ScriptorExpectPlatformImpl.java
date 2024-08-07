package com.ssblur.scriptor.neoforge;

import com.ssblur.scriptor.ScriptorExpectPlatform;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ScriptorExpectPlatformImpl {
  /**
   * This is our actual method to {@link ScriptorExpectPlatform#getConfigDirectory()}.
   */
  public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get().resolve("scriptor.json");
    }
}
