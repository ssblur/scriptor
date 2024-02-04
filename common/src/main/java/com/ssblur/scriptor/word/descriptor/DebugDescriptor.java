package com.ssblur.scriptor.word.descriptor;

import com.ssblur.scriptor.api.word.Descriptor;

public class DebugDescriptor extends Descriptor {
  @Override
  public Cost cost() { return new Cost(0, COSTTYPE.ADDITIVE); }
}
