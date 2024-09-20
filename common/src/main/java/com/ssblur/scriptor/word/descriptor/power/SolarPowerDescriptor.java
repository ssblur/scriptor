package com.ssblur.scriptor.word.descriptor.power;

import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.api.word.descriptor.StrengthDescriptor;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.api.word.descriptor.CastDescriptor;


public class SolarPowerDescriptor extends Descriptor implements CastDescriptor, StrengthDescriptor {
  @Override
  public Cost cost() { return new Cost(0, COSTTYPE.ADDITIVE_POST); }

  @Override
  public boolean cannotCast(Targetable caster) {
    return !caster.getLevel().isDay();
  }

  @Override
  public double strengthModifier() {
    return 1.5;
  }

  @Override
  public boolean allowsDuplicates() {
    return false;
  }
}
