package com.ssblur.scriptor.events;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.events.messages.*;
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
  public static final ResourceLocation GET_TRACE_DATA = new ResourceLocation(ScriptorMod.MOD_ID, "get_touch_data");
  public static final ResourceLocation GET_HITSCAN_DATA = new ResourceLocation(ScriptorMod.MOD_ID, "get_hitscan_data");
  public static final ResourceLocation RETURN_TRACE_DATA = new ResourceLocation(ScriptorMod.MOD_ID, "return_touch_data");
  public static final ResourceLocation RECEIVE_CHALK_MESSAGE = new ResourceLocation(ScriptorMod.MOD_ID, "receive_chalk_message");
  public static final ResourceLocation CURSOR_USE_BOOK = new ResourceLocation(ScriptorMod.MOD_ID, "cursor_use_book");
  public static final ResourceLocation CURSOR_USE_BOOKC = new ResourceLocation(ScriptorMod.MOD_ID, "cursor_use_bookc");
  public static final ResourceLocation CURSOR_RETURN_BOOKC = new ResourceLocation(ScriptorMod.MOD_ID, "cursor_return_bookc");
  public static final ResourceLocation CURSOR_USE_SCROLL = new ResourceLocation(ScriptorMod.MOD_ID, "cursor_use_scroll");
  public static final ResourceLocation CURSOR_USE_SCROLLC = new ResourceLocation(ScriptorMod.MOD_ID, "cursor_use_scrollc");
  public static final ResourceLocation CURSOR_RETURN_SCROLLC = new ResourceLocation(ScriptorMod.MOD_ID, "cursor_return_scrollc");
  public static final ResourceLocation COLOR_RECEIVEC = new ResourceLocation(ScriptorMod.MOD_ID, "color_receivec");
  public static final ResourceLocation PARTICLE = new ResourceLocation(ScriptorMod.MOD_ID, "particle");
  public static final ResourceLocation SCROLL_NETWORK_C = new ResourceLocation(ScriptorMod.MOD_ID, "scroll_networkc");

  public static void register() {
    ChatEvent.RECEIVED.register(new SpellChatEvents());
    LootEvent.MODIFY_LOOT_TABLE.register(new AddLootEvent());
    LifecycleEvent.SERVER_LEVEL_LOAD.register(new PreloadDictionary());
    PlayerEvent.PLAYER_JOIN.register(new PlayerJoinedEvent());
    ClientRawInputEvent.MOUSE_SCROLLED.register(new ScrollEvent());
    ReloadListenerRegistry.register(PackType.SERVER_DATA, TomeReloadListener.INSTANCE);
    ReloadListenerRegistry.register(PackType.SERVER_DATA, ReagentReloadListener.INSTANCE);
    ReloadListenerRegistry.register(PackType.SERVER_DATA, CustomColorReloadListener.INSTANCE);
    ReloadListenerRegistry.register(PackType.SERVER_DATA, ScrapReloadListener.INSTANCE);
    ReloadListenerRegistry.register(PackType.SERVER_DATA, GeneratorReloadListener.INSTANCE);
    ReloadListenerRegistry.register(PackType.SERVER_DATA, GeneratorBindingReloadListener.INSTANCE);

    if(Platform.getEnv() == EnvType.CLIENT) {
      NetworkManager.registerReceiver(NetworkManager.Side.S2C, GET_TRACE_DATA, TraceNetwork::getTraceData);
      NetworkManager.registerReceiver(NetworkManager.Side.S2C, GET_HITSCAN_DATA, TraceNetwork::getExtendedTraceData);
      NetworkManager.registerReceiver(NetworkManager.Side.S2C, CURSOR_RETURN_SCROLLC, IdentifyNetwork::receiveDataCreative);
      NetworkManager.registerReceiver(NetworkManager.Side.S2C, CURSOR_RETURN_BOOKC, EnchantNetwork::returnBookCreative);
      NetworkManager.registerReceiver(NetworkManager.Side.S2C, COLOR_RECEIVEC, ColorNetwork::receiveColor);
      NetworkManager.registerReceiver(NetworkManager.Side.S2C, PARTICLE, ParticleNetwork::getParticles);
      ScriptorEventsExpectPlatform.registerClientEvents();
    }
    NetworkManager.registerReceiver(NetworkManager.Side.C2S, RETURN_TRACE_DATA, TraceNetwork::returnTraceData);
    NetworkManager.registerReceiver(NetworkManager.Side.C2S, RECEIVE_CHALK_MESSAGE, ChalkNetwork::receiveChalkMessage);
    NetworkManager.registerReceiver(NetworkManager.Side.C2S, CURSOR_USE_BOOK, EnchantNetwork::useBook);
    NetworkManager.registerReceiver(NetworkManager.Side.C2S, CURSOR_USE_BOOKC, EnchantNetwork::useBookCreative);
    NetworkManager.registerReceiver(NetworkManager.Side.C2S, CURSOR_USE_SCROLL, IdentifyNetwork::useScroll);
    NetworkManager.registerReceiver(NetworkManager.Side.C2S, CURSOR_USE_SCROLLC, IdentifyNetwork::useScrollCreative);
    NetworkManager.registerReceiver(NetworkManager.Side.C2S, SCROLL_NETWORK_C, ScrollNetwork::scrollBook);
  }
}
