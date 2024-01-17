package com.ssblur.scriptor.events;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.events.network.*;
import com.ssblur.scriptor.events.reloadlisteners.*;
import dev.architectury.event.events.client.ClientRawInputEvent;
import dev.architectury.event.events.common.ChatEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.LootEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.registry.ReloadListenerRegistry;
import net.fabricmc.api.EnvType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

public class ScriptorEvents {
  public static final String MOD_ID = ScriptorMod.MOD_ID;

  public static void register() {
    ChatEvent.RECEIVED.register(new SpellChatEvents());
    LootEvent.MODIFY_LOOT_TABLE.register(new AddLootEvent());
    LifecycleEvent.SERVER_LEVEL_LOAD.register(new PreloadDictionary());
    PlayerEvent.PLAYER_JOIN.register(new PlayerJoinedEvent());

    ReloadListenerRegistry.register(PackType.SERVER_DATA, TomeReloadListener.INSTANCE);
    ReloadListenerRegistry.register(PackType.SERVER_DATA, ReagentReloadListener.INSTANCE);
    ReloadListenerRegistry.register(PackType.SERVER_DATA, CustomColorReloadListener.INSTANCE);
    ReloadListenerRegistry.register(PackType.SERVER_DATA, ScrapReloadListener.INSTANCE);
    ReloadListenerRegistry.register(PackType.SERVER_DATA, GeneratorReloadListener.INSTANCE);
    ReloadListenerRegistry.register(PackType.SERVER_DATA, GeneratorBindingReloadListener.INSTANCE);
    ReloadListenerRegistry.register(PackType.SERVER_DATA, ArtifactReloadListener.INSTANCE);

    if(Platform.getEnv() == EnvType.CLIENT) {
      ClientRawInputEvent.MOUSE_SCROLLED.register(new ScrollEvent());

      ScriptorEventsExpectPlatform.registerClientEvents();
    }

    ScriptorNetwork.register();
  }
}
