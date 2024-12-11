package com.ssblur.scriptor.events.network.server;

import com.ssblur.scriptor.data.components.BookOfBooksData;
import com.ssblur.scriptor.data.components.ScriptorDataComponents;
import com.ssblur.scriptor.events.network.ScriptorNetwork;
import com.ssblur.scriptor.events.network.ScriptorNetworkInterface;
import com.ssblur.scriptor.item.books.BookOfBooks;
import dev.architectury.networking.NetworkManager;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;

public class ScrollNetwork implements ScriptorNetworkInterface<ScrollNetwork.Payload> {
  public static void sendScroll(InteractionHand interactionHand, double amount) {
    NetworkManager.sendToServer(new Payload(interactionHand, amount));
  }

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
    int direction = (int) Math.signum(value.amount);
    var hand = value.hand;

    var player = context.getPlayer();
    var item = player.getItemInHand(hand);
    if(item.getItem() instanceof BookOfBooks) {
      var book = item.get(ScriptorDataComponents.BOOK_OF_BOOKS);

      if(book == null) return;
      var list = book.items;
      if(list.isEmpty()) return;

      int slot = book.active;
      slot = slot + direction + list.size();
      slot = slot % list.size();
      item.set(ScriptorDataComponents.BOOK_OF_BOOKS, new BookOfBooksData(list, slot));
    }
  }

  public record Payload(InteractionHand hand, double amount) implements CustomPacketPayload {
    public static final Type<Payload> TYPE = new Type<>(ScriptorNetwork.SERVER_SCROLL_NETWORK);
    public static final StreamCodec<RegistryFriendlyByteBuf, Payload> STREAM_CODEC = StreamCodec.composite(
      StreamCodec.of(FriendlyByteBuf::writeEnum, e -> e.readEnum(InteractionHand.class)),
      Payload::hand,
      ByteBufCodecs.DOUBLE,
      Payload::amount,
      Payload::new
    );

    @MethodsReturnNonnullByDefault
    @Override
    public Type<Payload> type() {
      return TYPE;
    }
  }
}
