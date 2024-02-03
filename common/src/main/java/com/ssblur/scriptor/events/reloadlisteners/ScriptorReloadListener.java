package com.ssblur.scriptor.events.reloadlisteners;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.ssblur.scriptor.ScriptorMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public abstract class ScriptorReloadListener extends SimpleJsonResourceReloadListener {
  static Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
  public ScriptorReloadListener(String string) {
    super(GSON, string);
  }

  public abstract void loadResource(ResourceLocation resourceLocation, JsonElement jsonElement);

  @Override
  protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
    object.forEach((resourceLocation, jsonElement) -> {
      if(jsonElement.isJsonObject()) {
        var jsonObject = jsonElement.getAsJsonObject();
        if(jsonObject.has("disabled") && jsonObject.get("disabled").getAsBoolean())
          return;
      }
      loadResource(resourceLocation, jsonElement);
    });
  }
}
