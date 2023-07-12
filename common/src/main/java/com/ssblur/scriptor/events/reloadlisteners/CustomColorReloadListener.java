package com.ssblur.scriptor.events.reloadlisteners;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.color.CustomColors;
import com.ssblur.scriptor.registry.words.WordRegistry;
import com.ssblur.scriptor.word.descriptor.color.CustomColorDescriptor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class CustomColorReloadListener extends SimpleJsonResourceReloadListener {
  static ResourceLocation COLORS = new ResourceLocation("data/scriptor/colors");
  static Gson GSON = new Gson();
  public static final CustomColorReloadListener INSTANCE = new CustomColorReloadListener();

  public CustomColorReloadListener() {
    super(GSON, "scriptor/colors");
  }

  @Override
  protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
    object.forEach((resourceLocation, jsonElement) -> {
      assert jsonElement.isJsonObject();
      var json = jsonElement.getAsJsonObject();
      assert json.has("color");

      if(json.has("disabled") && json.get("disabled").getAsBoolean())
        return;

      var name = resourceLocation.toShortLanguageKey();
      ScriptorMod.LOGGER.info("Loaded custom color " + name);
      if(json.get("color") instanceof JsonArray array) {
        int[] colors = array
          .asList()
          .stream()
          .map(element -> Integer.parseInt(element.getAsString(), 16))
          .mapToInt(i -> i)
          .toArray();
        CustomColors.registerWithEasing(name, colors);
        WordRegistry.INSTANCE.register(
          "color." + resourceLocation.toShortLanguageKey(),
          new CustomColorDescriptor(name)
        );
      } else if(json.get("color").isJsonPrimitive()) {
        CustomColors.registerWithEasing(
          name,
          new int[]{
            Integer.parseInt(json.get("color").getAsString(), 16)
          }
        );
        WordRegistry.INSTANCE.register(
          "color." + resourceLocation.toShortLanguageKey(),
          new CustomColorDescriptor(name)
        );
      }
    });
  }
}
