package com.ssblur.scriptor.helpers.generators;

import com.google.gson.JsonObject;

import javax.annotation.Nullable;

public abstract class TokenGenerator {
  abstract public String generateToken(String key, @Nullable JsonObject parameters);
  abstract public boolean canBeDefault();

  public interface TokenGeneratorGenerator {
    TokenGenerator create(JsonObject parameters);
  }
}
