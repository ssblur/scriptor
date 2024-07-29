package com.ssblur.scriptor.neoforge;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.blockentity.ScriptorBlockEntities;
import com.ssblur.scriptor.item.ScriptorItems;
import com.ssblur.scriptor.tabs.ScriptorTabs;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

public class ScriptorModClientEvents {
  public static void register(EntityRenderersEvent.RegisterRenderers event) {
    ScriptorBlockEntities.registerRenderers();
  }

  public static void buildCreativeTab(BuildCreativeModeTabContentsEvent event) {
    if(!ScriptorMod.COMMUNITY_MODE && event.getTab() == ScriptorTabs.SCRIPTOR_TAB.get()) {
      ScriptorItems.TOME_TIER1.listen(event::accept);
      ScriptorItems.TOME_TIER2.listen(event::accept);
      ScriptorItems.TOME_TIER3.listen(event::accept);
      ScriptorItems.TOME_TIER4.listen(event::accept);
      ScriptorItems.SCRAP_TIER1.listen(event::accept);
      ScriptorItems.SCRAP_TIER2.listen(event::accept);
      ScriptorItems.SCRAP_TIER3.listen(event::accept);
    }
  }
}
