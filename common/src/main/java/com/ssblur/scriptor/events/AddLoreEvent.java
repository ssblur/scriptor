package com.ssblur.scriptor.events;

import com.google.common.base.MoreObjects;
import com.ssblur.scriptor.data.components.ScriptorDataComponents;
import dev.architectury.event.events.client.ClientTooltipEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class AddLoreEvent implements ClientTooltipEvent.Item {
  @Override
  public void append(ItemStack stack, List<Component> lines, Item.TooltipContext tooltipContext, TooltipFlag flag) {
    int charges = MoreObjects.firstNonNull(stack.get(ScriptorDataComponents.CHARGES), 0);
    if(charges > 0) {
      if(charges <= 10)
        lines.add(Component.translatable("enchantment.scriptor.charged").append(" ").append(Component.translatable("enchantment.level." + charges)));
      else
        lines.add(Component.translatable("enchantment.scriptor.charged").append(" ").append("" + charges));
    }
  }
}
