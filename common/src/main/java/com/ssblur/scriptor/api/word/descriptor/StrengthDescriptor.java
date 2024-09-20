package com.ssblur.scriptor.api.word.descriptor;

public interface StrengthDescriptor {
  /**
   * How much to modify spell strength by.
   * @return A number to add to spell strength
   */
  double strengthModifier();
}
