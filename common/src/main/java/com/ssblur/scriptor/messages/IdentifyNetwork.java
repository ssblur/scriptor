package com.ssblur.scriptor.messages;

import com.ssblur.scriptor.events.ScriptorEvents;
import com.ssblur.scriptor.helpers.DictionarySavedData;
import com.ssblur.scriptor.helpers.LimitedBookSerializer;
import com.ssblur.scriptor.word.Spell;
import com.ssblur.scriptor.word.subject.InventorySubject;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;

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
      System.out.println(1);
      var text = tag.getList("pages", Tag.TAG_STRING);
      List<String> tokens = DictionarySavedData.computeIfAbsent(server).parseComponents(LimitedBookSerializer.decodeText(text));
      if(tokens == null) return;
      System.out.println(2);

      var scriptor = item.getOrCreateTagElement("scriptor");
      if(!scriptor.contains("identified"))
        scriptor.put("identified", new CompoundTag());
      var identified = scriptor.getCompound("identified");
      System.out.println(3);

      Random random = new Random();
      int firstPick = random.nextInt(tokens.size());
      int secondPick = random.nextInt(tokens.size());
      while(secondPick == firstPick)
        secondPick = random.nextInt(tokens.size());
      System.out.println(4);

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

  public static void useScrollCreative(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
    // Creative users manage slots differently and don't sync their cursor.
    // So, they manage this differently.
    // Could potentially cause some spells cast this way not to work in creative mode.
  }

  public static void clientUseScrollCreative() {
    // This is necessary because dictionary data is stored on the server.
    // Gives the item since slot numbers aren't consistent.
  }
}
