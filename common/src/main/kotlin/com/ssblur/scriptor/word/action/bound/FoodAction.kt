package com.ssblur.scriptor.word.action.bound

import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.color.CustomColors.getColor
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.helpers.ItemTargetableHelper
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.item.ScriptorItems
import com.ssblur.scriptor.word.descriptor.duration.DurationDescriptor
import com.ssblur.scriptor.word.descriptor.power.StrengthDescriptor
import net.minecraft.core.component.DataComponents
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.DyedItemColor
import kotlin.math.floor
import kotlin.math.roundToInt

class FoodAction(): Action() {
  override fun apply(caster: Targetable, targetable: Targetable, descriptors: Array<Descriptor>) {
    var strength = 3
    var saturation = 2.0f
    for (d in descriptors) {
      if (d is StrengthDescriptor) strength += d.strengthModifier().roundToInt()
      if (d is DurationDescriptor) saturation += d.durationModifier().toFloat()
    }

    val itemStack = ItemStack(ScriptorItems.ETHEREAL_FIG.get())
    itemStack[DataComponents.DYED_COLOR] = DyedItemColor(getColor(descriptors), false)
    itemStack[ScriptorDataComponents.EXPIRES] = caster.level.gameTime + floor(saturation * 80).toLong()
    itemStack[DataComponents.FOOD] = FoodProperties.Builder()
      .nutrition(strength)
      .saturationModifier(saturation)
      .alwaysEdible()
      .build()

    ItemTargetableHelper.depositItemStack(targetable, itemStack)
  }

  override fun cost() = Cost(3.0, COSTTYPE.ADDITIVE)
}
