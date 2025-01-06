package com.ssblur.scriptor.events

import com.ssblur.scriptor.helpers.ParticleQueue
import com.ssblur.scriptor.item.books.BookOfBooks
import com.ssblur.scriptor.network.server.ScriptorNetworkC2S
import com.ssblur.unfocused.event.client.ClientLevelTickEvent
import com.ssblur.unfocused.event.client.MouseScrollEvent
import net.minecraft.world.InteractionHand

object ScriptorClientEvents {
    init {
        MouseScrollEvent.register{
            val player = it.minecraft.player
            if(player != null && player.isShiftKeyDown) {
                if(player.getItemInHand(InteractionHand.MAIN_HAND).item is BookOfBooks) {
                    ScriptorNetworkC2S.scroll(ScriptorNetworkC2S.Scroll(InteractionHand.MAIN_HAND, it.amount.y))
                    it.cancel()
                } else if (player.getItemInHand(InteractionHand.OFF_HAND).item is BookOfBooks) {
                    ScriptorNetworkC2S.scroll(ScriptorNetworkC2S.Scroll(InteractionHand.OFF_HAND,  it.amount.y))
                    it.cancel()
                }
            }
        }
        ClientLevelTickEvent.Before.register{ ParticleQueue.process() }
    }
}