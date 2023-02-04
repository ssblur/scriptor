package com.ssblur.scriptor.forge;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.blockentity.ScriptorBlockEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ScriptorModClientEvents {
  @SubscribeEvent
  public static void register(EntityRenderersEvent.RegisterRenderers event) {
    ScriptorBlockEntities.registerRenderers();
    System.out.println("registering renderers");
  }
}
