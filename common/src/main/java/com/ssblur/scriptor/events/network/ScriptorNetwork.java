package com.ssblur.scriptor.events.network;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.events.network.client.*;
import com.ssblur.scriptor.events.network.server.*;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.minecraft.resources.ResourceLocation;

public class ScriptorNetwork {
  public static final String MOD_ID = ScriptorMod.MOD_ID;
  public static final ResourceLocation CLIENT_GET_TRACE_DATA = new ResourceLocation(MOD_ID, "client_get_touch_data");
  public static final ResourceLocation CLIENT_GET_HITSCAN_DATA = new ResourceLocation(MOD_ID, "client_get_hitscan_data");
  public static final ResourceLocation SERVER_RETURN_TRACE_DATA = new ResourceLocation(MOD_ID, "server_return_touch_data");
  public static final ResourceLocation SERVER_RECEIVE_CHALK_MESSAGE = new ResourceLocation(MOD_ID, "server_receive_chalk_message");
  public static final ResourceLocation SERVER_CURSOR_USE_BOOK = new ResourceLocation(MOD_ID, "server_cursor_use_book");
  public static final ResourceLocation SERVER_CURSOR_USE_BOOK_CREATIVE = new ResourceLocation(MOD_ID, "server_cursor_use_bookc");
  public static final ResourceLocation CLIENT_CURSOR_RETURN_BOOK_CREATIVE = new ResourceLocation(MOD_ID, "client_cursor_return_bookc");
  public static final ResourceLocation SERVER_CURSOR_USE_SCROLL = new ResourceLocation(MOD_ID, "server_cursor_use_scroll");
  public static final ResourceLocation SERVER_CURSOR_USE_SCROLL_CREATIVE = new ResourceLocation(MOD_ID, "server_cursor_use_scrollc");
  public static final ResourceLocation CLIENT_CURSOR_RETURN_SCROLL_CREATIVE = new ResourceLocation(MOD_ID, "client_cursor_return_scrollc");
  public static final ResourceLocation CLIENT_COLOR_RECEIVE = new ResourceLocation(MOD_ID, "client_color_receivec");
  public static final ResourceLocation CLIENT_PARTICLE = new ResourceLocation(MOD_ID, "client_particle");
  public static final ResourceLocation CLIENT_FLAG = new ResourceLocation(MOD_ID, "client_flag");
  public static final ResourceLocation SERVER_SCROLL_NETWORK = new ResourceLocation(MOD_ID, "server_scroll_networkc");

  public static void register() {
    register(new ServerTraceNetwork());
    register(new ChalkNetwork());
    register(new ServerUseBookNetwork());
    register(new ServerCreativeEnchantNetwork());
    register(new ServerIdentifyNetwork());
    register(new CreativeIdentifyNetwork());
    register(new ScrollNetwork());

    if(Platform.getEnv() == EnvType.CLIENT) {
      register(new ClientTraceNetwork());
      register(new ClientExtendedTraceNetwork());
      register(new ClientCreativeBookNetwork());
      register(new ClientIdentifyNetwork());
      register(new ReceiveColorNetwork());
      register(new ParticleNetwork());
      register(new ReceiveConfigNetwork());
    }
  }

  public static void register(ScriptorNetworkInterface networkInterface) {
    NetworkManager.registerReceiver(networkInterface.side(), networkInterface.type(), networkInterface.streamCodec(), networkInterface::receive);
  }

}
