package com.ssblur.scriptor.events.network;

import com.ssblur.scriptor.ScriptorMod;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class ConfigNetwork {
  enum Flags {
    COMMUNITY
  }

  public static void sendCommunityMode(ServerPlayer player, boolean flag) {
    FriendlyByteBuf out = new FriendlyByteBuf(Unpooled.buffer());

    out.writeEnum(Flags.COMMUNITY);
    out.writeBoolean(flag);

    NetworkManager.sendToPlayer(
      player,
      ScriptorNetwork.CLIENT_FLAG,
      out
    );
  }

  public static void sendCommunityMode(Iterable<ServerPlayer> player, boolean flag) {
    FriendlyByteBuf out = new FriendlyByteBuf(Unpooled.buffer());

    out.writeEnum(Flags.COMMUNITY);
    out.writeBoolean(flag);

    NetworkManager.sendToPlayers(
      player,
      ScriptorNetwork.CLIENT_FLAG,
      out
    );
  }

  public static void receiveFlagMessage(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
    Flags flag = buf.readEnum(Flags.class);
    switch(flag) {
      case COMMUNITY -> ScriptorMod.COMMUNITY_MODE = buf.readBoolean();
      default -> {}
    }
  }
}
