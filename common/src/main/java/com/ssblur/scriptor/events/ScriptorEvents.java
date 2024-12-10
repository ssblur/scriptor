package com.ssblur.scriptor.events;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.events.network.ScriptorNetwork;
import com.ssblur.scriptor.events.reloadlisteners.*;
import dev.architectury.event.events.client.ClientRawInputEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.client.ClientTooltipEvent;
import dev.architectury.event.events.common.*;
import dev.architectury.platform.Platform;
import dev.architectury.registry.ReloadListenerRegistry;
import net.fabricmc.api.EnvType;
import net.minecraft.server.packs.PackType;

public class ScriptorEvents {
  public static final String MOD_ID = ScriptorMod.INSTANCE.MOD_ID;

  public static void register() {
    ChatEvent.RECEIVED.register(new SpellChatEvents());
    LootEvent.MODIFY_LOOT_TABLE.register(new AddLootEvent());
    LifecycleEvent.SERVER_LEVEL_LOAD.register(new PreloadDictionary());
    PlayerEvent.PLAYER_JOIN.register(new PlayerJoinedEvent());
    TickEvent.PLAYER_POST.register(new PlayerTickEvent());
    EntityEvent.LIVING_HURT.register(new EntityDamagedEvent());

    ReloadListenerRegistry.register(PackType.SERVER_DATA, TomeReloadListener.INSTANCE);
    ReloadListenerRegistry.register(PackType.SERVER_DATA, ReagentReloadListener.INSTANCE);
    ReloadListenerRegistry.register(PackType.SERVER_DATA, CustomColorReloadListener.INSTANCE);
    ReloadListenerRegistry.register(PackType.SERVER_DATA, ScrapReloadListener.INSTANCE);
    ReloadListenerRegistry.register(PackType.SERVER_DATA, GeneratorReloadListener.INSTANCE);
    ReloadListenerRegistry.register(PackType.SERVER_DATA, GeneratorBindingReloadListener.INSTANCE);
    ReloadListenerRegistry.register(PackType.SERVER_DATA, ArtifactReloadListener.INSTANCE);
    ReloadListenerRegistry.register(PackType.SERVER_DATA, WordReloadListener.INSTANCE);
    ReloadListenerRegistry.register(PackType.SERVER_DATA, EngravingReloadListener.INSTANCE);

    if(Platform.getEnv() == EnvType.CLIENT) {
      ClientRawInputEvent.MOUSE_SCROLLED.register(new ScrollEvent());
      ClientTickEvent.ClientLevel.CLIENT_LEVEL_POST.register(new ClientLevelTickEvent());
      ClientTooltipEvent.ITEM.register(new AddLoreEvent());

      ScriptorEventsExpectPlatform.registerClientEvents();
    }

    ScriptorNetwork.register();
  }
}
