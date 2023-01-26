package com.ssblur.scriptor.word.descriptor;

public class SimpleStrengthDescriptor extends Descriptor implements StrengthDescriptor{
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
