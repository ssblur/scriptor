package com.ssblur.scriptor.word.descriptor;

public class CheapDescriptor extends Descriptor {
  @Override
  public Cost cost() { return new Cost(0.5, COSTTYPE.MULTIPLICATIVE); }
}
