package com.ssblur.scriptor.messages;

import com.ssblur.scriptor.events.ScriptorEvents;
import com.ssblur.scriptor.helpers.DictionarySavedData;
import com.ssblur.scriptor.helpers.LimitedBookSerializer;
import com.ssblur.scriptor.word.Spell;
import com.ssblur.scriptor.word.subject.InventorySubject;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.checkerframework.checker.units.qual.C;

import java.util.List;
import java.util.Random;

public class IdentifyNetwork {
  public static void useScroll(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
    var player = context.getPlayer();
    var slot = buf.readInt();
    var item = player.containerMenu.getItems().get(slot);
    var level = player.level;
    var carried = player.containerMenu.getCarried();

    if(carried == null || carried.isEmpty()) return;

    CompoundTag tag = item.getTag();
    if(tag != null && level instanceof ServerLevel server) {
      var text = tag.getList("pages", Tag.TAG_STRING);
      List<String> tokens = DictionarySavedData.computeIfAbsent(server).parseComponents(LimitedBookSerializer.decodeText(text));
      if(tokens == null) return;

      var scriptor = item.getOrCreateTagElement("scriptor");
      if(!scriptor.contains("identified"))
        scriptor.put("identified", new CompoundTag());
      var identified = scriptor.getCompound("identified");

      Random random = new Random();
      int firstPick = random.nextInt(tokens.size());
      int secondPick = random.nextInt(tokens.size());
      while(secondPick == firstPick)
        secondPick = random.nextInt(tokens.size());

      identified.putBoolean(tokens.get(firstPick), true);
      identified.putBoolean(tokens.get(secondPick), true);
      carried.shrink(1);
      player.getCooldowns().addCooldown(carried.getItem(), 10);
    }
  }

  public static void clientUseScroll(int slot) {
    FriendlyByteBuf out = new FriendlyByteBuf(Unpooled.buffer());
    out.writeInt(slot);
    NetworkManager.sendToServer(ScriptorEvents.CURSOR_USE_SCROLL, out);
  }

  public static void receiveDataCreative(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
    int slot = buf.readInt();
    CompoundTag tag = buf.readAnySizeNbt();

    assert Minecraft.getInstance().player != null;
    Minecraft.getInstance().player.containerMenu.getSlot(slot).getItem().setTag(tag);
  }

  public static void useScrollCreative(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
    var level = context.getPlayer().level;
    int slot = buf.readInt();
    CompoundTag tag = buf.readAnySizeNbt();

    if(tag != null && level instanceof ServerLevel server) {
      var text = tag.getList("pages", Tag.TAG_STRING);
      List<String> tokens = DictionarySavedData.computeIfAbsent(server).parseComponents(LimitedBookSerializer.decodeText(text));
      if(tokens == null) return;

      if(!tag.contains("scriptor"))
        tag.put("scriptor", new CompoundTag());
      var scriptor = tag.getCompound("scriptor");
      if(!scriptor.contains("identified"))
        scriptor.put("identified", new CompoundTag());
      var identified = scriptor.getCompound("identified");

      Random random = new Random();
      int firstPick = random.nextInt(tokens.size());
      int secondPick = random.nextInt(tokens.size());
      while(secondPick == firstPick)
        secondPick = random.nextInt(tokens.size());

      identified.putBoolean(tokens.get(firstPick), true);
      identified.putBoolean(tokens.get(secondPick), true);

      FriendlyByteBuf out = new FriendlyByteBuf(Unpooled.buffer());
      out.writeInt(slot);
      out.writeNbt(tag);
      NetworkManager.sendToPlayer((ServerPlayer) context.getPlayer(), ScriptorEvents.CURSOR_RETURN_SCROLLC, out);
    }
  }

  public static void clientUseScrollCreative(ItemStack book, int slot) {
    FriendlyByteBuf out = new FriendlyByteBuf(Unpooled.buffer());
    out.writeInt(slot);
    out.writeNbt(book.getTag());
    NetworkManager.sendToServer(ScriptorEvents.CURSOR_USE_SCROLLC, out);
  }
}
