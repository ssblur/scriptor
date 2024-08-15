package com.ssblur.scriptor.word.descriptor.discount;

import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.CastDescriptor;


public class RainDiscountDescriptor extends Descriptor implements CastDescriptor {
  @Override
  public Cost cost() { return new Cost(0.7, COSTTYPE.MULTIPLICATIVE); }

  @Override
  public boolean cannotCast(Targetable caster) {
    return caster.getLevel().isRainingAt(caster.getTargetBlockPos());
  }

  @Override
  public boolean allowsDuplicates() {
    return false;
  }
}
