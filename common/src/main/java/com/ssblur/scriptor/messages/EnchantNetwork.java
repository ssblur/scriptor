package com.ssblur.scriptor.messages;

import com.ssblur.scriptor.events.ScriptorEvents;
import com.ssblur.scriptor.helpers.DictionarySavedData;
import com.ssblur.scriptor.helpers.LimitedBookSerializer;
import com.ssblur.scriptor.word.Spell;
import com.ssblur.scriptor.word.subject.InventorySubject;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.ContainerSynchronizer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.Objects;

public class EnchantNetwork {
  public static void useBook(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
    var player = context.getPlayer();
    var slot = buf.readInt();
    var item = player.containerMenu.getItems().get(slot);
    var level = player.level;
    var carried = player.containerMenu.getCarried();

    if(carried == null || carried.isEmpty()) return;

    CompoundTag tag = carried.getTag();
    if(tag != null && level instanceof ServerLevel server) {
      var text = tag.getList("pages", Tag.TAG_STRING);
      Spell spell = DictionarySavedData.computeIfAbsent(server).parse(LimitedBookSerializer.decodeText(text));
      if(spell == null) return;
      if(spell.subject() instanceof InventorySubject subject) {
        subject.castOnItem(spell, player, item);
        player.getCooldowns().addCooldown(carried.getItem(), (int) Math.round(spell.cost() * 7));
      }
    }
  }

  public static void clientUseBook(int slot) {
    FriendlyByteBuf out = new FriendlyByteBuf(Unpooled.buffer());
    out.writeInt(slot);
    NetworkManager.sendToServer(ScriptorEvents.CURSOR_USE_BOOK, out);
  }

  public static void useBookCreative(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
    // Creative users manage slots differently and don't sync their cursor.
    // So, they manage this differently.
    // Could potentially cause some spells cast this way not to work in creative mode.
  }

  public static void clientUseBookCreative() {
    // This is necessary because dictionary data is stored on the server.
    // Gives the item since slot numbers aren't consistent.
  }
}
