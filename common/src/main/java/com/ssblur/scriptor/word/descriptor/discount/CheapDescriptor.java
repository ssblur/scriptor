package com.ssblur.scriptor.word.descriptor.discount;

import com.ssblur.scriptor.api.word.Descriptor;

public class CheapDescriptor extends Descriptor {
  @Override
  public Cost cost() { return new Cost(0.5, COSTTYPE.MULTIPLICATIVE); }
}
