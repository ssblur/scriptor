package com.ssblur.scriptor.fabric;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.events.CoordinateCasterWorldRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class ScriptorModFabricClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    WorldRenderEvents.AFTER_ENTITIES.register(context -> {
      CoordinateCasterWorldRenderer.INSTANCE.render(context.matrixStack());
      Thread.yield();
    });
    ScriptorMod.INSTANCE.clientInit();
  }
}
