package com.ssblur.scriptor.item;

import com.ssblur.scriptor.messages.IdentifyNetwork;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IdentifyScroll extends Item {
  public IdentifyScroll(Properties properties) {
    super(properties);
  }

  public boolean overrideStackedOnOther(ItemStack itemStack, Slot slot, ClickAction clickAction, Player player) {
    if (clickAction == ClickAction.SECONDARY && !slot.getItem().isEmpty() && slot.getItem().getItem() instanceof Spellbook) {
      if(player.getCooldowns().isOnCooldown(this)) return true;
      IdentifyNetwork.clientUseScroll(slot.index);
      return true;
    }
    return false;
  }

  @Override
  public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
    super.appendHoverText(itemStack, level, list, tooltipFlag);
    list.add(Component.translatable("extra.scriptor.use_identify"));
  }
}
