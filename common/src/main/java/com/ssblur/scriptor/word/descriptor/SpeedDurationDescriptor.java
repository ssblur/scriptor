package com.ssblur.scriptor.word.descriptor;

import com.ssblur.scriptor.api.word.descriptor.SpeedDescriptor;
import com.ssblur.scriptor.word.descriptor.duration.SimpleDurationDescriptor;

public class SpeedDurationDescriptor extends SimpleDurationDescriptor implements SpeedDescriptor {
  double speed;

  public SpeedDurationDescriptor(int cost, double duration, double speed) {
    super(cost, duration);
    this.speed = speed;
  }

  @Override
  public double speedModifier() {
    return speed;
  }
}
