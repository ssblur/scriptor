package com.ssblur.scriptor.events.forge;

import com.ssblur.scriptor.events.CoordinateCasterWorldRenderer;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ScriptorEventsExpectPlatformImpl {
  public static void registerClientEvents() {
    FMLJavaModLoadingContext.get().getModEventBus().addListener(ScriptorEventsExpectPlatformImpl::levelStageRenderers);
  }

  public static void levelStageRenderers(RenderLevelStageEvent event) {
    if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_CUTOUT_BLOCKS)
      CoordinateCasterWorldRenderer.render(event.getPoseStack());
  }
}
