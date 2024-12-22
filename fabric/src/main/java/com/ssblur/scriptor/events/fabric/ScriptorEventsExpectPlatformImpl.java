package com.ssblur.scriptor.events.fabric;

import com.ssblur.scriptor.events.CoordinateCasterWorldRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class ScriptorEventsExpectPlatformImpl {
  public static void registerClientEvents() {
    WorldRenderEvents.AFTER_ENTITIES.register(context -> {
      CoordinateCasterWorldRenderer.render(context.matrixStack());
      Thread.yield();
    });
  }
}
