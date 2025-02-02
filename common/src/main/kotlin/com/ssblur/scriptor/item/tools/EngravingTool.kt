package com.ssblur.scriptor.item.tools

import com.ssblur.scriptor.network.server.ScriptorNetworkC2S
import com.ssblur.scriptor.network.server.ScriptorNetworkC2S.SendChalk
import net.minecraft.client.Minecraft
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult

class EngravingTool(properties: Properties): Chalk(properties) {
  override fun use(
    level: Level,
    player: Player,
    interactionHand: InteractionHand
  ): InteractionResultHolder<ItemStack> {
    if (level.isClientSide) {
      val client = Minecraft.getInstance()
      val hit = client.hitResult
      if (hit is BlockHitResult)
        ScriptorNetworkC2S.sendChalk(SendChalk(hit, true))
    }

    return InteractionResultHolder.success(player.getItemInHand(interactionHand))
  }
}
