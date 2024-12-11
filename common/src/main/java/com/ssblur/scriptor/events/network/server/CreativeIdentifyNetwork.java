package com.ssblur.scriptor.events.network.server;

import com.ssblur.scriptor.data.saved_data.DictionarySavedData;
import com.ssblur.scriptor.events.network.ScriptorNetwork;
import com.ssblur.scriptor.events.network.ScriptorNetworkInterface;
import com.ssblur.scriptor.events.network.client.ClientIdentifyNetwork;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class CreativeIdentifyNetwork implements ScriptorNetworkInterface<CreativeIdentifyNetwork.Payload> {
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
    return NetworkManager.Side.C2S;
  }

  @Override
  public void receive(Payload value, NetworkManager.PacketContext context) {
    var level = context.getPlayer().level();
    int slot = value.slot;
    var text = value.spell;

    if (level instanceof ServerLevel server) {
      List<String> tokens = DictionarySavedData.computeIfAbsent(server).parseComponents(text);
      if (tokens == null) return;
      NetworkManager.sendToPlayer(
        (ServerPlayer) context.getPlayer(),
        new ClientIdentifyNetwork.Payload(
          tokens,
          slot
        )
      );
    }
  }

  public record Payload(int slot, String spell) implements CustomPacketPayload {
    public static final Type<Payload> TYPE = new Type<>(ScriptorNetwork.SERVER_CURSOR_USE_SCROLL_CREATIVE);
    public static final StreamCodec<RegistryFriendlyByteBuf, Payload> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.INT,
      Payload::slot,
      ByteBufCodecs.STRING_UTF8,
      Payload::spell,
      Payload::new
    );

    @Override
    public Type<Payload> type() {
      return TYPE;
    }
  }
}
