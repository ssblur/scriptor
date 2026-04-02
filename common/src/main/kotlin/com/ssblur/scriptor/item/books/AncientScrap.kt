package com.ssblur.scriptor.item.books

import com.ssblur.scriptor.data.components.DictionaryData
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.helpers.PlayerItemHelper.addOrDropItem
import com.ssblur.scriptor.item.ScriptorItems
import com.ssblur.scriptor.resources.Scraps
import com.ssblur.unfocused.extension.ItemStackExtension.matches
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class AncientScrap(properties: Properties, var tier: Int): Item(properties) {
  override fun appendHoverText(
    itemStack: ItemStack,
    level: TooltipContext,
    list: MutableList<Component>,
    tooltipFlag: TooltipFlag
  ) {
    super.appendHoverText(itemStack, level, list, tooltipFlag)
    list.add(Component.translatable("extra.scriptor.scrap_description").withStyle(ChatFormatting.GRAY))
    list.add(Component.translatable("extra.scriptor.scrap_tier", tier).withStyle(ChatFormatting.GRAY))
  }

  override fun use(
    level: Level,
    player: Player,
    interactionHand: InteractionHand
  ): InteractionResultHolder<ItemStack> {
    val result = super.use(level, player, interactionHand)

    if (!level.isClientSide) {
      player.sendSystemMessage(Component.translatable("extra.scriptor.scrap_use"))
      if(!player.isCreative) player.cooldowns.addCooldown(this, 20)

      // Generate and distribute scrap
      val scrap = Scraps.getRandomScrapItem(tier, player)

      player.sendSystemMessage(Component.translatable("extra.scriptor.scrap_get"))

      if(!player.isCreative) player.getItemInHand(interactionHand).shrink(1)

      if(player.inventory.contains { it matches ScriptorItems.DICTIONARY.get() }) {
        val item = player.inventory.items.firstOrNull{
          it matches ScriptorItems.DICTIONARY.get()
              && ( it[ScriptorDataComponents.DICTIONARY_DATA]?.values?.any { entry ->
                entry[0] == scrap[DataComponents.ITEM_NAME]?.string
              }) != true
        }

        item?.let {
          item[ScriptorDataComponents.DICTIONARY_DATA] = item[ScriptorDataComponents.DICTIONARY_DATA]
            ?.withWord(scrap[DataComponents.ITEM_NAME]!!.string, scrap[ScriptorDataComponents.SPELL])
            ?: DictionaryData(listOf(listOf(scrap[DataComponents.ITEM_NAME]!!.string, scrap[ScriptorDataComponents.SPELL]!!)))
          player.sendSystemMessage(Component.translatable("extra.scriptor.scrap_stuff"))
          return InteractionResultHolder.consume(player.getItemInHand(interactionHand))
        }
      }

      player.addOrDropItem(scrap)

      return InteractionResultHolder.consume(player.getItemInHand(interactionHand))
    }

    return result
  }
}
