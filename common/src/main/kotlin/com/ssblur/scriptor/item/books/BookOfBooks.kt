package com.ssblur.scriptor.item.books

import com.mojang.blaze3d.vertex.PoseStack
import com.ssblur.scriptor.data.components.BookOfBooksData
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.helpers.SpellbookHelper
import com.ssblur.scriptor.item.interfaces.ItemWithCustomRenderer
import net.minecraft.ChatFormatting
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.SlotAccess
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ClickAction
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class BookOfBooks(properties: Properties, var capacity: Int) : Item(properties),
    ItemWithCustomRenderer {
    init {
        SpellbookHelper.SPELLBOOKS += this
    }

    override fun overrideOtherStackedOnMe(
        book: ItemStack,
        itemStack: ItemStack,
        slot: Slot,
        clickAction: ClickAction,
        player: Player,
        slotAccess: SlotAccess
    ): Boolean {
        if (clickAction != ClickAction.SECONDARY || !slot.allowModification(player)) return false

        if (itemStack.isEmpty) remove(book, slotAccess)
        else if (itemStack.item is Spellbook && getInventory(book).size < capacity) add(book, itemStack)
        else return false

        return true
    }

    override fun getName(itemStack: ItemStack): Component {
        val item = getActiveItem(itemStack)
        if (!item.isEmpty) return Component.translatable("item.scriptor.book_of_books_2", item.hoverName.string)

        return super.getName(itemStack)
    }

    override fun use(
        level: Level,
        player: Player,
        interactionHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {
        val result = super.use(level, player, interactionHand)

        val book = player.getItemInHand(interactionHand)
        val castResult = SpellbookHelper.castFromItem(getActiveItem(book), player)

        if (castResult) return result
        return InteractionResultHolder.fail(player.getItemInHand(interactionHand))
    }

    override fun appendHoverText(
        itemStack: ItemStack,
        level: TooltipContext,
        list: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(itemStack, level, list, tooltipFlag)

        val inventory = getInventory(itemStack)

        if (inventory.size == 0) {
            list.add(Component.translatable("lore.scriptor.empty_book_1").withStyle(ChatFormatting.GRAY))
            list.add(Component.translatable("lore.scriptor.empty_book_2").withStyle(ChatFormatting.GRAY))
        }

        for (i in inventory.indices) if (i == getActiveSlot(itemStack)) list.add(
            Component.translatable("lore.scriptor.active_book", inventory[i].hoverName.string).withStyle(
                ChatFormatting.GREEN
            )
        )
        else list.add(Component.literal(inventory[i].hoverName.string).withStyle(ChatFormatting.GRAY))

        list.add(Component.translatable("lore.scriptor.book_scroll").withStyle(ChatFormatting.GRAY))
    }

    override fun render(
        player: AbstractClientPlayer?,
        i: Float,
        pitch: Float,
        hand: InteractionHand?,
        swingProgress: Float,
        itemStack: ItemStack?,
        readyProgress: Float,
        matrix: PoseStack?,
        buffer: MultiBufferSource,
        lightLevel: Int
    ): Boolean {
        if(player == null || hand == null) return false
        if (getActiveItem(player.getItemInHand(hand)).item is ItemWithCustomRenderer)
            return (getActiveItem(player.getItemInHand(hand)).item as ItemWithCustomRenderer).render(
                player,
                i,
                pitch,
                hand,
                swingProgress,
                getActiveItem(player.getItemInHand(hand)),
                readyProgress,
                matrix,
                buffer,
                lightLevel
            )
        return false
    }

    companion object {
        fun getInventory(book: ItemStack): List<ItemStack> {
            val data = book.get(ScriptorDataComponents.BOOK_OF_BOOKS)
                ?: return listOf()

            return data.items
        }

        fun getActiveSlot(book: ItemStack): Int {
            val data = book.get(ScriptorDataComponents.BOOK_OF_BOOKS) ?: return 0
            return data.active
        }

        fun getActiveItem(book: ItemStack): ItemStack {
            val slot = getActiveSlot(book)
            val inventory = getInventory(book)
            if (inventory.isEmpty()) return ItemStack.EMPTY
            if (inventory.size <= slot) return inventory[inventory.size - 1]
            return inventory[slot]
        }

        fun remove(book: ItemStack, slotAccess: SlotAccess) {
            if (!slotAccess.get().isEmpty) return

            val data = book.get(ScriptorDataComponents.BOOK_OF_BOOKS) ?: return

            val list = ArrayList(data.items)
            if (list.isEmpty()) return

            val item = list.removeAt(list.size - 1)
            book.set(ScriptorDataComponents.BOOK_OF_BOOKS, BookOfBooksData(list, data.active))
            slotAccess.set(item)
        }

        fun add(book: ItemStack, item: ItemStack) {
            if (item.isEmpty) return
            val insert = item.copy()
            item.shrink(1)

            var data = book.get(ScriptorDataComponents.BOOK_OF_BOOKS)
            if (data == null) data = BookOfBooksData(listOf(), 0)

            val list = ArrayList(data.items)

            list.add(insert)
            book.set(ScriptorDataComponents.BOOK_OF_BOOKS, BookOfBooksData(list, data.active))
        }
    }
}
