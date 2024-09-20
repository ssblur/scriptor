package com.ssblur.scriptor.api.word.descriptor;

public interface DurationDescriptor {
  /**
   * How much this descriptor modifies the spell's duration
   * @return How much to add to this spell's duration
   */
  double durationModifier();
}
