package com.ssblur.scriptor.word.descriptor;

public class ColorDescriptor extends Descriptor {
  int color;
  public ColorDescriptor(int color) {
    this.color = color;
  }

  @Override
  public Cost cost() {
    return new Cost(0, COSTTYPE.ADDITIVE);
  }

  public int getColor() {
    return color;
  }
}
