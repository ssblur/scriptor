package com.ssblur.scriptor.events

import com.ssblur.scriptor.config.ScriptorConfig
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.helpers.SpellbookHelper
import com.ssblur.scriptor.item.ScriptorTags
import com.ssblur.unfocused.extension.ItemStackExtension.matches
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

object ItemRightClicked {
  fun itemRightClicked(entity: Player, item: ItemStack): Boolean {
    if(item[ScriptorDataComponents.SPELL] != null && !(item matches ScriptorTags.IGNORE_SPELL_COMPONENT)) {
      return SpellbookHelper.castFromItem(
        item,
        entity,
        maxCost = ScriptorConfig.ITEM_MAX_COST(),
        costMultiplier = ScriptorConfig.ITEM_COOLDOWN_MULTIPLIER()
      )
    }
    return false
  }
}