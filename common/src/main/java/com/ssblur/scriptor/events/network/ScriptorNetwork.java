package com.ssblur.scriptor.events.network;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.events.network.client.*;
import com.ssblur.scriptor.events.network.server.*;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class ScriptorNetwork {
  public static final String MOD_ID = ScriptorMod.MOD_ID;
  public static final ResourceLocation CLIENT_GET_TRACE_DATA = ScriptorMod.location("client_get_touch_data");
  public static final ResourceLocation CLIENT_GET_HITSCAN_DATA = ScriptorMod.location("client_get_hitscan_data");
  public static final ResourceLocation SERVER_RETURN_TRACE_DATA = ScriptorMod.location("server_return_touch_data");
  public static final ResourceLocation SERVER_RECEIVE_CHALK_MESSAGE = ScriptorMod.location("server_receive_chalk_message");
  public static final ResourceLocation SERVER_CURSOR_USE_BOOK = ScriptorMod.location("server_cursor_use_book");
  public static final ResourceLocation SERVER_CURSOR_USE_BOOK_CREATIVE = ScriptorMod.location("server_cursor_use_bookc");
  public static final ResourceLocation CLIENT_CURSOR_RETURN_BOOK_CREATIVE = ScriptorMod.location("client_cursor_return_bookc");
  public static final ResourceLocation SERVER_CURSOR_USE_SCROLL = ScriptorMod.location("server_cursor_use_scroll");
  public static final ResourceLocation SERVER_CURSOR_USE_SCROLL_CREATIVE = ScriptorMod.location("server_cursor_use_scrollc");
  public static final ResourceLocation CLIENT_CURSOR_RETURN_SCROLL_CREATIVE = ScriptorMod.location("client_cursor_return_scrollc");
  public static final ResourceLocation CLIENT_COLOR_RECEIVE = ScriptorMod.location("client_color_receivec");
  public static final ResourceLocation CLIENT_PARTICLE = ScriptorMod.location("client_particle");
  public static final ResourceLocation CLIENT_FLAG = ScriptorMod.location("client_flag");
  public static final ResourceLocation SERVER_SCROLL_NETWORK = ScriptorMod.location("server_scroll_networkc");

  public static void register() {
    register(new ServerTraceNetwork());
    register(new ChalkNetwork());
    register(new ServerUseBookNetwork());
    register(new ServerCreativeEnchantNetwork());
    register(new ServerIdentifyNetwork());
    register(new CreativeIdentifyNetwork());
    register(new ScrollNetwork());

    register(new ClientTraceNetwork());
    register(new ClientExtendedTraceNetwork());
    register(new ClientCreativeBookNetwork());
    register(new ClientIdentifyNetwork());
    register(new ReceiveColorNetwork());
    register(new ParticleNetwork());
    register(new ReceiveConfigNetwork());
  }

  public static <T extends CustomPacketPayload> void register(ScriptorNetworkInterface<T> networkInterface) {
    if(networkInterface.side() == NetworkManager.Side.C2S || Platform.getEnv() == EnvType.CLIENT)
      NetworkManager.registerReceiver(networkInterface.side(), networkInterface.type(), networkInterface.streamCodec(), networkInterface);

    if(networkInterface.side() == NetworkManager.Side.S2C && Platform.getEnv() == EnvType.SERVER)
      NetworkManager.registerS2CPayloadType(networkInterface.type(), networkInterface.streamCodec());
  }

}
