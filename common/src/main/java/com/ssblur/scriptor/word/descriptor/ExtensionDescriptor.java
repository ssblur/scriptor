package com.ssblur.scriptor.word.descriptor;

public class ExtensionDescriptor extends Descriptor {
  @Override
  public Cost cost() { return new Cost(1, COSTTYPE.ADDITIVE); }

}
