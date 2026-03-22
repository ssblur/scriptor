package com.ssblur.scriptor.events

import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.helpers.ParticleQueue
import com.ssblur.scriptor.item.books.BookOfBooks
import com.ssblur.scriptor.network.server.ScriptorNetworkC2S
import com.ssblur.unfocused.event.client.ClientLevelTickEvent
import com.ssblur.unfocused.event.client.ClientLoreEvent
import com.ssblur.unfocused.event.client.MouseScrollEvent
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
            Component
              .translatable("enchantment.scriptor.charged")
              .append(" ")
              .append(Component.translatable("enchantment.level.$charges"))
          )
        else
          lore.add(
            Component
              .translatable("enchantment.scriptor.charged")
              .append(" ")
              .append("" + charges)
          )
      }

      stack[ScriptorDataComponents.SPELL]?.let {
        lore.add(Component.translatable("lore.scriptor.inscribed"))
        lore.add(Component.translatable("lore.scriptor.inscribed_2", it))
      }
    }

    ScriptorCooldownHud.init()
  }
}