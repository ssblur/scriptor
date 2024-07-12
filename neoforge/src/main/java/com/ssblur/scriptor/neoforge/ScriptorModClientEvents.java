package com.ssblur.scriptor.neoforge;

import com.ssblur.scriptor.blockentity.ScriptorBlockEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class ScriptorModClientEvents {
  @SubscribeEvent
  public static void register(EntityRenderersEvent.RegisterRenderers event) {
    ScriptorBlockEntities.registerRenderers();
  }
}
