package com.ssblur.scriptor.neoforge;

import com.ssblur.scriptor.events.CoordinateCasterWorldRenderer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@SuppressWarnings("unused")
public class ScriptorModClientEvents {
  public static void register(EntityRenderersEvent.RegisterRenderers event) {

  }

  @SubscribeEvent
  public static void levelStageRenderers(RenderLevelStageEvent event) {
    if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_CUTOUT_BLOCKS)
      CoordinateCasterWorldRenderer.INSTANCE.render(event.getPoseStack());
  }
}
