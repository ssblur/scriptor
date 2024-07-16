package com.ssblur.scriptor.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class Ingredient extends Item {
  String[] description;
  public Ingredient(Properties properties, String... description) {
    super(properties);
    this.description = description;
  }

  @Override
  public void appendHoverText(ItemStack itemStack, TooltipContext level, List<Component> list, TooltipFlag tooltipFlag) {
    super.appendHoverText(itemStack, level, list, tooltipFlag);

    for(var string: description)
      list.add(Component.translatable(string).withStyle(ChatFormatting.GRAY));
  }
}
