package com.ssblur.scriptor.helpers

import com.ssblur.scriptor.helpers.targetable.InventoryTargetable
import com.ssblur.scriptor.helpers.targetable.ItemTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import java.util.function.Predicate

@Suppress("unused")
object ItemTargetableHelper {
    fun getTargetItemStack(targetable: Targetable, aggressive: Boolean): ItemStack {
        if (targetable is ItemTargetable && (targetable.shouldTargetItem() || aggressive)) return targetable.targetItem
        else if (targetable is InventoryTargetable) if (targetable.container != null) {
            val slot: Int = if (targetable.shouldIgnoreTargetedSlot()) targetable.firstFilledSlot
            else targetable.targetedSlot
            return targetable.container!!.getItem(slot)
        }
        return ItemStack.EMPTY
    }

    fun getTargetItemStack(targetable: Targetable, aggressive: Boolean, condition: Predicate<ItemStack>): ItemStack {
        if (targetable is ItemTargetable && (targetable.shouldTargetItem() || aggressive)) {
            if (condition.test(targetable.targetItem)) return targetable.targetItem
        } else if (targetable is InventoryTargetable) if (targetable.container != null) {
            val slot: Int = if (targetable.shouldIgnoreTargetedSlot()) targetable.getFirstMatchingSlot(condition)
            else targetable.targetedSlot
            if (slot >= 0 && condition.test(
                    targetable.container!!.getItem(slot)
                )
            ) return targetable.container!!.getItem(slot)
        }
        return ItemStack.EMPTY
    }

    fun getTargetItemStack(targetable: Targetable): ItemStack {
        return getTargetItemStack(targetable, false)
    }

    fun getTargetItemStackAggressively(targetable: Targetable): ItemStack {
        return getTargetItemStack(targetable, true)
    }

    fun depositItemStack(targetable: Targetable, itemStack: ItemStack) {
        if (targetable is InventoryTargetable) {
            if (targetable.container != null) {
                var slot: Int = targetable.getFirstMatchingSlotNotEmpty(itemStack)
                if (slot >= 0) {
                    val item: ItemStack = targetable.container!!.getItem(slot)
                    if (item.count + itemStack.count < item.maxStackSize) {
                        item.grow(itemStack.count)
                        return
                    } else {
                        val diff = item.maxStackSize - item.count
                        item.grow(diff)
                        itemStack.shrink(diff)
                    }
                }

                slot = targetable.getFirstMatchingSlot(Predicate { obj: ItemStack -> obj.isEmpty })
                if (slot >= 0) {
                    targetable.container!!.setItem(slot, itemStack.copy())
                    return
                }
            }
        }

        if (targetable is ItemTargetable && targetable.targetEntity is Player)
            if ((targetable.targetEntity as Player).addItem(itemStack)) return

        val pos = targetable.targetPos
        val entity = ItemEntity(
            targetable.level,
            pos.x(),
            pos.y() + 1,
            pos.z(),
            itemStack
        )
        targetable.level.addFreshEntity(entity)
    }
}
