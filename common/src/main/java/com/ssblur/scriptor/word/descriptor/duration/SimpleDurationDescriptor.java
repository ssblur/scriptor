package com.ssblur.scriptor.word.descriptor.duration;

import com.ssblur.scriptor.word.descriptor.Descriptor;

public class SimpleDurationDescriptor extends Descriptor implements DurationDescriptor {
  Cost cost;
  double duration;

  public SimpleDurationDescriptor(int cost, double duration) {
    this.cost = new Cost(cost, COSTTYPE.ADDITIVE);
    this.duration = duration;
  }

  @Override
  public Cost cost() {
    return cost;
  }

  @Override
  public double durationModifier() {
    return duration;
  }
}
