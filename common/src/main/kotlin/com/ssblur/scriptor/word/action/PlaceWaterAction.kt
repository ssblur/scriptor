package com.ssblur.scriptor.word.action

import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.Spell
import net.minecraft.world.level.block.Blocks

class PlaceWaterAction : Action() {
  override fun cost() = Cost(1.5, COSTTYPE.ADDITIVE)

  override fun apply(
    caster: Targetable,
    targetable: Targetable,
    descriptors: Array<Descriptor>,
    spellData: MutableList<String>
  ) {
    val pos = targetable.targetBlockPos
    val level = targetable.level

    if (!level.getBlockState(pos).canBeReplaced()) return

    if(level.dimensionType().ultraWarm) { // Nether / Nether-likes
      Spell.fizzle(level, pos)
      return
    }

    level.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState())
  }
}
