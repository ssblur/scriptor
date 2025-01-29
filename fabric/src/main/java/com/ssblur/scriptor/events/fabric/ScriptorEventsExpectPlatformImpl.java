package com.ssblur.scriptor.events.fabric;

import com.ssblur.scriptor.events.CoordinateCasterWorldRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

@SuppressWarnings("unused")
public class ScriptorEventsExpectPlatformImpl {
  public static void registerClientEvents() {
    WorldRenderEvents.AFTER_ENTITIES.register(context -> {
      CoordinateCasterWorldRenderer.INSTANCE.render(context.matrixStack());
      Thread.yield();
    });
  }
}
