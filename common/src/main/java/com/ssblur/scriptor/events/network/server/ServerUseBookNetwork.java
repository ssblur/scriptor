package com.ssblur.scriptor.events.network.server;

import com.ssblur.scriptor.data.DictionarySavedData;
import com.ssblur.scriptor.events.network.ScriptorNetwork;
import com.ssblur.scriptor.events.network.ScriptorNetworkInterface;
import com.ssblur.scriptor.helpers.LimitedBookSerializer;
import com.ssblur.scriptor.word.Spell;
import com.ssblur.scriptor.word.subject.InventorySubject;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;

public class ServerUseBookNetwork implements ScriptorNetworkInterface<ServerUseBookNetwork.Payload> {
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
    var player = context.getPlayer();
    var level = player.level();
    var slot = value.slot;
    var item = player.containerMenu.getItems().get(slot);
    var carried = player.containerMenu.getCarried();

    if (carried.isEmpty()) return;

    var text = carried.get(DataComponents.WRITTEN_BOOK_CONTENT);
    if (text != null && level instanceof ServerLevel server) {
      Spell spell = DictionarySavedData.computeIfAbsent(server).parse(LimitedBookSerializer.decodeText(text));
      if (spell == null) return;
      if (spell.subject() instanceof InventorySubject subject) {
        subject.castOnItem(spell, player, item);
        player.getCooldowns().addCooldown(carried.getItem(), (int) Math.round(spell.cost() * 7));
      }
    }
  }

  public record Payload(int slot) implements CustomPacketPayload {
    public static final Type<Payload> TYPE = new Type<>(ScriptorNetwork.SERVER_CURSOR_USE_BOOK);
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
