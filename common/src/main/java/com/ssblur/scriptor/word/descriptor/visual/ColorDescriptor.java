package com.ssblur.scriptor.word.descriptor.visual;

import com.ssblur.scriptor.word.descriptor.Descriptor;

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
