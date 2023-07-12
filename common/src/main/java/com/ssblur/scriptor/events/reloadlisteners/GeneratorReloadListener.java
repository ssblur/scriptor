package com.ssblur.scriptor.events.reloadlisteners;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.exceptions.InvalidGeneratorException;
import com.ssblur.scriptor.exceptions.MissingRequiredElementException;
import com.ssblur.scriptor.helpers.resource.TomeResource;
import com.ssblur.scriptor.registry.TokenGeneratorRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Random;

public class GeneratorReloadListener extends SimpleJsonResourceReloadListener {
  static ResourceLocation GENERATORS = new ResourceLocation("data/scriptor/generators");
  static Gson GSON = new Gson();
  public static final GeneratorReloadListener INSTANCE = new GeneratorReloadListener();
  GeneratorReloadListener() {
    super(GSON, "scriptor/generators");
  }

  @Override
  protected void apply(Map<ResourceLocation, JsonElement> objects, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
    objects.forEach((resourceLocation, jsonElement) -> {
      var object = jsonElement.getAsJsonObject();
      if(!object.has("generator"))
        throw new MissingRequiredElementException("generator", resourceLocation);
      if(!object.has("parameters"))
        throw new MissingRequiredElementException("parameters", resourceLocation);

      var generator = object.get("generator").getAsString();
      if(TokenGeneratorRegistry.INSTANCE.getGeneratorGenerator(generator) == null)
        throw new InvalidGeneratorException(generator, resourceLocation);

      var generatorGenerator = TokenGeneratorRegistry.INSTANCE.getGeneratorGenerator(generator);
      TokenGeneratorRegistry.INSTANCE.registerGenerator(resourceLocation, generatorGenerator.create(object.get("parameters").getAsJsonObject()));

      if(object.has("default") && object.get("default").getAsBoolean())
        TokenGeneratorRegistry.INSTANCE.registerDefaultGenerator(resourceLocation);
      ScriptorMod.LOGGER.info("Loaded custom generator at {}", resourceLocation);
    });
  }
}
