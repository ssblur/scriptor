package com.ssblur.scriptor.events

import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.helpers.ParticleQueue
import com.ssblur.scriptor.helpers.ScriptionaryHelper
import com.ssblur.scriptor.item.ScriptorTags
import com.ssblur.scriptor.item.books.BookOfBooks
import com.ssblur.scriptor.network.server.ScriptorNetworkC2S
import com.ssblur.unfocused.event.client.ClientDisconnectEvent
import com.ssblur.unfocused.event.client.ClientLevelTickEvent
import com.ssblur.unfocused.event.client.ClientLoreEvent
import com.ssblur.unfocused.event.client.MouseScrollEvent
import com.ssblur.unfocused.extension.ItemStackExtension.matches
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand

object ScriptorClientEvents {
  fun init() {
    MouseScrollEvent.register {
      val player = it.minecraft.player
      if (player != null && player.isShiftKeyDown) {
        if (player.getItemInHand(InteractionHand.MAIN_HAND).item is BookOfBooks) {
          ScriptorNetworkC2S.scroll(ScriptorNetworkC2S.Scroll(InteractionHand.MAIN_HAND, it.amount.y))
          it.cancel()
        } else if (player.getItemInHand(InteractionHand.OFF_HAND).item is BookOfBooks) {
          ScriptorNetworkC2S.scroll(ScriptorNetworkC2S.Scroll(InteractionHand.OFF_HAND, it.amount.y))
          it.cancel()
        }
      }
    }
    ClientLevelTickEvent.Before.register { ParticleQueue.process() }
    ClientLoreEvent.register { (stack, lore, _, _) ->
      val charges = stack[ScriptorDataComponents.CHARGES] ?: 0
      if (charges > 0) {
        if (charges <= 10)
          lore.add(
            1,
            Component
              .translatable("enchantment.scriptor.charged")
              .append(" ")
              .append(Component.translatable("enchantment.level.$charges"))
          )
        else
          lore.add(
            1,
            Component
              .translatable("enchantment.scriptor.charged")
              .append(" ")
              .append("" + charges)
          )
      }

      stack[ScriptorDataComponents.SPELL]?.let {
        if(stack matches ScriptorTags.IGNORE_SPELL_COMPONENT) return@let

        lore.add(1, Component.translatable("lore.scriptor.inscribed")
          .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY))
        lore.add(2, Component.translatable("lore.scriptor.inscribed_2", it)
          .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY))
      }
    }

    ClientDisconnectEvent.register {
      ScriptionaryHelper.PLAYER_NOTES.clear()
      ScriptionaryHelper.PLAYER_OBSERVATIONS.clear()
    }

    ScriptorCooldownHud.init()
  }
}