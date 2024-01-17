package com.ssblur.scriptor.registry;

import com.google.gson.JsonObject;
import com.ssblur.scriptor.helpers.generators.CommunityModeGenerator;
import com.ssblur.scriptor.helpers.generators.MixedGroupGenerator;
import com.ssblur.scriptor.helpers.generators.StaticTokenGenerator;
import com.ssblur.scriptor.helpers.generators.TokenGenerator;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;

public class TokenGeneratorRegistry {
  public static TokenGeneratorRegistry INSTANCE = new TokenGeneratorRegistry();

  public static final TokenGenerator.TokenGeneratorGenerator MIXED_GROUP =
    INSTANCE.registerGeneratorGenerator("mixed_groups", MixedGroupGenerator::new);
  public static final TokenGenerator.TokenGeneratorGenerator STATIC_TOKEN =
    INSTANCE.registerGeneratorGenerator("static_token", StaticTokenGenerator::new);
  public static final TokenGenerator.TokenGeneratorGenerator COMMUNITY =
    INSTANCE.registerGeneratorGenerator("community", CommunityModeGenerator::new);

  HashMap<ResourceLocation, TokenGenerator> generators = new HashMap<>();
  HashMap<String, ResourceLocation> generatorBindings = new HashMap<>();
  HashMap<String, JsonObject> generatorBindingConfig = new HashMap<>();
  HashMap<String, TokenGenerator.TokenGeneratorGenerator> generatorGenerators = new HashMap<>();
  ResourceLocation defaultGenerator;

  public TokenGenerator.TokenGeneratorGenerator registerGeneratorGenerator(String key, TokenGenerator.TokenGeneratorGenerator generatorGenerator) {
    generatorGenerators.put(key, generatorGenerator);
    return generatorGenerator;
  }

  public TokenGenerator.TokenGeneratorGenerator getGeneratorGenerator(String key) {
    return generatorGenerators.get(key);
  }

  public void registerGenerator(ResourceLocation key, TokenGenerator generator) {
    generators.put(key, generator);
  }

  public TokenGenerator getGenerator(ResourceLocation key) {
    return generators.get(key);
  }

  public void registerBinding(String word, ResourceLocation generator, @Nullable JsonObject parameters) {
    generatorBindings.put(word, generator);
    generatorBindingConfig.put(word, parameters);
  }

  public ResourceLocation getBinding(String word) {
    return generatorBindings.getOrDefault(word, defaultGenerator);
  }

  public void registerDefaultGenerator(ResourceLocation generator) {
    defaultGenerator = generator;
  }

  public ResourceLocation getDefaultGenerator() {
    return defaultGenerator;
  }

  public String generateWord(String word, @Nullable JsonObject object) {
    return getGenerator(getBinding(word)).generateToken(word, object);
  }

  public String generateWord(String word) {
    return generateWord(word, generatorBindingConfig.get(word));
  }
}
