package com.ssblur.scriptor.item.books

import com.ssblur.scriptor.config.ScriptorConfig
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.helpers.SpellbookHelper
import com.ssblur.scriptor.item.ScriptorTabs
import com.ssblur.scriptor.network.server.ScriptorNetworkC2S
import com.ssblur.scriptor.network.server.ScriptorNetworkC2S.UseBook
import com.ssblur.unfocused.tab.CreativeTabs.tab
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ClickAction
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.WrittenBookItem
import net.minecraft.world.level.Level

class SpellScroll(properties: Properties) : WrittenBookItem(properties) {
    init {
        tab(ScriptorTabs.SCRIPTOR_SPELLBOOKS_TAB)
    }

    override fun use(
        level: Level,
        player: Player,
        interactionHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {
        if (level.isClientSide) return InteractionResultHolder.success(player.getItemInHand(interactionHand))

        val item = player.getItemInHand(interactionHand)
        val result = SpellbookHelper.castFromItem(
            item,
            player,
            maxCost = ScriptorConfig.SCROLL_MAX_COST(),
            costMultiplier = ScriptorConfig.SCROLL_COOLDOWN_MULTIPLIER(),
            cooldownFunc = { ply, time ->
                ply.cooldowns.addCooldown(this, time)
            }
        )
        if(result) {
            player.getItemInHand(interactionHand).shrink(1)
            return InteractionResultHolder.consume(player.getItemInHand(interactionHand))
        }
        return InteractionResultHolder.fail(player.getItemInHand(interactionHand))
    }

    override fun getName(itemStack: ItemStack): Component {
        val title = itemStack.get(ScriptorDataComponents.TOME_NAME)
        if (title != null) {
            return Component.translatable(title)
        }
        return super.getName(itemStack)
    }

    override fun overrideStackedOnOther(
        itemStack: ItemStack,
        slot: Slot,
        clickAction: ClickAction,
        player: Player
    ): Boolean {
        if (clickAction == ClickAction.SECONDARY && !slot.item.isEmpty && slot.item.item !is BookOfBooks) {
            if (player.cooldowns.isOnCooldown(this)) return true
            val level = player.level()
            if (!level.isClientSide) return true
            if (player.isCreative) return false
            else ScriptorNetworkC2S.useBook(UseBook(slot.index))
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

        if (itemStack.get(DataComponents.WRITTEN_BOOK_CONTENT) == null) list.add(
            Component.translatable("lore.scriptor.no_spell").withStyle(ChatFormatting.GRAY)
        )

        Component.translatable("lore.scriptor.spell_scroll").withStyle(ChatFormatting.WHITE)
    }
}