package com.ssblur.scriptor.events.network.client;

import com.ssblur.scriptor.data_components.ScriptorDataComponents;
import com.ssblur.scriptor.events.network.ScriptorNetwork;
import com.ssblur.scriptor.events.network.ScriptorNetworkInterface;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

public class ClientIdentifyNetwork implements ScriptorNetworkInterface<ClientIdentifyNetwork.Payload> {
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
    Minecraft.getInstance().player.containerMenu.getSlot(value.slot).getItem().set(ScriptorDataComponents.IDENTIFIED, value.components);
  }

  public record Payload(List<String> components, int slot) implements CustomPacketPayload {
    public static final Type<Payload> TYPE = new Type<>(ScriptorNetwork.CLIENT_CURSOR_RETURN_SCROLL_CREATIVE);
    public static final StreamCodec<RegistryFriendlyByteBuf, Payload> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
      Payload::components,
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
