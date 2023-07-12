package com.ssblur.scriptor.helpers.generators;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ssblur.scriptor.exceptions.MissingRequiredElementException;
import com.ssblur.scriptor.registry.TokenGeneratorRegistry;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.HashMap;

public class StaticTokenGenerator extends TokenGenerator{
  enum CollisionStrategy {
    FAIL,
    FALLBACK,
    SHORTEN
  }
  public record StaticTokenParameters(CollisionStrategy collisionStrategy) {}
  static Type PARAMETERS_TYPE = new TypeToken<StaticTokenParameters>() {}.getType();
  static Gson GSON = new Gson();
  HashMap<String, Boolean> usedTokens = new HashMap<>();
  StaticTokenParameters parameters;

  public StaticTokenGenerator(JsonObject object) {
    parameters = GSON.fromJson(object, PARAMETERS_TYPE);
  }
  @Override
  public String generateToken(String key, @Nullable JsonObject parameters) {
    assert parameters != null;
    if(!parameters.has("token"))
      throw new MissingRequiredElementException("token", "The word \"" + key + "\" must have a token defined because it uses a static token generator.");

    var token = parameters.get("token").getAsString();
    if(usedTokens.containsKey(token))
      switch(this.parameters.collisionStrategy()) {
        case FAIL:
          throw new RuntimeException(String.format("Failed to generate static token: token %s already in use!", token));
        case SHORTEN:
          token = shorten(token);
          break;
        case FALLBACK:
          return TokenGeneratorRegistry.INSTANCE.getGenerator(TokenGeneratorRegistry.INSTANCE.getDefaultGenerator()).generateToken(key, null);
      }
    usedTokens.put(token, true);
    return token;
  }

  String shorten(String string) {
    String token = string;
    while(usedTokens.containsKey(token)) {
      token = token.substring(0, token.length() - 1);
      if(token.isEmpty())
        throw new RuntimeException(String.format("Failed to generate a static token: token %s cannot be shortened any further!", string));
    }
    return token;
  }

  @Override
  public boolean canBeDefault() {
    return false;
  }
}
