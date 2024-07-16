package com.ssblur.scriptor.events.network.client;

import com.ssblur.scriptor.api.word.Action;
import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.api.word.Subject;
import com.ssblur.scriptor.events.network.ScriptorNetwork;
import com.ssblur.scriptor.events.network.ScriptorNetworkInterface;
import com.ssblur.scriptor.registry.words.WordRegistry;
import com.ssblur.scriptor.word.PartialSpell;
import com.ssblur.scriptor.word.Spell;
import com.ssblur.scriptor.word.subject.InventorySubject;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.ArrayList;
import java.util.List;

public class ClientCreativeBookNetwork implements ScriptorNetworkInterface<ClientCreativeBookNetwork.Payload> {
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
    Action action = null;
    Subject subject = null;
    ArrayList<Descriptor> descriptor = new ArrayList<>();
    for(var i: value.components) {
      var split = i.replace("\"", "").split(":", 2);

      switch (split[0]) {
        case "action" -> action = WordRegistry.INSTANCE.actionRegistry.get(split[1]);
        case "descriptor" -> descriptor.add(WordRegistry.INSTANCE.descriptorRegistry.get(split[1]));
        case "subject" -> subject = WordRegistry.INSTANCE.subjectRegistry.get(split[1]);
      }
    }
    Spell spell = new Spell(subject, new PartialSpell(action, descriptor.toArray(new Descriptor[0])));
    var player = context.getPlayer();
    var item = player.containerMenu.getItems().get(value.slot);
    var carried = player.containerMenu.getCarried();
    if(spell.subject() instanceof InventorySubject inventorySubject) {
      inventorySubject.castOnItem(spell, player, item);
      player.getCooldowns().addCooldown(carried.getItem(), 5);
    }
  }

  public record Payload(List<String> components, int slot) implements CustomPacketPayload {
    public static final Type<Payload> TYPE = new Type<>(ScriptorNetwork.CLIENT_CURSOR_RETURN_BOOK_CREATIVE);
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
