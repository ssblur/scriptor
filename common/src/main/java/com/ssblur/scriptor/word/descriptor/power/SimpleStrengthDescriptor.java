package com.ssblur.scriptor.word.descriptor.power;

import com.ssblur.scriptor.word.descriptor.Descriptor;

public class SimpleStrengthDescriptor extends Descriptor implements StrengthDescriptor {
  Cost cost;
  double strength;
  boolean allowDuplication = false;

  public SimpleStrengthDescriptor(int cost, double strength) {
    this.cost = new Cost(cost, COSTTYPE.ADDITIVE);
    this.strength = strength;
  }

  @Override
  public Cost cost() {
    return cost;
  }

  @Override
  public double strengthModifier() {
    return strength;
  }

  public SimpleStrengthDescriptor allowDuplication() {
    allowDuplication = true;
    return this;
  }

  @Override
  public boolean allowsDuplicates() {
    return allowDuplication;
  }
}
