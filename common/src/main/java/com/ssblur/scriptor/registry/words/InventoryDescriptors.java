package com.ssblur.scriptor.registry.words;

import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.word.descriptor.focus.inventory.CasterFirstEmptySlotDescriptor;
import com.ssblur.scriptor.word.descriptor.focus.inventory.CasterFirstFilledSlotDescriptor;
import com.ssblur.scriptor.word.descriptor.focus.inventory.CasterIgnoreTargetedSlotDescriptor;
import com.ssblur.scriptor.word.descriptor.focus.inventory.CasterInventoryDescriptor;
import com.ssblur.scriptor.word.descriptor.target.inventory.FirstEmptySlotDescriptor;
import com.ssblur.scriptor.word.descriptor.target.inventory.FirstFilledSlotDescriptor;
import com.ssblur.scriptor.word.descriptor.target.inventory.IgnoreTargetedSlotDescriptor;
import com.ssblur.scriptor.word.descriptor.target.inventory.InventoryDescriptor;

@SuppressWarnings("unused")
public class InventoryDescriptors {
  private static final WordRegistry INSTANCE = WordRegistry.INSTANCE;

  public final Descriptor INVENTORY = INSTANCE.register("inventory", new InventoryDescriptor());
  public final Descriptor FIRST_EMPTY = INSTANCE.register("first_empty", new FirstEmptySlotDescriptor());
  public final Descriptor FIRST_FILLED = INSTANCE.register("first_filled", new FirstFilledSlotDescriptor());
  public final Descriptor FIRST_MATCHING = INSTANCE.register("first_matching", new IgnoreTargetedSlotDescriptor());

  public final Descriptor CASTER_INVENTORY = INSTANCE.register("caster_inventory", new CasterInventoryDescriptor());
  public final Descriptor CASTER_FIRST_EMPTY = INSTANCE.register("caster_first_empty", new CasterFirstEmptySlotDescriptor());
  public final Descriptor CASTER_FIRST_FILLED = INSTANCE.register("caster_first_filled", new CasterFirstFilledSlotDescriptor());
  public final Descriptor CASTER_FIRST_MATCHING = INSTANCE.register("caster_first_matching", new CasterIgnoreTargetedSlotDescriptor());
}
