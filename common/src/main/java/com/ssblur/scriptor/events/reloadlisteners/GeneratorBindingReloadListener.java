package com.ssblur.scriptor.events.reloadlisteners;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ssblur.scriptor.registry.TokenGeneratorRegistry;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.lang.reflect.Type;

public class GeneratorBindingReloadListener extends ScriptorReloadListener {
  public record GeneratorBinding(String word, @Nullable JsonObject parameters){}
  public record GeneratorBindings(String generator, GeneratorBinding[] bindings){}
  static Type BINDINGS = new TypeToken<GeneratorBindings>() {}.getType();
  public static final GeneratorBindingReloadListener INSTANCE = new GeneratorBindingReloadListener();
  GeneratorBindingReloadListener() {
    super("scriptor/bindings");
  }

  @Override
  public void loadResource(ResourceLocation resourceLocation, JsonElement jsonElement) {
    GeneratorBindings bindings = GSON.fromJson(jsonElement, BINDINGS);
    for(var binding: bindings.bindings())
      if(
        TokenGeneratorRegistry.INSTANCE.getBinding(binding.word()) == null
          || TokenGeneratorRegistry.INSTANCE.getBinding(binding.word()).getNamespace().equals("scriptor")
          || !resourceLocation.getNamespace().equals("scriptor")
      )
        TokenGeneratorRegistry.INSTANCE.registerBinding(binding.word(), new ResourceLocation(bindings.generator()), binding.parameters());
  }
}