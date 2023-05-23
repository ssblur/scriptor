package com.ssblur.scriptor.events;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.ssblur.scriptor.helpers.TomeResource;
import com.ssblur.scriptor.registry.WordRegistry;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.descriptor.discount.ReagentDescriptor;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ReagentReloadListener extends SimpleJsonResourceReloadListener {
  static class ReagentResource {
    String item;
    int cost;
    boolean disabled = false;
    ReagentResource(String item, int cost, boolean disabled) {
      this.item = item;
      this.disabled = disabled;
      this.cost = cost;
    }

    ReagentResource(String item, int cost) {
      this.item = item;
      this.cost = cost;
    }
  }

  static ResourceLocation REAGENTS = new ResourceLocation("data/reagents");
  @SuppressWarnings("UnstableApiUsage")
  static Type REAGENT_TYPE = new TypeToken<ReagentResource>() {}.getType();
  static Gson GSON = new Gson();
  static Random RANDOM = new Random();
  public static final ReagentReloadListener INSTANCE = new ReagentReloadListener();

  HashMap<String, Descriptor> words;

  ReagentReloadListener() {
    super(GSON, "reagents");
  }

  @Override
  protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
    words = new HashMap<>();
    object.forEach((resourceLocation, jsonElement) -> {
      ReagentResource resource = GSON.fromJson(jsonElement, REAGENT_TYPE);
      if(!resource.disabled)
        words.put(
          resourceLocation.toShortLanguageKey(),
          WordRegistry.INSTANCE.register(
            "reagent." + resourceLocation.toShortLanguageKey(),
            new ReagentDescriptor(Registry.ITEM.get(new ResourceLocation(resource.item)), resource.cost)
          )
        );
    });
  }
}
