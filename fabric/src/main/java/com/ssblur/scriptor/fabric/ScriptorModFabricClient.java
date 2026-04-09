package com.ssblur.scriptor.fabric;

import com.ssblur.scriptor.ScriptorClient;
import net.fabricmc.api.ClientModInitializer;

public class ScriptorModFabricClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
      ScriptorClient.INSTANCE.clientInit();
  }
}
