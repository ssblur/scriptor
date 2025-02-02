package com.ssblur.scriptor.helpers.targetable

import com.ssblur.scriptor.mixin.BaseContainerBlockEntityAccessor
import net.minecraft.core.BlockPos
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity

class ContainerTargetable(level: Level, pos: BlockPos, var slot: Int): Targetable(level, pos), InventoryTargetable {
  override val container: Container?
    get() {
      val blockEntity = level.getBlockEntity(targetBlockPos)
      if (blockEntity is Container) {
        if (blockEntity is BaseContainerBlockEntity) {
          if ((blockEntity as BaseContainerBlockEntityAccessor).lockKey.unlocksWith(ItemStack.EMPTY))
            return level.getBlockEntity(targetBlockPos) as Container
          return null
        }
        return level.getBlockEntity(targetBlockPos) as Container
      }
      return null
    }

  override var targetedSlot = slot
}
