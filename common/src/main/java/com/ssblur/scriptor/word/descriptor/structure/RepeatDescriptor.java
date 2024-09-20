package com.ssblur.scriptor.word.descriptor.structure;

import com.ssblur.scriptor.api.word.Descriptor;

public class RepeatDescriptor extends Descriptor {
  public final int count;
  public RepeatDescriptor(int count) {
    this.count = count;
  }

  @Override
  public Cost cost() {
    return Cost.add(0);
  }
}
