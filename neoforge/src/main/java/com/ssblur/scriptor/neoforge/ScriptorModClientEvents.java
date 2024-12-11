package com.ssblur.scriptor.neoforge;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.blockentity.ScriptorBlockEntities;
import com.ssblur.scriptor.item.ScriptorItems;
import com.ssblur.scriptor.item.ScriptorTabs;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@SuppressWarnings("unused")
public class ScriptorModClientEvents {
  public static void register(EntityRenderersEvent.RegisterRenderers event) {
    ScriptorBlockEntities.registerRenderers();
  }

  public static void buildCreativeTab(BuildCreativeModeTabContentsEvent event) {
    if(!ScriptorMod.INSTANCE.getCOMMUNITY_MODE() && event.getTab() == ScriptorTabs.INSTANCE.getSCRIPTOR_TAB().get()) {
      event.accept(ScriptorItems.INSTANCE.getTOME_TIER1().get());
      event.accept(ScriptorItems.INSTANCE.getTOME_TIER2().get());
      event.accept(ScriptorItems.INSTANCE.getTOME_TIER3().get());
      event.accept(ScriptorItems.INSTANCE.getTOME_TIER4().get());
      event.accept(ScriptorItems.INSTANCE.getSCRAP_TIER1().get());
      event.accept(ScriptorItems.INSTANCE.getSCRAP_TIER2().get());
      event.accept(ScriptorItems.INSTANCE.getSCRAP_TIER3().get());
    }
  }
}
