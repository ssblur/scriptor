package com.ssblur.scriptor.word.action

import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.ItemTargetableHelper
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.descriptor.power.StrengthDescriptor
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import java.util.stream.Collectors

class BreakBlockAction: Action() {
  override fun apply(caster: Targetable, targetable: Targetable, descriptors: Array<Descriptor>) {
    if (targetable.level.isClientSide) return
    var strength = 1.0
    for (d in descriptors) {
      if (d is StrengthDescriptor) strength += d.strengthModifier()
    }

    val itemTarget = ItemTargetableHelper.getTargetItemStack(
      targetable,
      false
    ) { !it.isEmpty && it.isDamageableItem }
    if (!itemTarget.isEmpty) if (itemTarget.isDamageableItem) {
      itemTarget.damageValue = itemTarget.damageValue + Math.round(strength).toInt()
      return
    }

    val pos = targetable.offsetBlockPos
    val state = targetable.level.getBlockState(pos)
    if (state.block.defaultDestroyTime() < 0) return
    val level = targetable.level

    @Suppress("redundantsuppression", "deprecation")
    val tags = state.block.builtInRegistryHolder().tags().collect(Collectors.toSet())
    var neededStrength = 0
    for (tag in tags) {
      if (toolLevelsList.containsKey(tag.location().toString()))
        neededStrength = toolLevelsList[tag.location().toString()]!!
    }
    if (strength > neededStrength) if (caster is EntityTargetable) {
      if (caster.targetEntity is Player)
        state.block.playerDestroy(
          level,
          (caster.targetEntity as Player),
          pos,
          state,
          level.getBlockEntity(pos),
          ItemStack(Items.NETHERITE_PICKAXE)
        )
      level.destroyBlock(pos, false, caster.targetEntity, Math.round(strength).toInt())
    } else {
      level.destroyBlock(pos, true, null, Math.round(strength).toInt())
    }
  }

  override fun cost() = Cost(1.0, COSTTYPE.ADDITIVE)

  companion object {
    var toolLevelsList: HashMap<String, Int> = HashMap()

    init {
      toolLevelsList["minecraft:needs_stone_tool"] = 1
      toolLevelsList["minecraft:needs_iron_tool"] = 2
      toolLevelsList["minecraft:needs_diamond_tool"] = 3
      toolLevelsList["minecraft:needs_netherite_tool"] = 4
    }
  }
}
