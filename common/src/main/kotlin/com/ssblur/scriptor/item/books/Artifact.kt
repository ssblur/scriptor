package com.ssblur.scriptor.item.books

import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.data.saved_data.DictionarySavedData.Companion.computeIfAbsent
import com.ssblur.scriptor.helpers.targetable.SpellbookTargetable
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class Artifact(properties: Properties, val lore: String = "lore.scriptor.artifact_1"): Item(properties.stacksTo(1)) {
  init {
    ARTIFACTS.add(this)
  }

  override fun appendHoverText(
    itemStack: ItemStack,
    level: TooltipContext,
    list: MutableList<Component>,
    tooltipFlag: TooltipFlag
  ) {
    super.appendHoverText(itemStack, level, list, tooltipFlag)

    list.add(Component.translatable(lore).withStyle(ChatFormatting.GRAY))

    val text = itemStack.get(ScriptorDataComponents.SPELL)
    if (text != null) list.add(Component.translatable("lore.scriptor.artifact_2", text))
  }

  override fun use(
    level: Level,
    player: Player,
    interactionHand: InteractionHand
  ): InteractionResultHolder<ItemStack> {
    val result = super.use(level, player, interactionHand)

    val itemStack = player.getItemInHand(interactionHand)
    val text = itemStack.get(ScriptorDataComponents.SPELL)
    if (text == null || level !is ServerLevel) return result

    level.playSound(
      null,
      player.blockPosition(),
      SoundEvents.EVOKER_CAST_SPELL,
      SoundSource.PLAYERS,
      0.4f,
      level.getRandom().nextFloat() * 1.2f + 0.6f
    )

    val spell = computeIfAbsent(level).parse(text)
    if (spell != null) {
      spell.cast(
        SpellbookTargetable(
          itemStack,
          player,
          player.inventory.selected
        ).withTargetItem(false)
      )
      if (!player.isCreative) for (artifact in ARTIFACTS) player.cooldowns.addCooldown(
        artifact,
        Math.round(spell.cost() * 2).toInt()
      )
      return InteractionResultHolder.pass(itemStack)
    }
    return InteractionResultHolder.fail(itemStack)
  }

  companion object {
    var ARTIFACTS: MutableList<Item> = ArrayList()
  }
}
