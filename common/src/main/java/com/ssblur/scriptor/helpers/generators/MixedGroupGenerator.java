package com.ssblur.scriptor.helpers.generators;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ssblur.scriptor.events.reloadlisteners.ReagentReloadListener;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Random;

public class MixedGroupGenerator extends TokenGenerator {
  public record TokenGroup(String[] tokens, int weight) {}
  public record MixedGroupParameters(TokenGroup[] groups, int maxConsecutiveGroups, int minTokens, int maxTokens) {}
  static Type PARAMETERS_TYPE = new TypeToken<MixedGroupParameters>() {}.getType();
  static Gson GSON = new Gson();
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
      if (parameters.has("maxTokens"))
        maxTokens = parameters.get("maxTokens").getAsInt();
      if (parameters.has("minTokens"))
        minTokens = parameters.get("minTokens").getAsInt();
    }

    int tokens = RANDOM.nextInt(minTokens, maxTokens + 1);
    StringBuilder builder = new StringBuilder();

    TokenGroup lastGroup = null;
    int consecutiveGroups = 0;

    for(int i = minTokens; i <= tokens; i++) {
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
