package com.ssblur.scriptor.item.books

import com.ssblur.scriptor.advancement.ScriptorAdvancements
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.data.saved_data.DictionarySavedData.Companion.computeIfAbsent
import com.ssblur.scriptor.helpers.LimitedBookSerializer
import com.ssblur.scriptor.helpers.SpellbookHelper
import com.ssblur.scriptor.resources.Tomes
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class AncientSpellbook(properties: Properties, var tier: Int): Item(properties) {
  override fun appendHoverText(
    itemStack: ItemStack,
    level: TooltipContext,
    list: MutableList<Component>,
    tooltipFlag: TooltipFlag
  ) {
    super.appendHoverText(itemStack, level, list, tooltipFlag)
    list.add(Component.translatable("extra.scriptor.tome_description"))
    list.add(Component.translatable("extra.scriptor.tome_tier", tier))
  }

  override fun use(
    level: Level,
    player: Player,
    interactionHand: InteractionHand
  ): InteractionResultHolder<ItemStack> {
    val result = super.use(level, player, interactionHand)

    if (!level.isClientSide) {
      val server = level as ServerLevel

      player.sendSystemMessage(Component.translatable("extra.scriptor.tome_use"))
      player.cooldowns.addCooldown(this, 20)

      val resource = Tomes.getRandomTome(tier, player)
      if (resource.getSpell().spells.size > 1) ScriptorAdvancements.COMPLEX_SPELL.get().trigger(player as ServerPlayer)

      val spell = resource.getSpell()
      val sentence = computeIfAbsent(server).generate(spell)

      val spellbook =
        LimitedBookSerializer.createSpellbook(resource.author, resource.name, sentence, resource.item)
      spellbook[ScriptorDataComponents.INVENTORY_CAST] = SpellbookHelper.isInventoryCaster(spellbook, level)
      if (!player.addItem(spellbook)) {
        val entity = ItemEntity(
          level,
          player.x,
          player.y + 1,
          player.z + 1,
          spellbook
        )
        level.addFreshEntity(entity)
      }
      player.sendSystemMessage(
        Component.translatable(
          "extra.scriptor.spell_get",
          Component.translatable(resource.name)
        )
      )
      player.getItemInHand(interactionHand).shrink(1)
      return InteractionResultHolder.consume(player.getItemInHand(interactionHand))
    }

    return result
  }
}
