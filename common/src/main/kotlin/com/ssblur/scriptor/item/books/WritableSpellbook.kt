package com.ssblur.scriptor.item.books

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.inventory.BookEditScreen
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.WritableBookItem
import net.minecraft.world.level.Level

class WritableSpellbook(properties: Properties) : WritableBookItem(properties) {
    override fun use(
        level: Level,
        player: Player,
        interactionHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {
        val itemStack = player.getItemInHand(interactionHand)
        if (level.isClientSide) openScreen(player, itemStack, interactionHand)
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide())
    }

    @Environment(EnvType.CLIENT)
    fun openScreen(player: Player, itemStack: ItemStack, interactionHand: InteractionHand) {
        Minecraft.getInstance().setScreen(BookEditScreen(player, itemStack, interactionHand))
    }
}
