package com.ssblur.scriptor.events.reloadlisteners;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.item.ScriptorItems;
import com.ssblur.scriptor.registry.words.WordRegistry;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.descriptor.discount.ReagentDescriptor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ReagentReloadListener extends ScriptorReloadListener {
  static class ReagentResource {
    String item;
    int cost;

    ReagentResource(String item, int cost) {
      this.item = item;
      this.cost = cost;
    }
  }

  static Type REAGENT_TYPE = new TypeToken<ReagentResource>() {}.getType();
  public static final ReagentReloadListener INSTANCE = new ReagentReloadListener();

  HashMap<String, Descriptor> words;

  ReagentReloadListener() {
    super("scriptor/reagents");
  }

  @Override
  protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
    words = new HashMap<>();
    super.apply(object, resourceManager, profilerFiller);
  }

  @Override
  public void loadResource(ResourceLocation resourceLocation, JsonElement jsonElement) {
    ReagentResource resource = GSON.fromJson(jsonElement, REAGENT_TYPE);
    ScriptorMod.LOGGER.info(
      "Loaded reagent {}. Item: {} Cost: {}",
      resourceLocation.toShortLanguageKey(),
      resource.item,
      resource.cost
    );
    words.put(
      "reagent." + resourceLocation.toShortLanguageKey(),
      WordRegistry.INSTANCE.register(
        "reagent." + resourceLocation.toShortLanguageKey(),
        new ReagentDescriptor(ScriptorItems.ITEMS.getRegistrar().get(new ResourceLocation(resource.item)), resource.cost)
      )
    );
  }
}
