package com.ssblur.scriptor.events;

import dev.architectury.event.events.client.ClientTooltipEvent;
import dev.architectury.event.events.common.LootEvent;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;

public class ScriptorEvents {
  public static void register() {
    LootEvent.MODIFY_LOOT_TABLE.register(new AddLootEvent());

    if(Platform.getEnv() == EnvType.CLIENT) {
      ClientTooltipEvent.ITEM.register(new AddLoreEvent());
    }
  }
}
