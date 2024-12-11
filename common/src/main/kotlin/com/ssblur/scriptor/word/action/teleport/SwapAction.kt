package com.ssblur.scriptor.word.action.teleport

import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.InventoryTargetable
import com.ssblur.scriptor.helpers.targetable.ItemTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.TicketType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.RelativeMovement
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level

open class SwapAction : Action() {
    override fun apply(caster: Targetable, targetable: Targetable, descriptors: Array<Descriptor>) {
        if (targetable.level.isClientSide) return

        teleport(caster, targetable)
        teleport(targetable, caster)
    }

    protected fun teleport(from: Targetable, to: Targetable) {
        if ((from is InventoryTargetable
                    && to is InventoryTargetable) && from.container != null && to.container != null
        ) {
            val itemStack: ItemStack =
                if (from.shouldIgnoreTargetedSlot()) from.container!!.getItem(from.targetedSlot)
                else from.container!!.getItem(from.firstFilledSlot)
            if (to.container!!.canPlaceItem(to.targetedSlot, itemStack)) {
                val newItemStack = itemStack.copy()
                newItemStack.count = 1
                val slot: Int = if (to.shouldIgnoreTargetedSlot()) to.getFirstMatchingSlot(newItemStack)
                else to.targetedSlot
                val itemStack2: ItemStack = to.container!!.getItem(slot)
                if (itemStack2.isEmpty) {
                    itemStack.shrink(1)
                    to.container!!.setItem(slot, newItemStack)
                } else if (ItemStack.isSameItemSameComponents(itemStack2, itemStack)) {
                    itemStack.shrink(1)
                    itemStack2.grow(1)
                }
            }
        } else if ((from is ItemTargetable
                    && to is InventoryTargetable) && to.container != null
        ) {
            val itemStack: ItemStack = from.targetItem
            if (to.container!!.canPlaceItem(to.targetedSlot, itemStack)) {
                val newItemStack = itemStack.copy()
                newItemStack.count = 1
                val slot: Int = if (to.shouldIgnoreTargetedSlot()) to.getFirstMatchingSlot(newItemStack)
                else to.targetedSlot
                val itemStack2: ItemStack = to.container!!.getItem(slot)
                if (itemStack2.isEmpty) {
                    itemStack.shrink(1)
                    to.container!!.setItem(slot, newItemStack)
                } else if (ItemStack.isSameItemSameComponents(itemStack2, itemStack)) {
                    itemStack.shrink(1)
                    itemStack2.grow(1)
                }
            }
        } else if (from is ItemTargetable && from.shouldTargetItem()) {
            val itemStack: ItemStack = from.targetItem
            itemStack.shrink(1)
            val newItemStack = itemStack.copy()
            newItemStack.count = 1
            val entity = ItemEntity(to.level, to.targetPos.x(), to.targetPos.y() + 1, to.targetPos.z(), newItemStack)
            to.level.addFreshEntity(entity)
        } else if (from is EntityTargetable && from.targetEntity is LivingEntity) {
            val living = from.targetEntity as LivingEntity
            val level: Level = living.level()
            if (level !== to.level) {
                val toLevel = to.level as ServerLevel
                toLevel.chunkSource.addRegionTicket<Int>(
                    TicketType.POST_TELEPORT,
                    ChunkPos(to.targetBlockPos),
                    1,
                    living.getId()
                )
                living.teleportTo(
                    toLevel,
                    to.targetPos.x,
                    to.targetPos.y,
                    to.targetPos.z,
                    setOf<RelativeMovement>(),
                    living.getYRot(),
                    living.getXRot()
                )
                living.stopRiding()
                if (living is Player) {
                    if (living.isSleeping()) living.stopSleepInBed(true, true)
                    living.onUpdateAbilities()
                }
            }

            if (living is PathfinderMob) living.getNavigation().stop()

            living.teleportTo(to.targetPos.x, to.targetPos.y, to.targetPos.z)
            living.setDeltaMovement(0.0, 0.0, 0.0)
            living.resetFallDistance()
        }
    }

    override fun cost(): Cost {
        return Cost(6.0, COSTTYPE.ADDITIVE)
    }
}
