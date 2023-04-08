package com.ssblur.scriptor.word.descriptor.power;

import com.ssblur.scriptor.word.descriptor.Descriptor;

public class SimpleStrengthDescriptor extends Descriptor implements StrengthDescriptor {
  Cost cost;
  double strength;

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
}
