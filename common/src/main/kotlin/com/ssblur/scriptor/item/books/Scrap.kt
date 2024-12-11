package com.ssblur.scriptor.item.books

import com.ssblur.scriptor.ScriptorMod.LOGGER
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.helpers.ComponentHelper
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class Scrap(properties: Properties) : Item(properties) {
    override fun appendHoverText(
        itemStack: ItemStack,
        level: TooltipContext,
        list: List<Component>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(itemStack, level, list, tooltipFlag)

        val mutableList = mutableListOf<Component>()
        mutableList.addAll(list)

        val key = itemStack.get(ScriptorDataComponents.SPELL)
        if (key != null) {
            val parts = key.split(":".toRegex(), limit = 2).toTypedArray()
            if (parts.size == 2) ComponentHelper.updateTooltipWith(mutableList, parts[0] + ".scriptor." + parts[1])
            else LOGGER.error("Invalid Identify entry: {}", key)
        }

        ComponentHelper.addCommunityDisclaimer(mutableList, itemStack)
    }
}
