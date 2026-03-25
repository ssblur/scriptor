package com.ssblur.scriptor.item.books

import com.ssblur.scriptor.helpers.ScriptionaryHelper.awardNote
import com.ssblur.scriptor.resources.Notes
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class AncientNote(properties: Properties, var tier: Int): Item(properties) {
  override fun appendHoverText(
    itemStack: ItemStack,
    level: TooltipContext,
    list: MutableList<Component>,
    tooltipFlag: TooltipFlag
  ) {
    super.appendHoverText(itemStack, level, list, tooltipFlag)
    list.add(Component.translatable("extra.scriptor.note_description").withStyle(ChatFormatting.GRAY))
    list.add(Component.translatable("extra.scriptor.note_tier", tier).withStyle(ChatFormatting.GRAY))
  }

  override fun use(
    level: Level,
    player: Player,
    interactionHand: InteractionHand
  ): InteractionResultHolder<ItemStack> {
    val result = super.use(level, player, interactionHand)

    if (!level.isClientSide) {
      player.sendSystemMessage(Component.translatable("extra.scriptor.note_use"))
      if(!player.isCreative) player.cooldowns.addCooldown(this, 20)

      player.awardNote(Notes.getRandomNote(tier, player))

      return InteractionResultHolder.consume(player.getItemInHand(interactionHand))
    }

    return result
  }
}
