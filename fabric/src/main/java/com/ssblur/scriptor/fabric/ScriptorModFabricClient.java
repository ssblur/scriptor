package com.ssblur.scriptor.fabric;

import com.ssblur.scriptor.ScriptorMod;
import net.fabricmc.api.ClientModInitializer;

public class ScriptorModFabricClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    ScriptorMod.INSTANCE.clientInit();
  }
}
