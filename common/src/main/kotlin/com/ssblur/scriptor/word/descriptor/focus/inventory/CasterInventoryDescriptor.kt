package com.ssblur.scriptor.word.descriptor.focus.inventory

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.ContainerTargetable
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.InventoryEntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.descriptor.focus.FocusDescriptor
import net.minecraft.world.Container

class CasterInventoryDescriptor : Descriptor(), FocusDescriptor {
    override fun cost(): Cost {
        return Cost(0.0, COSTTYPE.ADDITIVE)
    }

    override fun modifyFocus(targetable: Targetable): Targetable {
        if (targetable is ContainerTargetable) return targetable
        if (targetable is EntityTargetable) return InventoryEntityTargetable(targetable.targetEntity, 0)
        if (targetable.level.getBlockEntity(targetable.targetBlockPos) is Container)
            return ContainerTargetable(targetable.level, targetable.targetBlockPos, 0)
        return targetable
    }
}
