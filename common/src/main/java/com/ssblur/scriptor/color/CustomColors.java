package com.ssblur.scriptor.color;

import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.word.descriptor.color.ColorDescriptor;
import net.minecraft.world.item.DyeColor;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;

public class CustomColors {
  public static CustomColors INSTANCE = new CustomColors();

  static {
    INSTANCE.reset();
  }

  HashMap<Integer, Function<Float, Integer>> registry = new HashMap<>();
  int currentNumber = 0;
  HashMap<String, Integer> colors = new HashMap<>();

  public void reset() {
    currentNumber = 0;
    colors = new HashMap<>();
    registry = new HashMap<>();

    // Rainbow
    // it can be gay too, as a treat.
    INSTANCE.register("rainbow", tick -> {
      float s = tick % 31;
      s /= 93;
      s += 0.75f;
      float b = tick % 23;
      b /= 46;
      b += 0.5f;
      tick %= 40;
      return Color.getHSBColor(tick / 40f, s, b).getRGB();
    });
  }

  public void putColor(int index, String key, int[] colors) {
    this.colors.put(key, index);
    this.registry.put(index, createCustomColor(colors));
  }

  public int register(String value, Function<Float, Integer> color) {
    currentNumber--;
    colors.put(value, currentNumber);
    registry.put(currentNumber, color);
    return currentNumber;
  }

  public static int registerWithEasing(String key, int[] values) {
    return INSTANCE.register(
      key,
      createCustomColor(values)
    );
  }

  public static int getKey(String key) {
    return INSTANCE.colors.get(key);
  }

  public static int getColor(int color, float tick) {
    if(color > 0) return color;
    if(INSTANCE.registry.containsKey(color))
      return INSTANCE.registry.get(color).apply(tick);
    return 0;
  }

  public static int getColor(Iterable<Descriptor> descriptors) {
    long r = 0;
    long g = 0;
    long b = 0;
    long a = 0;
    int colorN = 0;
    int c;
    for(Descriptor d: descriptors)
      if(d instanceof ColorDescriptor descriptor)
        if(descriptor.getColor() >= 0) {
          colorN += 1;
          c = descriptor.getColor();
          b += c & 0xFF;
          g += (c >> 8) & 0xFF;
          r += (c >> 16) & 0xFF;
          a += (c >> 24) & 0xFF;
        } else {
          return descriptor.getColor();
        }
    if(colorN == 0) return 0xa020f0;

    r /= colorN;
    g /= colorN;
    b /= colorN;
    a /= colorN;
    return (int) ((a << 24) + (r << 16) + (g << 8) + b);
  }

  public static int getColor(Descriptor[] descriptors) {
    return getColor(Arrays.stream(descriptors).toList());
  }

  static int ease(int from, int to, float partial) {
    partial = (float) Math.pow(partial, 5);
    int bA = from & 0xFF;
    int gA = (from >> 8) & 0xFF;
    int rA = (from >> 16) & 0xFF;
    int aA = (from >> 24) & 0xFF;
    int bB = to & 0xFF;
    int gB = (to >> 8) & 0xFF;
    int rB = (to >> 16) & 0xFF;
    int aB = (to >> 24) & 0xFF;

    int b = (int)( bA * partial + bB * (1 - partial));
    int g = (int)( gA * partial + gB * (1 - partial));
    int r = (int)( rA * partial + rB * (1 - partial));
    int a = (int)( aA * partial + aB * (1 - partial));
    return (a << 24) + (r << 16) + (g << 8) + b;
  }

  static Function<Float, Integer> createCustomColor(int[] list) {
    return tick -> {
      float partial = tick % 60 / 60;
      tick /= 60;
      tick %= list.length;
      int index = tick.intValue();
      int color = list[index];
      int nextColor = list[(index + 1) % list.length];
      return ease(nextColor, color, partial);
    };
  }

  public static DyeColor getDyeColor(int color, float tick) {
    int distance = -1;
    DyeColor dyeColor = DyeColor.WHITE;

    int c = getColor(color, tick);
    int b = c & 0xFF;
    int g = (c >> 8) & 0xFF;
    int r = (c >> 16) & 0xFF;

    int cD, bD, gD, rD, aD, d;
    for(var dye: DyeColor.values()) {
      cD = dye.getFireworkColor();
      bD = cD & 0xFF;
      gD = (cD >> 8) & 0xFF;
      rD = (cD >> 16) & 0xFF;
      d = (int) Math.sqrt(Math.pow(b - bD, 2) + Math.pow(g - gD, 2) + Math.pow(r - rD, 2));
      if(d < distance || distance == -1) {
        dyeColor = dye;
        distance = d;
      }
    }

    return dyeColor;
  }
}
