package com.ssblur.scriptor.item.tools

import com.ssblur.scriptor.events.network.server.ChalkNetwork
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

open class Chalk(properties: Properties) : Item(properties) {
    override fun appendHoverText(
        itemStack: ItemStack,
        level: TooltipContext,
        list: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(itemStack, level, list, tooltipFlag)
        if (!hasName(itemStack)) {
            list.add(Component.translatable("extra.scriptor.chalk_unnamed_1").withStyle(ChatFormatting.RED))
            list.add(Component.translatable("extra.scriptor.chalk_unnamed_2"))
        }
    }

    override fun use(
        level: Level,
        player: Player,
        interactionHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {
        val result = super.use(level, player, interactionHand)

        if (level.isClientSide) ChalkNetwork.sendChalkMessage()

        return result
    }

    companion object {
        fun hasName(itemStack: ItemStack): Boolean {
            val name = itemStack.get(DataComponents.ITEM_NAME)
            return name != null
        }
    }
}
