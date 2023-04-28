package com.ssblur.scriptor.events.fabric;

import com.ssblur.scriptor.events.CoordinateCasterWorldRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class ScriptorEventsExpectPlatformImpl {
  public static void registerClientEvents() {
    WorldRenderEvents.BEFORE_ENTITIES.register(context -> {
      Thread.yield();
      CoordinateCasterWorldRenderer.render(context.matrixStack());
    });
  }
}
