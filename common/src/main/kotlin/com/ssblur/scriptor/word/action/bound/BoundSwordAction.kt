package com.ssblur.scriptor.word.action.bound

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.color.CustomColors.getColor
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.helpers.ItemTargetableHelper
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.item.ScriptorItems.BOUND_SWORD
import com.ssblur.scriptor.word.descriptor.duration.DurationDescriptor
import com.ssblur.scriptor.word.descriptor.power.StrengthDescriptor
import net.minecraft.core.component.DataComponents
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.DyedItemColor
import net.minecraft.world.item.component.ItemAttributeModifiers
import kotlin.math.floor

class BoundSwordAction: Action() {
  override fun apply(caster: Targetable, targetable: Targetable, descriptors: Array<Descriptor>) {
    var strength = 5.0
    var duration = 4.0
    for (d in descriptors) {
      if (d is StrengthDescriptor) strength += d.strengthModifier()
      if (d is DurationDescriptor) duration += d.durationModifier()
    }

    val itemStack = ItemStack(BOUND_SWORD.get())

    itemStack.set(DataComponents.DYED_COLOR, DyedItemColor(getColor(descriptors), false))
    itemStack.set(
      ScriptorDataComponents.EXPIRES, caster.level.gameTime + floor(duration * 80)
        .toLong()
    )

    val finalStrength = strength
    itemStack.update(
      DataComponents.ATTRIBUTE_MODIFIERS,
      ItemAttributeModifiers.EMPTY
    ) { modifiers: ItemAttributeModifiers ->
      modifiers.withModifierAdded(
        Attributes.ATTACK_DAMAGE,
        AttributeModifier(
          ScriptorMod.location("bound_sword"),
          finalStrength,
          AttributeModifier.Operation.ADD_VALUE
        ),
        EquipmentSlotGroup.MAINHAND
      )
    }

    ItemTargetableHelper.depositItemStack(targetable, itemStack)
  }

  override fun cost() = Cost(6.0, COSTTYPE.ADDITIVE)
}
