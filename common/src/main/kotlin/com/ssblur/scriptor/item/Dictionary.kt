package com.ssblur.scriptor.item

import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.screen.menu.DictionaryMenu
import com.ssblur.unfocused.menu.SimpleMenuProvider
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class Dictionary(properties: Properties) : Item(properties) {
  override fun appendHoverText(
    itemStack: ItemStack,
    level: TooltipContext,
    list: MutableList<Component>,
    tooltipFlag: TooltipFlag
  ) {
    super.appendHoverText(itemStack, level, list, tooltipFlag)

    list.add(Component.translatable("lore.scriptor.dictionary").withStyle(ChatFormatting.GRAY))
    list.add(Component.translatable(
      "lore.scriptor.dictionary_3",
      itemStack[ScriptorDataComponents.DICTIONARY_DATA]?.values?.size ?: 0
    ).withStyle(ChatFormatting.GRAY))
    list.add(Component.translatable("lore.scriptor.dictionary_2").withStyle(ChatFormatting.GRAY))
  }

  override fun use(
    level: Level,
    player: Player,
    interactionHand: InteractionHand
  ): InteractionResultHolder<ItemStack?>? {
    if(!level.isClientSide)
      player.openMenu(SimpleMenuProvider { i, inventory, _ ->
        val menu = DictionaryMenu(i, inventory)
        menu.dictionary = player.getItemInHand(interactionHand)
        menu
      })
    return InteractionResultHolder.success(player.getItemInHand(interactionHand))
  }
}