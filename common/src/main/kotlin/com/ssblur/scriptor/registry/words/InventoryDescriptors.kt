package com.ssblur.scriptor.registry.words

import com.ssblur.scriptor.registry.words.WordRegistry.register
import com.ssblur.scriptor.word.descriptor.focus.inventory.CasterFirstEmptySlotDescriptor
import com.ssblur.scriptor.word.descriptor.focus.inventory.CasterFirstFilledSlotDescriptor
import com.ssblur.scriptor.word.descriptor.focus.inventory.CasterIgnoreTargetedSlotDescriptor
import com.ssblur.scriptor.word.descriptor.focus.inventory.CasterInventoryDescriptor
import com.ssblur.scriptor.word.descriptor.target.inventory.FirstEmptySlotDescriptor
import com.ssblur.scriptor.word.descriptor.target.inventory.FirstFilledSlotDescriptor
import com.ssblur.scriptor.word.descriptor.target.inventory.IgnoreTargetedSlotDescriptor
import com.ssblur.scriptor.word.descriptor.target.inventory.InventoryDescriptor

@Suppress("unused")
object InventoryDescriptors {
    val INVENTORY = register("inventory", InventoryDescriptor())
    val FIRST_EMPTY = register("first_empty", FirstEmptySlotDescriptor())
    val FIRST_FILLED = register("first_filled", FirstFilledSlotDescriptor())
    val FIRST_MATCHING = register("first_matching", IgnoreTargetedSlotDescriptor())

    val CASTER_INVENTORY = register("caster_inventory", CasterInventoryDescriptor())
    val CASTER_FIRST_EMPTY = register("caster_first_empty", CasterFirstEmptySlotDescriptor())
    val CASTER_FIRST_FILLED = register("caster_first_filled", CasterFirstFilledSlotDescriptor())
    val CASTER_FIRST_MATCHING =
        register("caster_first_matching", CasterIgnoreTargetedSlotDescriptor())
}
