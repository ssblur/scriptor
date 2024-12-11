package com.ssblur.scriptor.events.reloadlisteners;

import com.google.gson.JsonElement;
import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.error.InvalidGeneratorException;
import com.ssblur.scriptor.error.MissingRequiredElementException;
import com.ssblur.scriptor.registry.TokenGeneratorRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class GeneratorReloadListener extends ScriptorReloadListener {
  public static final GeneratorReloadListener INSTANCE = new GeneratorReloadListener();
  GeneratorReloadListener() {
    super("scriptor/generators");
  }

  @Override
  protected void apply(Map<ResourceLocation, JsonElement> objects, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
    ScriptorMod.INSTANCE.setCOMMUNITY_MODE(false);
    super.apply(objects, resourceManager, profilerFiller);
  }

  @Override
  public void loadResource(ResourceLocation resourceLocation, JsonElement jsonElement) {
    var object = jsonElement.getAsJsonObject();
    if(!object.has("generator"))
      throw new MissingRequiredElementException("generator", resourceLocation);
    if(!object.has("parameters"))
      throw new MissingRequiredElementException("parameters", resourceLocation);

    var generator = object.get("generator").getAsString();
    if(TokenGeneratorRegistry.INSTANCE.getGeneratorGenerator(generator) == null)
      throw new InvalidGeneratorException(generator, resourceLocation);

    if(generator.equals("community")) {
      ScriptorMod.INSTANCE.getLOGGER().info("Community mode generator loaded, locking down debug features.");
      ScriptorMod.INSTANCE.setCOMMUNITY_MODE(true);
    }

    var generatorGenerator = TokenGeneratorRegistry.INSTANCE.getGeneratorGenerator(generator);
    if(generatorGenerator != null)
      TokenGeneratorRegistry.INSTANCE.registerGenerator(resourceLocation, generatorGenerator.create(object.get("parameters").getAsJsonObject()));

    if(object.has("default") && object.get("default").getAsBoolean())
      if(TokenGeneratorRegistry.defaultGenerator == null
          || TokenGeneratorRegistry.defaultGenerator.getNamespace().equals("scriptor")
          || !resourceLocation.getNamespace().equals("scriptor"))
        TokenGeneratorRegistry.INSTANCE.registerDefaultGenerator(resourceLocation);
      else
        ScriptorMod.INSTANCE.getLOGGER().warn(
          "Skipping registration of default generator at {}; A default generator outside the 'scriptor' namespace was already registered.",
          resourceLocation
        );

    ScriptorMod.INSTANCE.getLOGGER().info("Loaded custom generator at {}", resourceLocation);
  }
}
