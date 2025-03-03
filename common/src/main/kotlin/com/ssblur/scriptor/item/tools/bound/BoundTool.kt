package com.ssblur.scriptor.item.tools.bound

import com.ssblur.scriptor.color.CustomColors.getColor
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.unfocused.helper.ColorHelper.registerColor
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
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
  init {
    try {
      clientInit()
    } catch (_: NoSuchMethodError) {
    }
  }

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

  @Environment(EnvType.CLIENT)
  fun clientInit() {
    this.registerColor { itemStack: ItemStack?, t: Int ->
      if (t == 1) getColor(
        itemStack!!
      ) else -0x1
    }
  }
}
