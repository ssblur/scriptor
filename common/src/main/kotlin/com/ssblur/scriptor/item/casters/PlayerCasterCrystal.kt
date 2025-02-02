package com.ssblur.scriptor.item.casters

import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import java.util.*

class PlayerCasterCrystal(properties: Properties): CasterCrystal(properties) {
  override fun getTargetables(itemStack: ItemStack?, level: Level?): List<Targetable?>? {
    val uuid = itemStack!!.get(ScriptorDataComponents.PLAYER_FOCUS)
    if (uuid != null) {
      val player = level!!.getPlayerByUUID(UUID.fromString(uuid))
      if (player != null) return java.util.List.of<Targetable?>(
        EntityTargetable(player)
      )
    }
    return listOf<Targetable>()
  }

  override fun appendHoverText(
    itemStack: ItemStack,
    context: TooltipContext,
    list: MutableList<Component>,
    tooltipFlag: TooltipFlag
  ) {
    super.appendHoverText(itemStack, context, list, tooltipFlag)

    list.add(Component.translatable("lore.scriptor.player_crystal_1").withStyle(ChatFormatting.GRAY))

    val name = itemStack.get(ScriptorDataComponents.PLAYER_NAME)
    if (name != null) {
      list.add(
        Component.translatable(
          "lore.scriptor.player_crystal_2",
          name
        ).withStyle(ChatFormatting.GRAY)
      )
      list.add(Component.translatable("lore.scriptor.crystal_reset").withStyle(ChatFormatting.GRAY))
    }

    list.add(Component.translatable("lore.scriptor.crystal_focus").withStyle(ChatFormatting.GRAY))
  }

  override fun use(
    level: Level,
    player: Player,
    interactionHand: InteractionHand
  ): InteractionResultHolder<ItemStack> {
    val result = super.use(level, player, interactionHand)

    if (!level.isClientSide) {
      val itemStack = player.getItemInHand(interactionHand)
      itemStack.set(ScriptorDataComponents.PLAYER_FOCUS, player.stringUUID)
      itemStack.set(ScriptorDataComponents.PLAYER_NAME, player.displayName!!.string)
    }

    return result
  }
}
