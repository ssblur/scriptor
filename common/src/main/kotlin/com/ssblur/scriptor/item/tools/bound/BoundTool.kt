package com.ssblur.scriptor.item.tools.bound

import com.ssblur.scriptor.data.components.ScriptorDataComponents
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.tags.TagKey
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.block.Block

class BoundTool(tier: Tier, tagKey: TagKey<Block>, properties: Properties):
  DiggerItem(tier, tagKey, properties) {
  override fun appendHoverText(
    itemStack: ItemStack,
    tooltipContext: TooltipContext,
    list: MutableList<Component>,
    tooltipFlag: TooltipFlag
  ) {
    super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag)
    if(!itemStack.has(ScriptorDataComponents.EXPIRES))
      list.add(Component.translatable("lore.scriptor.bound_tool").withStyle(ChatFormatting.GRAY))
  }
}
