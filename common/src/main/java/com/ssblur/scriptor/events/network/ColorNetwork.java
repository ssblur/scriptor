package com.ssblur.scriptor.events.network;

import com.ssblur.scriptor.color.CustomColors;
import com.ssblur.scriptor.events.ScriptorEvents;
import com.ssblur.scriptor.events.reloadlisteners.CustomColorReloadListener;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class ColorNetwork {
  public static void receiveColor(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
    int index = buf.readInt();
    int[] colors = buf.readVarIntArray();
    String name = buf.readUtf();

    CustomColors.INSTANCE.putColor(index, name, colors);
  }

  public static void syncColors(MinecraftServer server) {
    for(var triplet: CustomColorReloadListener.INSTANCE.cache) {
      FriendlyByteBuf out = new FriendlyByteBuf(Unpooled.buffer());
      out.writeInt(triplet.getA());
      out.writeVarIntArray(triplet.getC());
      out.writeUtf(triplet.getB());
      NetworkManager.sendToPlayers(server.getPlayerList().getPlayers(), ScriptorNetwork.CLIENT_COLOR_RECEIVE, out);
    }
  }

  public static void syncColors(ServerPlayer player) {
    for(var triplet: CustomColorReloadListener.INSTANCE.cache) {
      FriendlyByteBuf out = new FriendlyByteBuf(Unpooled.buffer());
      out.writeInt(triplet.getA());
      out.writeVarIntArray(triplet.getC());
      out.writeUtf(triplet.getB());
      NetworkManager.sendToPlayer(player, ScriptorNetwork.CLIENT_COLOR_RECEIVE, out);
    }
  }
}
