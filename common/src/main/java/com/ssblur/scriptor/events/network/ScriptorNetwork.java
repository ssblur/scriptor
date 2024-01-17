package com.ssblur.scriptor.events.network;

import com.ssblur.scriptor.ScriptorMod;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.minecraft.resources.ResourceLocation;

public class ScriptorNetwork {
  public static final String MOD_ID = ScriptorMod.MOD_ID;
  public static final ResourceLocation CLIENT_GET_TRACE_DATA = new ResourceLocation(MOD_ID, "get_touch_data");
  public static final ResourceLocation CLIENT_GET_HITSCAN_DATA = new ResourceLocation(MOD_ID, "get_hitscan_data");
  public static final ResourceLocation SERVER_RETURN_TRACE_DATA = new ResourceLocation(MOD_ID, "return_touch_data");
  public static final ResourceLocation SERVER_RECEIVE_CHALK_MESSAGE = new ResourceLocation(MOD_ID, "receive_chalk_message");
  public static final ResourceLocation SERVER_CURSOR_USE_BOOK = new ResourceLocation(MOD_ID, "cursor_use_book");
  public static final ResourceLocation SERVER_CURSOR_USE_BOOK_CREATIVE = new ResourceLocation(MOD_ID, "cursor_use_bookc");
  public static final ResourceLocation CLIENT_CURSOR_RETURN_BOOK_CREATIVE = new ResourceLocation(MOD_ID, "cursor_return_bookc");
  public static final ResourceLocation SERVER_CURSOR_USE_SCROLL = new ResourceLocation(MOD_ID, "cursor_use_scroll");
  public static final ResourceLocation SERVER_CURSOR_USE_SCROLL_CREATIVE = new ResourceLocation(MOD_ID, "cursor_use_scrollc");
  public static final ResourceLocation CLIENT_CURSOR_RETURN_SCROLL_CREATIVE = new ResourceLocation(MOD_ID, "cursor_return_scrollc");
  public static final ResourceLocation CLIENT_COLOR_RECEIVE = new ResourceLocation(MOD_ID, "color_receivec");
  public static final ResourceLocation CLIENT_PARTICLE = new ResourceLocation(MOD_ID, "particle");
  public static final ResourceLocation SERVER_SCROLL_NETWORK = new ResourceLocation(MOD_ID, "scroll_networkc");
  public static void register() {
    NetworkManager.registerReceiver(NetworkManager.Side.C2S, SERVER_RETURN_TRACE_DATA, TraceNetwork::returnTraceData);
    NetworkManager.registerReceiver(NetworkManager.Side.C2S, SERVER_RECEIVE_CHALK_MESSAGE, ChalkNetwork::receiveChalkMessage);
    NetworkManager.registerReceiver(NetworkManager.Side.C2S, SERVER_CURSOR_USE_BOOK, EnchantNetwork::useBook);
    NetworkManager.registerReceiver(NetworkManager.Side.C2S, SERVER_CURSOR_USE_BOOK_CREATIVE, EnchantNetwork::useBookCreative);
    NetworkManager.registerReceiver(NetworkManager.Side.C2S, SERVER_CURSOR_USE_SCROLL, IdentifyNetwork::useScroll);
    NetworkManager.registerReceiver(NetworkManager.Side.C2S, SERVER_CURSOR_USE_SCROLL_CREATIVE, IdentifyNetwork::useScrollCreative);
    NetworkManager.registerReceiver(NetworkManager.Side.C2S, SERVER_SCROLL_NETWORK, ScrollNetwork::scrollBook);

    if(Platform.getEnv() == EnvType.CLIENT) {
      NetworkManager.registerReceiver(NetworkManager.Side.S2C, CLIENT_GET_TRACE_DATA, TraceNetwork::getTraceData);
      NetworkManager.registerReceiver(NetworkManager.Side.S2C, CLIENT_GET_HITSCAN_DATA, TraceNetwork::getExtendedTraceData);
      NetworkManager.registerReceiver(NetworkManager.Side.S2C, CLIENT_CURSOR_RETURN_SCROLL_CREATIVE, IdentifyNetwork::receiveDataCreative);
      NetworkManager.registerReceiver(NetworkManager.Side.S2C, CLIENT_CURSOR_RETURN_BOOK_CREATIVE, EnchantNetwork::returnBookCreative);
      NetworkManager.registerReceiver(NetworkManager.Side.S2C, CLIENT_COLOR_RECEIVE, ColorNetwork::receiveColor);
      NetworkManager.registerReceiver(NetworkManager.Side.S2C, CLIENT_PARTICLE, ParticleNetwork::getParticles);
    }
  }
}
