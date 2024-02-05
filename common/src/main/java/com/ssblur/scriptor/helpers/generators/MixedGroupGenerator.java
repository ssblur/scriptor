package com.ssblur.scriptor.helpers.generators;

import com.google.common.reflect.TypeToken;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Random;

public class MixedGroupGenerator extends TokenGenerator {
  public record TokenGroup(String[] tokens, int weight) {}
  public record MixedGroupParameters(TokenGroup[] groups, int maxConsecutiveGroups, int minTokens, int maxTokens) {}
  static Type PARAMETERS_TYPE = new TypeToken<MixedGroupParameters>() {}.getType();
  static Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
  static Random RANDOM = new Random();


  int totalWeight;
  MixedGroupParameters parameters;

  public MixedGroupGenerator(JsonObject object) {
    parameters = GSON.fromJson(object, PARAMETERS_TYPE);
    for(var group: parameters.groups())
      totalWeight += group.weight();
  }

  protected MixedGroupGenerator() {}

  public boolean canBeDefault() { return true; }

  @Override
  public String generateToken(String key, @Nullable JsonObject parameters) {
    int maxTokens = this.parameters.maxTokens;
    int minTokens = this.parameters.minTokens;
    if(parameters != null) {
      if (parameters.has("max_tokens"))
        maxTokens = parameters.get("max_tokens").getAsInt();
      if (parameters.has("min_tokens"))
        minTokens = parameters.get("min_tokens").getAsInt();
    }

    int tokens = RANDOM.nextInt(minTokens, maxTokens + 1);
    StringBuilder builder = new StringBuilder();

    TokenGroup lastGroup = null;
    int consecutiveGroups = 0;

    for(int i = 0; i < tokens; i++) {
      TokenGroup tokenGroup = null;
      do {
        int random = RANDOM.nextInt(totalWeight);
        for (var group : this.parameters.groups()) {
          if (random < group.weight) {
            tokenGroup = group;
            break;
          } else {
            random -= group.weight;
          }
        }
      } while(tokenGroup == null || (lastGroup == tokenGroup && consecutiveGroups >= this.parameters.maxConsecutiveGroups));

      if(lastGroup != tokenGroup) {
        consecutiveGroups = 0;
        lastGroup = tokenGroup;
      }
      consecutiveGroups++;

      var groupTokens = tokenGroup.tokens;
      builder.append(groupTokens[RANDOM.nextInt(groupTokens.length)]);
    }
    return builder.toString();
  }
}
