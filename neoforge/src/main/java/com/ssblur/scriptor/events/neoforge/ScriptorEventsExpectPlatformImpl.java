package com.ssblur.scriptor.events.neoforge;

import com.ssblur.scriptor.events.CoordinateCasterWorldRenderer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

public class ScriptorEventsExpectPlatformImpl {
  public static void registerClientEvents() {
  }

  @SubscribeEvent
  public static void levelStageRenderers(RenderLevelStageEvent event) {
    if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_CUTOUT_BLOCKS)
      CoordinateCasterWorldRenderer.render(event.getPoseStack());
  }
}
