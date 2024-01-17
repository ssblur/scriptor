package com.ssblur.scriptor.events.network;

import com.ssblur.scriptor.events.ScriptorEvents;
import com.ssblur.scriptor.data.DictionarySavedData;
import com.ssblur.scriptor.helpers.LimitedBookSerializer;
import com.ssblur.scriptor.registry.words.WordRegistry;
import com.ssblur.scriptor.word.PartialSpell;
import com.ssblur.scriptor.word.Spell;
import com.ssblur.scriptor.word.action.Action;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.subject.InventorySubject;
import com.ssblur.scriptor.word.subject.Subject;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Objects;

public class EnchantNetwork {
  public static void useBook(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
    var player = context.getPlayer();
    var level = player.level();
    var slot = buf.readInt();
    var item = player.containerMenu.getItems().get(slot);
    var carried = player.containerMenu.getCarried();

    if (carried == null || carried.isEmpty()) return;

    CompoundTag tag = carried.getTag();
    if (tag != null && level instanceof ServerLevel server) {
      var text = tag.getList("pages", Tag.TAG_STRING);
      Spell spell = DictionarySavedData.computeIfAbsent(server).parse(LimitedBookSerializer.decodeText(text));
      if (spell == null) return;
      if (spell.subject() instanceof InventorySubject subject) {
        subject.castOnItem(spell, player, item);
        player.getCooldowns().addCooldown(carried.getItem(), (int) Math.round(spell.cost() * 7));
      }
    }
  }

  public static void clientUseBook(int slot) {
    FriendlyByteBuf out = new FriendlyByteBuf(Unpooled.buffer());
    out.writeInt(slot);
    NetworkManager.sendToServer(ScriptorNetwork.SERVER_CURSOR_USE_BOOK, out);
  }

  public static void useBookCreative(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
    var item = ItemStack.of(Objects.requireNonNull(buf.readNbt()));
    var slot = buf.readInt();

    var compound = item.getTag();
    if(compound == null) return;
    var text = compound.getList("pages", Tag.TAG_STRING);
    var spell = DictionarySavedData.computeIfAbsent((ServerLevel) context.getPlayer().level()).parseComponents(LimitedBookSerializer.decodeText(text));
    var tagOut = new CompoundTag();
    var list = new ListTag();
    spell.forEach(i -> list.add(StringTag.valueOf(i)));
    tagOut.put("components", list);

    FriendlyByteBuf out = new FriendlyByteBuf(Unpooled.buffer());
    out.writeNbt(tagOut);
    out.writeInt(slot);
    NetworkManager.sendToPlayer((ServerPlayer) context.getPlayer(), ScriptorNetwork.CLIENT_CURSOR_RETURN_BOOK_CREATIVE, out);
  }

  public static void returnBookCreative(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
    var tag = buf.readNbt();
    if(tag == null) return;

    var slot = buf.readInt();
    var list = tag.getList("components", Tag.TAG_STRING);
    var components = list.stream().map(Object::toString).toList();

    Action action = null;
    Subject subject = null;
    ArrayList<Descriptor> descriptor = new ArrayList<>();
    for(var i: components) {
      var split = i.replace("\"", "").split(":", 2);

      switch (split[0]) {
        case "action" -> action = WordRegistry.INSTANCE.actionRegistry.get(split[1]);
        case "descriptor" -> descriptor.add(WordRegistry.INSTANCE.descriptorRegistry.get(split[1]));
        case "subject" -> subject = WordRegistry.INSTANCE.subjectRegistry.get(split[1]);
      }
    }
    Spell spell = new Spell(subject, new PartialSpell(action, descriptor.toArray(new Descriptor[0])));
    var player = context.getPlayer();
    var item = player.containerMenu.getItems().get(slot);
    var carried = player.containerMenu.getCarried();
    if(spell.subject() instanceof InventorySubject inventorySubject) {
      inventorySubject.castOnItem(spell, player, item);
      player.getCooldowns().addCooldown(carried.getItem(), 5);
    }
  }

  public static void clientUseBookCreative(ItemStack itemStack, int slot) {
    FriendlyByteBuf out = new FriendlyByteBuf(Unpooled.buffer());
    var tag = new CompoundTag();
    itemStack.save(tag);
    out.writeNbt(tag);
    out.writeInt(slot);
    NetworkManager.sendToServer(ScriptorNetwork.SERVER_CURSOR_USE_BOOK_CREATIVE, out);
  }
}
