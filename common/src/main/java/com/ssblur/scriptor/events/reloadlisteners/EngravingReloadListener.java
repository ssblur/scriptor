package com.ssblur.scriptor.events.reloadlisteners;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonElement;
import com.ssblur.scriptor.helpers.resource.EngravingResource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EngravingReloadListener extends ScriptorReloadListener {
  static Type ENGRAVING_TYPE = new TypeToken<EngravingResource>() {}.getType();
  static Random RANDOM = new Random();
  public static final EngravingReloadListener INSTANCE = new EngravingReloadListener();

  HashMap<ResourceLocation, EngravingResource> engravings;
  ArrayList<ResourceLocation> keys;

  EngravingReloadListener() {
    super("scriptor/engravings");
  }

  @Override
  protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
    engravings = new HashMap<>();
    keys = new ArrayList<>();
    super.apply(object, resourceManager, profilerFiller);
  }

  @Override
  public void loadResource(ResourceLocation resourceLocation, JsonElement jsonElement) {
    EngravingResource resource = GSON.fromJson(jsonElement, ENGRAVING_TYPE);
    if(!resource.isDisabled()) {
      keys.add(resourceLocation);
      engravings.put(resourceLocation, resource);
    }
  }

  public EngravingResource getRandomEngraving() {
    var keys = engravings.keySet();
    return engravings.get(keys.toArray(new ResourceLocation[]{})[RANDOM.nextInt(keys.size())]);
  }
}
