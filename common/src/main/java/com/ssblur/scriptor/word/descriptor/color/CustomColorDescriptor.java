package com.ssblur.scriptor.word.descriptor.color;


import com.ssblur.scriptor.api.word.descriptor.ColorDescriptor;
import com.ssblur.scriptor.color.CustomColors;

public class CustomColorDescriptor extends ColorDescriptor {
  public CustomColorDescriptor(String color) {
    super(CustomColors.getKey(color));
  }
}
