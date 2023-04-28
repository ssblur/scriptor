package com.ssblur.scriptor.events.forge;

import com.ssblur.scriptor.events.CoordinateCasterWorldRenderer;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
public class ScriptorEventsExpectPlatformImpl {
  public static void registerClientEvents() {
  }

  @SubscribeEvent
  public static void levelStageRenderers(RenderLevelStageEvent event) {
    System.out.println("test");
    if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_CUTOUT_BLOCKS)
      CoordinateCasterWorldRenderer.render(event.getPoseStack());
  }
}
