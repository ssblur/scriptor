package com.ssblur.scriptor.helpers.generators;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class CommunityModeGenerator extends TokenGenerator {
  protected static String[] consonantGroup = new String[]{
    "b",
    "c",
    "d",
    "e",
    "f",
    "g",
    "h",
    "j",
    "k",
    "l",
    "m",
    "n",
    "p",
    "r",
    "s",
    "t",
    "v",
    "w",
    "x",
    "z",
    "st",
    "th",
    "ld",
    "qu",
    "pr",
    "lk",
    "mm",
    "pp",
    "ck",
    "rd",
    "pt"
  };
  protected static String[] vowelGroup = new String[]{
    "a",
    "e",
    "i",
    "o",
    "u",
    "ou",
    "ao",
    "io",
    "ee"
  };

  public CommunityModeGenerator(JsonObject object) {}

  @Override
  public String generateToken(String key, @Nullable JsonObject parameters) {
    Random random = new Random(key.hashCode() + 0x055b10b0);
    StringBuilder builder = new StringBuilder();
    boolean consonantFirst = random.nextBoolean();
    for(int i = 0; i < 3 + random.nextInt(4); i++) {
      if (consonantFirst ^ (i % 2 == 1))
        builder.append(vowelGroup[random.nextInt(vowelGroup.length)]);
      else
        builder.append(consonantGroup[random.nextInt(consonantGroup.length)]);
    }

    return builder.toString();
  }

  @Override
  public boolean canBeDefault() {
    return true;
  }
}
