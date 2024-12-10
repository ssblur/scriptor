package com.ssblur.scriptor.events.network.client;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.events.network.ScriptorNetwork;
import com.ssblur.scriptor.events.network.ScriptorNetworkInterface;
import com.ssblur.scriptor.events.network.ScriptorStreamCodecs;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class ReceiveConfigNetwork implements ScriptorNetworkInterface<ReceiveConfigNetwork.Payload> {
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
    var flag = value.key;
    switch(flag) {
      case COMMUNITY -> ScriptorMod.INSTANCE.setCOMMUNITY_MODE(value.value);
      default -> {}
    }
  }

  public enum FLAGS {
    COMMUNITY
  }

  public static void sendCommunityMode(ServerPlayer player, boolean flag) {
    NetworkManager.sendToPlayer(
      player,
      new Payload(FLAGS.COMMUNITY, flag)
    );
  }

  public static void sendCommunityMode(Iterable<ServerPlayer> player, boolean flag) {
    NetworkManager.sendToPlayers(
      player,
      new Payload(FLAGS.COMMUNITY, flag)
    );
  }

  public record Payload(FLAGS key, boolean value) implements CustomPacketPayload {
    public static final Type<Payload> TYPE = new Type<>(ScriptorNetwork.CLIENT_FLAG);
    public static final StreamCodec<RegistryFriendlyByteBuf, Payload> STREAM_CODEC = StreamCodec.composite(
      ScriptorStreamCodecs.fromEnum(FLAGS.class),
      Payload::key,
      ByteBufCodecs.BOOL,
      Payload::value,
      Payload::new
    );

    @Override
    @NotNull
    public Type<Payload> type() {
      return TYPE;
    }
  }
}
