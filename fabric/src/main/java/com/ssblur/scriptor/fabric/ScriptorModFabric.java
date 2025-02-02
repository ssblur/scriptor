package com.ssblur.scriptor.fabric;

import com.ssblur.scriptor.ScriptorMod;
import net.fabricmc.api.ModInitializer;

public class ScriptorModFabric implements ModInitializer {
  @Override
  public void onInitialize() {
    ScriptorMod.INSTANCE.init();
  }
}
