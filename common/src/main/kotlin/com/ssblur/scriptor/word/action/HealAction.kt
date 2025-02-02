package com.ssblur.scriptor.word.action

import com.ssblur.scriptor.ScriptorDamage.magic
import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.ItemTargetableHelper
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.ItemTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.descriptor.power.StrengthDescriptor
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import kotlin.math.max

class HealAction: Action() {
  override fun apply(caster: Targetable, targetable: Targetable, descriptors: Array<Descriptor>) {
    var strength = 2.0
    for (d in descriptors) {
      if (d is StrengthDescriptor) strength += d.strengthModifier()
    }

    strength = max(strength, 0.0)

    val itemTarget = ItemTargetableHelper.getTargetItemStack(
      targetable,
      false
    ) { !it.isEmpty && it.isDamageableItem }
    if (itemTarget.isDamageableItem) {
      itemTarget.damageValue = itemTarget.damageValue - Math.round(strength).toInt()
      return
    }

    if (targetable is ItemTargetable && targetable.shouldTargetItem()) {
      val item: ItemStack = targetable.targetItem
      if (!item.isEmpty) {
        if (item.isDamageableItem) {
          item.damageValue = item.damageValue - Math.round(strength).toInt()
          return
        }
      }
    }

    if (targetable is EntityTargetable) {
      val entity: Entity = targetable.targetEntity
      val source = if (caster is EntityTargetable) caster.targetEntity else entity
      if (entity is LivingEntity) if (entity.isInvertedHealAndHarm) entity.hurt(
        magic(source, source),
        strength.toFloat()
      )
      else entity.heal(strength.toFloat())
    }
  }

  override fun cost() = Cost(4.0, COSTTYPE.ADDITIVE)
}
