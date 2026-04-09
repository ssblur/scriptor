package com.ssblur.scriptor.recipe

import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.helpers.LimitedBookSerializer
import com.ssblur.scriptor.item.ScriptorTags
import com.ssblur.unfocused.extension.ItemStackExtension.matches
import net.minecraft.core.component.DataComponents
import net.minecraft.world.Container
import net.minecraft.world.inventory.DataSlot

object AnvilRecipeHandler {
  fun handle(input: Container, results: Container, cost: DataSlot) {
    if(!input.getItem(0).isEmpty && input.getItem(1) matches ScriptorTags.SPELLBOOKS) {
      if(input.getItem(0)[ScriptorDataComponents.SPELL] != null) return
      input.getItem(1)[DataComponents.WRITTEN_BOOK_CONTENT]?.let {
        val item = (if(results.getItem(0).isEmpty) input.getItem(0) else results.getItem(0)).copy()
        item[ScriptorDataComponents.SPELL] = LimitedBookSerializer.decodeText(it)
        item[DataComponents.ENCHANTMENT_GLINT_OVERRIDE] = true
        results.setItem(0, item)
        cost.set(cost.get() + 5)
      }
    }
  }
}