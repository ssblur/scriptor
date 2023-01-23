package com.ssblur.scriptor.word.descriptor;

public class DebugDescriptor extends Descriptor {
  @Override
  public Cost cost() { return new Cost(0, COSTTYPE.ADDITIVE); }
}
