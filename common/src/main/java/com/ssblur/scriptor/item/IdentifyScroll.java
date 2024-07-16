package com.ssblur.scriptor.item;

import com.ssblur.scriptor.events.network.server.CreativeIdentifyNetwork;
import com.ssblur.scriptor.events.network.server.ServerIdentifyNetwork;
import com.ssblur.scriptor.helpers.LimitedBookSerializer;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class IdentifyScroll extends Item {
  public IdentifyScroll(Properties properties) {
    super(properties);
  }

  @Environment(EnvType.CLIENT)
  public boolean overrideStackedOnOther(ItemStack itemStack, Slot slot, ClickAction clickAction, Player player) {
    if(clickAction == ClickAction.SECONDARY && !slot.getItem().isEmpty() && slot.getItem().getItem() instanceof Spellbook) {
      if(player.getCooldowns().isOnCooldown(this)) return true;

      var level = player.level();
      if (!level.isClientSide) return true;

      if(player.isCreative()) {
        var book = slot.getItem().get(DataComponents.WRITTEN_BOOK_CONTENT);
        var spell = LimitedBookSerializer.decodeText(book);
        NetworkManager.sendToServer(new CreativeIdentifyNetwork.Payload(slot.index, spell));
        player.getCooldowns().addCooldown(this, 10);
      } else
        NetworkManager.sendToServer(new ServerIdentifyNetwork.Payload(slot.index));
      return true;
    }
    return false;
  }

  @Override
  public void appendHoverText(ItemStack itemStack, TooltipContext level, List<Component> list, TooltipFlag tooltipFlag) {
    super.appendHoverText(itemStack, level, list, tooltipFlag);
    list.add(Component.translatable("extra.scriptor.use_identify"));
  }
}
