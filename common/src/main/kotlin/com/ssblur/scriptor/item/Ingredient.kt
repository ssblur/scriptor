package com.ssblur.scriptor.item

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class Ingredient(properties: Properties, vararg val description: String): Item(properties) {
  override fun appendHoverText(
    itemStack: ItemStack,
    level: TooltipContext,
    list: MutableList<Component>,
    tooltipFlag: TooltipFlag
  ) {
    super.appendHoverText(itemStack, level, list, tooltipFlag)

    for (string in description) list.add(Component.translatable(string).withStyle(ChatFormatting.GRAY))
  }
}
