package com.ssblur.scriptor.events.network.server;

import com.ssblur.scriptor.data.saved_data.DictionarySavedData;
import com.ssblur.scriptor.events.network.ScriptorNetwork;
import com.ssblur.scriptor.events.network.ScriptorNetworkInterface;
import com.ssblur.scriptor.events.network.client.ClientCreativeBookNetwork;
import com.ssblur.scriptor.helpers.LimitedBookSerializer;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ServerCreativeEnchantNetwork implements ScriptorNetworkInterface<ServerCreativeEnchantNetwork.Payload> {
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
    var item = value.item;
    var slot = value.slot;

    var text = item.get(DataComponents.WRITTEN_BOOK_CONTENT);
    if(text == null) return;
    var spell = DictionarySavedData.computeIfAbsent((ServerLevel) context.getPlayer().level()).parseComponents(LimitedBookSerializer.decodeText(text));

    NetworkManager.sendToPlayer((ServerPlayer) context.getPlayer(), new ClientCreativeBookNetwork.Payload(spell, slot));
  }

  public record Payload(int slot, ItemStack item) implements CustomPacketPayload {
    public static final Type<Payload> TYPE = new Type<>(ScriptorNetwork.SERVER_CURSOR_USE_BOOK_CREATIVE);
    public static final StreamCodec<RegistryFriendlyByteBuf, Payload> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.INT,
      Payload::slot,
      ItemStack.STREAM_CODEC,
      Payload::item,
      Payload::new
    );

    @Override
    public Type<Payload> type() {
      return TYPE;
    }
  }
}
