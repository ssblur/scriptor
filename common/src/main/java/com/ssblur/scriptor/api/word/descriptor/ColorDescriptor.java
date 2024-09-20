package com.ssblur.scriptor.api.word.descriptor;

import com.ssblur.scriptor.api.word.Descriptor;

public class ColorDescriptor extends Descriptor {
  int color;

  /**
   * A descriptor which can modify a spell or target's color
   * @param color This Descriptor's color
   */
  public ColorDescriptor(int color) {
    this.color = color;
  }

  @Override
  public Cost cost() {
    return new Cost(0, COSTTYPE.ADDITIVE);
  }

  /**
   * The color of this descriptor
   * Can change over time; will update any actively rendered components appropriately.
   * @return This Descriptor's color.
   */
  public int getColor() {
    return color;
  }
}
