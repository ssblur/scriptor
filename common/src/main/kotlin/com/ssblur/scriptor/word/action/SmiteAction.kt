package com.ssblur.scriptor.word.action

import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.helpers.ItemTargetableHelper
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.descriptor.power.StrengthDescriptor
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LightningBolt
import kotlin.math.floor

class SmiteAction: Action() {
  override fun apply(caster: Targetable, targetable: Targetable, descriptors: Array<Descriptor>) {
    var strength = 1.0
    for (d in descriptors) {
      if (d is StrengthDescriptor) strength += d.strengthModifier()
    }

    val itemTarget = ItemTargetableHelper.getTargetItemStack(targetable)
    if (!itemTarget.isEmpty) {
      itemTarget.set(ScriptorDataComponents.CHARGES, floor(strength).toInt())
      return
    }

    val level = targetable.level as ServerLevel
    val bolt = LightningBolt(EntityType.LIGHTNING_BOLT, level)
    if (caster is EntityTargetable && caster.targetEntity is ServerPlayer) bolt.cause =
      (caster.targetEntity as ServerPlayer)
    bolt.setPos(targetable.targetPos)
    level.addFreshEntity(bolt)
  }

  override fun cost() = Cost(12.0, COSTTYPE.ADDITIVE)
}
