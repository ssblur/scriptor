package com.ssblur.scriptor.item.tools

import com.ssblur.scriptor.events.network.server.ChalkNetwork
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class EngravingTool(properties: Properties) : Chalk(properties) {
    override fun use(
        level: Level,
        player: Player,
        interactionHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {
        if (level.isClientSide) ChalkNetwork.sendChalkMessage(true)

        return InteractionResultHolder.success(player.getItemInHand(interactionHand))
    }
}
