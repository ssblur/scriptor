package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import net.minecraft.world.entity.Entity;

public class DebugAction extends Action {
  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    System.out.println(caster + " cast debug on " + targetable + " with " + descriptors.length + " descriptors.");
  }
  @Override
  public Cost cost() { return new Cost(0, COSTTYPE.ADDITIVE); }
}
