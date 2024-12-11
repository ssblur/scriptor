package com.ssblur.scriptor.item.books

import com.ssblur.scriptor.events.network.server.CreativeIdentifyNetwork
import com.ssblur.scriptor.events.network.server.ServerIdentifyNetwork
import com.ssblur.scriptor.helpers.LimitedBookSerializer
import dev.architectury.networking.NetworkManager
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ClickAction
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class IdentifyScroll(properties: Properties) : Item(properties) {
    @Environment(EnvType.CLIENT)
    override fun overrideStackedOnOther(
        itemStack: ItemStack,
        slot: Slot,
        clickAction: ClickAction,
        player: Player
    ): Boolean {
        if (clickAction == ClickAction.SECONDARY && !slot.item.isEmpty && slot.item.item is Spellbook) {
            if (player.cooldowns.isOnCooldown(this)) return true

            val level = player.level()
            if (!level.isClientSide) return true

            if (player.isCreative) {
                val book = slot.item.get(DataComponents.WRITTEN_BOOK_CONTENT)
                val spell = LimitedBookSerializer.decodeText(book!!)
                NetworkManager.sendToServer(CreativeIdentifyNetwork.Payload(slot.index, spell))
                player.cooldowns.addCooldown(this, 10)
            } else NetworkManager.sendToServer(ServerIdentifyNetwork.Payload(slot.index))
            return true
        }
        return false
    }

    override fun appendHoverText(
        itemStack: ItemStack,
        level: TooltipContext,
        list: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(itemStack, level, list, tooltipFlag)
        list.add(Component.translatable("extra.scriptor.use_identify"))
    }
}
