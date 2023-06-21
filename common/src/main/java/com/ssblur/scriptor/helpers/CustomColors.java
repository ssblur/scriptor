package com.ssblur.scriptor.helpers;

import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.descriptor.ColorDescriptor;
import net.minecraft.world.item.DyeColor;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;

public class CustomColors {
  public static CustomColors INSTANCE = new CustomColors();

  static {
    // Rainbow
    // it can be gay too, as a treat.
    INSTANCE.register(-1, tick -> {
      tick %= 40;
      return Color.getHSBColor(tick / 40f, 1f, 0.5f).getRGB();
    });

    // Trans Flag
    INSTANCE.register(
      -2,
      createCustomColor(new int[]{
        0x5BCEFA,
        0xF5A9B8,
        0xFFFFFF,
        0xF5A9B8
      })
    );

    // Enby Flag
    INSTANCE.register(
      -3,
      createCustomColor(new int[]{
        0xFCF434,
        0xFFFFFF,
        0x9C59D1,
        0x2C2C2C
      })
    );

    // Bi flag
    INSTANCE.register(
      -4,
      createCustomColor(new int[]{
        0xD60270,
        0x9B4D96,
        0x0038A8
      })
    );

    // Pan flag
    INSTANCE.register(
      -5,
      createCustomColor(new int[]{
        0xFF1C8D,
        0xFFD700,
        0x1AB3FF
      })
    );

    // Lesbian Flag
    INSTANCE.register(
      -6,
      createCustomColor(new int[]{
        0xD62800,
        0xFF9B56,
        0xFFFFFF,
        0xD462A6,
        0xA40062
      })
    );

    // Agender Flag
    INSTANCE.register(
      -7,
      createCustomColor(new int[]{
        0x000000,
        0xBABABA,
        0xFFFFFF,
        0xBAF484,
        0x000000,
        0xBABABA,
        0xFFFFFF
      })
    );

    // Ace (/grayce, demi, etc.) Flag
    INSTANCE.register(
      -8,
      createCustomColor(new int[]{
        0x000000,
        0xA4A4A4,
        0xFFFFFF,
        0x810081
      })
    );

    // Genderqueer Flag
    INSTANCE.register(
      -9,
      createCustomColor(new int[]{
        0xB57FDD,
        0xFFFFFF,
        0x49821E
      })
    );

    // Genderfluid Flag
    INSTANCE.register(
      -10,
      createCustomColor(new int[]{
        0xFE76A2,
        0xFFFFFF,
        0xBF12D7,
        0x000000,
        0x303CBE
      })
    );

    // Intersex Flag
    INSTANCE.register(
      -11,
      createCustomColor(new int[]{
        0xFFD800,
        0x7902AA
      })
    );

    // Aro Flag
    INSTANCE.register(
      -12,
      createCustomColor(new int[]{
        0x3BA740,
        0xA8D47A,
        0xFFFFFF,
        0xABABAB,
        0x000000
      })
    );

    // Poly Flag
    INSTANCE.register(
      -13,
      createCustomColor(new int[]{
        0x0000FF,
        0xFF0000,
        0xFFFF00,
        0xFF0000,
        0x000000
      })
    );
  }

  HashMap<Integer, Function<Float, Integer>> registry = new HashMap<>();

  public void register(int index, Function<Float, Integer> color) {
    registry.put(index, color);
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
