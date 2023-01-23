package com.ssblur.scriptor.word.descriptor;

public class DurationDescriptor extends ExtensionDescriptor {
  @Override
  public Cost cost() { return new Cost(2, COSTTYPE.ADDITIVE); }

}
