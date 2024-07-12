package com.ssblur.scriptor.events.network.client;

import com.ssblur.scriptor.color.CustomColors;
import com.ssblur.scriptor.events.network.ScriptorNetwork;
import com.ssblur.scriptor.events.network.ScriptorNetworkInterface;
import com.ssblur.scriptor.events.reloadlisteners.CustomColorReloadListener;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class ReceiveColorNetwork implements ScriptorNetworkInterface<ReceiveColorNetwork.Payload> {
  @Override
  public CustomPacketPayload.Type<Payload> type() {
    return Payload.TYPE;
  }

  @Override
  public StreamCodec<RegistryFriendlyByteBuf, Payload> streamCodec() {
    return Payload.STREAM_CODEC;
  }

  @Override
  public NetworkManager.Side side() {
    return NetworkManager.Side.S2C;
  }

  @Override
  public void receive(Payload value, NetworkManager.PacketContext context) {
    CustomColors.INSTANCE.putColor(value.index, value.name, new int[]{value.r, value.g, value.b});
  }

  public static void syncColors(MinecraftServer server) {
    for(var triplet: CustomColorReloadListener.INSTANCE.cache) {
      NetworkManager.sendToPlayers(
        server.getPlayerList().getPlayers(),
        new Payload(
          triplet.getB(),
          triplet.getA(),
          triplet.getC()[0], triplet.getC()[1], triplet.getC()[2]
        )
      );
    }
  }

  public static void syncColors(ServerPlayer player) {
    for(var triplet: CustomColorReloadListener.INSTANCE.cache) {
      NetworkManager.sendToPlayer(
        player,
        new Payload(
          triplet.getB(),
          triplet.getA(),
          triplet.getC()[0], triplet.getC()[1], triplet.getC()[2]
        )
      );
    }
  }

  public record Payload(String name, int index, int r, int g, int b) implements CustomPacketPayload {
    public static final Type<Payload> TYPE = new Type<>(ScriptorNetwork.CLIENT_COLOR_RECEIVE);
    public static final StreamCodec<RegistryFriendlyByteBuf, Payload> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.STRING_UTF8,
      Payload::name,
      ByteBufCodecs.INT,
      Payload::index,
      ByteBufCodecs.INT,
      Payload::r,
      ByteBufCodecs.INT,
      Payload::g,
      ByteBufCodecs.INT,
      Payload::b,
      Payload::new
    );

    @Override
    public Type<Payload> type() {
      return TYPE;
    }
  }
}
