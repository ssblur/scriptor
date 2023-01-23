package com.ssblur.scriptor.events;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.ssblur.scriptor.helpers.LimitedBookSerializer;
import com.ssblur.scriptor.helpers.TomeResource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TomeReloadListener extends SimpleJsonResourceReloadListener {
  static ResourceLocation TOMES = new ResourceLocation("data/tomes");
  static Type TOME_TYPE = new TypeToken<TomeResource>() {}.getType();
  static Gson GSON = new Gson();
  static Random RANDOM = new Random();
  public static final TomeReloadListener INSTANCE = new TomeReloadListener();

  HashMap<ResourceLocation, TomeResource> tomes;
  ArrayList<ResourceLocation> keys;

  TomeReloadListener() {
    super(GSON, "tomes");
  }

  @Override
  protected void apply(Map<ResourceLocation, JsonElement> object, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
    tomes = new HashMap<>();
    keys = new ArrayList<>();
    object.forEach((resourceLocation, jsonElement) -> {
      TomeResource resource = GSON.fromJson(jsonElement, TOME_TYPE);
      if(!resource.isDisabled()) {
        keys.add(resourceLocation);
        tomes.put(resourceLocation, resource);
      }
    });
  }

  public TomeResource getRandomTome() {
    return tomes.get(keys.get(RANDOM.nextInt(keys.size())));
  }
}
