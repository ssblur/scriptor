package com.ssblur.scriptor.helpers.generators;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

public class DebugGenerator extends TokenGenerator {

  public DebugGenerator(JsonObject object) {}

  protected DebugGenerator() {}

  public boolean canBeDefault() { return true; }

  @Override
  public String generateToken(String key, @Nullable JsonObject parameters) {
    return key;
  }
}
