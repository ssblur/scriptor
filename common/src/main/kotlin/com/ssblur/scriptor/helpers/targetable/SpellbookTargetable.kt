package com.ssblur.scriptor.helpers.targetable

import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

class SpellbookTargetable(itemStack: ItemStack?, entity: Player?, var slot: Int) : ItemTargetable(
    itemStack!!, entity
) {
    val player: Player?
        get() {
            if (targetEntity is Player) return targetEntity as Player
            return null
        }
}
