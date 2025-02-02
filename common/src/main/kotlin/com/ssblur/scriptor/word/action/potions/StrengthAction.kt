package com.ssblur.scriptor.word.action.potions

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.Targetable
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.item.BoneMealItem
import net.minecraft.world.item.ItemStack

class StrengthAction: PotionAction(MobEffects.DAMAGE_BOOST, 40.0, 1.0 / 3.0, Cost(9.0, COSTTYPE.ADDITIVE)) {
  override fun applyToPosition(
    caster: Targetable?,
    targetable: Targetable?,
    descriptors: Array<Descriptor>?,
    strength: Double,
    duration: Double
  ) {
    var playBoneMealAnimation = false

    val level = targetable!!.level
    val pos = targetable.offsetBlockPos

    var i = 0
    while (i < strength + 1) {
      if (BoneMealItem.growCrop(ItemStack.EMPTY, level, pos)
        || BoneMealItem.growWaterPlant(ItemStack.EMPTY, level, pos, targetable.facing.opposite)
      ) playBoneMealAnimation = true
      i++
    }

    if (playBoneMealAnimation) level.levelEvent(1505, pos, 0)
  }
}
