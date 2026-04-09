package com.ssblur.scriptor.recipe

import com.ssblur.scriptor.data.components.ScriptorDataComponents
import net.minecraft.world.item.ItemStack

object GrindstoneRecipeHandler {
  fun handle(input: ItemStack): ItemStack? {
    if(input[ScriptorDataComponents.SPELL] != null) {
      val copy = input.copy()
      copy[ScriptorDataComponents.SPELL] = null
      return copy
    }
    return null
  }
}