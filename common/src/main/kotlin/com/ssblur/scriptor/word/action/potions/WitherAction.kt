package com.ssblur.scriptor.word.action.potions

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.network.client.ParticleNetwork
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.level.block.CropBlock
import java.util.*
import kotlin.math.max

class WitherAction: PotionAction(MobEffects.WITHER, 30.0, 1.0 / 3.0, Cost(12.0, COSTTYPE.ADDITIVE)) {
  override fun applyToPosition(
    caster: Targetable?,
    targetable: Targetable?,
    descriptors: Array<Descriptor>?,
    strength: Double,
    duration: Double
  ) {
    var playAnimation = false

    val level = targetable!!.level
    val pos = targetable.offsetBlockPos

    if (level.getBlockState(pos).hasProperty(CropBlock.AGE)) {
      val state = level.getBlockState(pos)
      var age = state.getValue(CropBlock.AGE)
      if (age > 0) {
        playAnimation = true
        var i = 0
        while (i < strength + 1) {
          age = max((age - (RANDOM.nextInt(2) + 1)).toDouble(), 0.0)
            .toInt()
          i++
        }
        level.setBlockAndUpdate(pos, state.setValue(CropBlock.AGE, age))
      }
    }

    if (playAnimation) ParticleNetwork.wither(level, pos)
  }

  companion object {
    val RANDOM: Random = Random()
  }
}
