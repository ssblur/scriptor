package com.ssblur.scriptor.events;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.events.messages.EnchantNetwork;
import com.ssblur.scriptor.events.messages.IdentifyNetwork;
import com.ssblur.scriptor.events.messages.ParticleNetwork;
import com.ssblur.scriptor.events.messages.TraceNetwork;
import dev.architectury.event.events.common.ChatEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.LootEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.registry.ReloadListenerRegistry;
import net.fabricmc.api.EnvType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

public class ScriptorEvents {
  public static final ResourceLocation GET_TRACE_DATA = new ResourceLocation(ScriptorMod.MOD_ID, "get_touch_data");
  public static final ResourceLocation RETURN_TRACE_DATA = new ResourceLocation(ScriptorMod.MOD_ID, "return_touch_data");
  public static final ResourceLocation CURSOR_USE_BOOK = new ResourceLocation(ScriptorMod.MOD_ID, "cursor_use_book");
  public static final ResourceLocation CURSOR_USE_BOOKC = new ResourceLocation(ScriptorMod.MOD_ID, "cursor_use_bookc");
  public static final ResourceLocation CURSOR_RETURN_BOOKC = new ResourceLocation(ScriptorMod.MOD_ID, "cursor_return_bookc");
  public static final ResourceLocation CURSOR_USE_SCROLL = new ResourceLocation(ScriptorMod.MOD_ID, "cursor_use_scroll");
  public static final ResourceLocation CURSOR_USE_SCROLLC = new ResourceLocation(ScriptorMod.MOD_ID, "cursor_use_scrollc");
  public static final ResourceLocation CURSOR_RETURN_SCROLLC = new ResourceLocation(ScriptorMod.MOD_ID, "cursor_return_scrollc");
  public static final ResourceLocation PARTICLE = new ResourceLocation(ScriptorMod.MOD_ID, "particle");

  public static void register() {
    ChatEvent.RECEIVED.register(new SpellChatEvents());
    LootEvent.MODIFY_LOOT_TABLE.register(new AddLootEvent());
    LifecycleEvent.SERVER_LEVEL_LOAD.register(new PreloadDictionary());
    ReloadListenerRegistry.register(PackType.SERVER_DATA, TomeReloadListener.INSTANCE);
    ReloadListenerRegistry.register(PackType.SERVER_DATA, ReagentReloadListener.INSTANCE);

    if(Platform.getEnv() == EnvType.CLIENT) {
      NetworkManager.registerReceiver(NetworkManager.Side.S2C, GET_TRACE_DATA, TraceNetwork::getTraceData);
      NetworkManager.registerReceiver(NetworkManager.Side.S2C, CURSOR_RETURN_SCROLLC, IdentifyNetwork::receiveDataCreative);
      NetworkManager.registerReceiver(NetworkManager.Side.S2C, CURSOR_RETURN_BOOKC, EnchantNetwork::returnBookCreative);
      NetworkManager.registerReceiver(NetworkManager.Side.S2C, PARTICLE, ParticleNetwork::getParticles);
      ScriptorEventsExpectPlatform.registerClientEvents();
    }
    NetworkManager.registerReceiver(NetworkManager.Side.C2S, RETURN_TRACE_DATA, TraceNetwork::returnTraceData);
    NetworkManager.registerReceiver(NetworkManager.Side.C2S, CURSOR_USE_BOOK, EnchantNetwork::useBook);
    NetworkManager.registerReceiver(NetworkManager.Side.C2S, CURSOR_USE_BOOKC, EnchantNetwork::useBookCreative);
    NetworkManager.registerReceiver(NetworkManager.Side.C2S, CURSOR_USE_SCROLL, IdentifyNetwork::useScroll);
    NetworkManager.registerReceiver(NetworkManager.Side.C2S, CURSOR_USE_SCROLLC, IdentifyNetwork::useScrollCreative);
  }
}
