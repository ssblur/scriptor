package com.ssblur.scriptor.item.tools

import com.ssblur.scriptor.network.server.ScriptorNetworkC2S
import com.ssblur.scriptor.network.server.ScriptorNetworkC2S.SendChalk
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult

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
        if (level.isClientSide) {
            val client = Minecraft.getInstance()
            val hit = client.hitResult
            if (hit is BlockHitResult)
                ScriptorNetworkC2S.sendChalk(SendChalk(hit, false))
        }

        return InteractionResultHolder.success(player.getItemInHand(interactionHand))
    }

    companion object {
        fun hasName(itemStack: ItemStack): Boolean {
            val name = itemStack[DataComponents.CUSTOM_NAME]
            return name != null
        }
    }
}
