package com.ssblur.scriptor.word.descriptor.discount

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.InventoryTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.descriptor.AfterCastDescriptor
import com.ssblur.scriptor.word.descriptor.CastDescriptor
import net.minecraft.world.Container
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import kotlin.math.min

class ReagentDescriptor(var item: Item, var cost: Int) : Descriptor(), CastDescriptor, AfterCastDescriptor {
    override fun cost(): Cost {
        return Cost.add(-cost.toDouble())
    }

    override fun cannotCast(caster: Targetable?): Boolean {
        val c = runningTotals.getOrDefault(item, 0) + 1
        runningTotals[item] = c
        var container: Container? = null

        if (caster is InventoryTargetable && caster.container != null) container = caster.container
        else if (caster is EntityTargetable && caster.targetEntity is Player) container = (caster.targetEntity as Player).inventory

        if (container != null) if (container.countItem(item) >= c) return false

        runningTotals.remove(item)
        return true
    }

    override fun allowsDuplicates(): Boolean {
        return true
    }

    override fun afterCast(caster: Targetable?) {
        var c = runningTotals.getOrDefault(item, 0)
        runningTotals.remove(item)

        val container = if (caster is InventoryTargetable && caster.container != null) caster.container
        else if (caster is EntityTargetable && caster.targetEntity is Player) (caster.targetEntity as Player).inventory
        else return

        for (i in 0 until container!!.containerSize) if (container.getItem(i).item === item) {
            val maxSize = min(container.getItem(i).count.toDouble(), c.toDouble()).toInt()
            c -= maxSize
            container.getItem(i).shrink(maxSize)
            if (c <= 0) return
        }
    }

    companion object {
        var runningTotals: HashMap<Item, Int> = HashMap()
    }
}
