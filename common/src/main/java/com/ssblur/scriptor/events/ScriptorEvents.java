package com.ssblur.scriptor.events;

import com.ssblur.scriptor.ScriptorMod;
import dev.architectury.event.events.client.ClientRawInputEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.client.ClientTooltipEvent;
import dev.architectury.event.events.common.*;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;

public class ScriptorEvents {
  public static final String MOD_ID = ScriptorMod.MOD_ID;

  public static void register() {
    ChatEvent.RECEIVED.register(new SpellChatEvents());
    LootEvent.MODIFY_LOOT_TABLE.register(new AddLootEvent());
    LifecycleEvent.SERVER_LEVEL_LOAD.register(new PreloadDictionary());
    PlayerEvent.PLAYER_JOIN.register(new PlayerJoinedEvent());
    TickEvent.PLAYER_POST.register(new PlayerTickEvent());
    EntityEvent.LIVING_HURT.register(new EntityDamagedEvent());

    if(Platform.getEnv() == EnvType.CLIENT) {
      ClientRawInputEvent.MOUSE_SCROLLED.register(new ScrollEvent());
      ClientTickEvent.ClientLevel.CLIENT_LEVEL_POST.register(new ClientLevelTickEvent());
      ClientTooltipEvent.ITEM.register(new AddLoreEvent());

      ScriptorEventsExpectPlatform.registerClientEvents();
    }
  }
}
