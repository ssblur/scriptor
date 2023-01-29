package com.ssblur.scriptor.events;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.messages.TouchNetwork;
import dev.architectury.event.events.common.ChatEvent;
import dev.architectury.event.events.common.LootEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.registry.ReloadListenerRegistry;
import net.fabricmc.api.EnvType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

public class ScriptorEvents {
  public static final ResourceLocation GET_TOUCH_DATA = new ResourceLocation(ScriptorMod.MOD_ID, "get_touch_data");
  public static final ResourceLocation RETURN_TOUCH_DATA = new ResourceLocation(ScriptorMod.MOD_ID, "return_touch_data");

  public static void register() {
    ChatEvent.RECEIVED.register(new SpellChatEvents());
    LootEvent.MODIFY_LOOT_TABLE.register(new AddLootEvent());
    ReloadListenerRegistry.register(PackType.SERVER_DATA, TomeReloadListener.INSTANCE);

    if(Platform.getEnv() == EnvType.CLIENT)
      NetworkManager.registerReceiver(NetworkManager.Side.S2C, GET_TOUCH_DATA, TouchNetwork::getTouchData);
    NetworkManager.registerReceiver(NetworkManager.Side.C2S, RETURN_TOUCH_DATA, TouchNetwork::returnTouchData);
  }
}
