package com.ssblur.scriptor.registry.words;

import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.api.word.descriptor.ColorDescriptor;

@SuppressWarnings("unused")
public class ColorDescriptors {
  private static final WordRegistry INSTANCE = WordRegistry.INSTANCE;

  public final Descriptor WHITE = INSTANCE.register("white", new ColorDescriptor(0xe4e4e4));
  public final Descriptor LIGHT_GRAY = INSTANCE.register("light_gray", new ColorDescriptor(0xa0a7a7));
  public final Descriptor DARK_GRAY = INSTANCE.register("dark_gray", new ColorDescriptor(0x414141));
  public final Descriptor BLACK = INSTANCE.register("black", new ColorDescriptor(0x181414));
  public final Descriptor RED = INSTANCE.register("red", new ColorDescriptor(0x9e2b27));
  public final Descriptor ORANGE = INSTANCE.register("orange", new ColorDescriptor(0xea7e35));
  public final Descriptor YELLOW = INSTANCE.register("yellow", new ColorDescriptor(0xc2b51c));
  public final Descriptor LIME_GREEN = INSTANCE.register("lime_green", new ColorDescriptor(0x39ba2e));
  public final Descriptor GREEN = INSTANCE.register("green", new ColorDescriptor(0x364b18));
  public final Descriptor LIGHT_BLUE = INSTANCE.register("light_blue", new ColorDescriptor(0x6387d2));
  public final Descriptor CYAN = INSTANCE.register("cyan", new ColorDescriptor(0x267191));
  public final Descriptor BLUE = INSTANCE.register("blue", new ColorDescriptor(0x253193));
  public final Descriptor PURPLE = INSTANCE.register("purple", new ColorDescriptor(0x7e34bf));
  public final Descriptor MAGENTA = INSTANCE.register("magenta", new ColorDescriptor(0xbe49c9));
  public final Descriptor PINK = INSTANCE.register("pink", new ColorDescriptor(0xd98199));
  public final Descriptor BROWN = INSTANCE.register("brown", new ColorDescriptor(0x56331c));

  public final Descriptor RAINBOW = INSTANCE.register("rainbow", new ColorDescriptor(-1));
//  public final Descriptor TRANS = INSTANCE.register("trans", new ColorDescriptor(-2));
//  public final Descriptor ENBY = INSTANCE.register("enby", new ColorDescriptor(-3));
//  public final Descriptor BI = INSTANCE.register("bi", new ColorDescriptor(-4));
//  public final Descriptor PAN = INSTANCE.register("pan", new ColorDescriptor(-5));
//  public final Descriptor LESBIAN = INSTANCE.register("lesbian", new ColorDescriptor(-6));
//  public final Descriptor AGENDER = INSTANCE.register("agender", new ColorDescriptor(-7));
//  public final Descriptor ACE = INSTANCE.register("ace", new ColorDescriptor(-8));
//  public final Descriptor GENDERQUEER = INSTANCE.register("genderqueer", new ColorDescriptor(-9));
//  public final Descriptor GENDERFLUID = INSTANCE.register("genderfluid", new ColorDescriptor(-10));
//  public final Descriptor INTERSEX = INSTANCE.register("intersex", new ColorDescriptor(-11));
//  public final Descriptor ARO = INSTANCE.register("aro", new ColorDescriptor(-12));
//  public final Descriptor POLY = INSTANCE.register("poly", new ColorDescriptor(-13));
}
