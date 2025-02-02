package com.ssblur.scriptor.helpers.targetable

import com.ssblur.scriptor.config.ScriptorConfig
import net.minecraft.world.Container
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.animal.horse.AbstractHorse
import net.minecraft.world.entity.player.Player

class InventoryEntityTargetable @JvmOverloads constructor(entity: Entity?, var slot: Int, var isSelf: Boolean = false):
  EntityTargetable(entity!!), InventoryTargetable {
  override val container: Container?
    get() {
      if (targetEntity is Container) return targetEntity as Container
      if (targetEntity is Player && (ScriptorConfig.CAN_TARGET_PLAYER_INVENTORIES() || isSelf))
        return (targetEntity as Player).getInventory()
      if (targetEntity is AbstractHorse) return (targetEntity as AbstractHorse).inventory
      return null
    }

  override var targetedSlot: Int = slot
}
