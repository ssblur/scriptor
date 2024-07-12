package com.ssblur.scriptor.events.network.server;

import com.ssblur.scriptor.data.DictionarySavedData;
import com.ssblur.scriptor.data_components.ScriptorDataComponents;
import com.ssblur.scriptor.events.network.ScriptorNetwork;
import com.ssblur.scriptor.events.network.ScriptorNetworkInterface;
import com.ssblur.scriptor.helpers.LimitedBookSerializer;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.List;

public class ServerIdentifyNetwork implements ScriptorNetworkInterface<ServerIdentifyNetwork.Payload> {
  @Override
  public CustomPacketPayload.Type type() {
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
    var player = context.getPlayer();
    var level = player.level();
    var slot = value.slot;
    var item = player.containerMenu.getItems().get(slot);
    var carried = player.containerMenu.getCarried();

    if (carried.isEmpty()) return;

    var text = item.get(DataComponents.WRITTEN_BOOK_CONTENT);
    if (text != null && level instanceof ServerLevel server) {
      List<String> tokens = DictionarySavedData.computeIfAbsent(server).parseComponents(LimitedBookSerializer.decodeText(text));
      if (tokens == null) return;

      var identified = item.get(ScriptorDataComponents.IDENTIFIED);
      if(identified == null)
        identified = new ArrayList<>();
      else
        identified = new ArrayList<>(identified);

      for (var token : tokens)
        if(!identified.contains(token))
          identified.add(token);

      item.set(ScriptorDataComponents.IDENTIFIED, identified);

      carried.shrink(1);
      player.getCooldowns().addCooldown(carried.getItem(), 10);
    }
  }

  public record Payload(int slot) implements CustomPacketPayload {
    public static final Type<Payload> TYPE = new Type<>(ScriptorNetwork.SERVER_CURSOR_USE_SCROLL);
    public static final StreamCodec<RegistryFriendlyByteBuf, Payload> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.INT,
      Payload::slot,
      Payload::new
    );

    @Override
    public Type<Payload> type() {
      return TYPE;
    }
  }
}
