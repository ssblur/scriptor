package com.ssblur.scriptor.api.word.descriptor;

import com.ssblur.scriptor.api.word.Descriptor;

public class RepeatDescriptor extends Descriptor {
  public final int count;

  /**
   * A structural Descriptor which will, when parsed, be replaced
   * with duplicates of the next Descriptor.
   * @param count The number of duplicates this Descriptor will produce
   */
  public RepeatDescriptor(int count) {
    this.count = count;
  }

  // Unused
  @Override
  public Cost cost() {
    return Cost.add(0);
  }
}
