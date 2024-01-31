package com.ssblur.scriptor.events.reloadlisteners;

import com.google.common.reflect.TypeToken;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.item.ScriptorItems;
import com.ssblur.scriptor.registry.words.WordRegistry;
import com.ssblur.scriptor.word.Word;
import com.ssblur.scriptor.word.action.Action;
import com.ssblur.scriptor.word.action.CommandAction;
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

public class WordReloadListener extends SimpleJsonResourceReloadListener {
  static class WordResource {
    int cost;
    boolean disabled;
    String[] castAtPosition;
    String[] castOnEntity = new String[0];
    String[] castOnItem = new String[0];

    WordResource(int cost, boolean disabled, String[] castAtPosition) {
      this.disabled = disabled;
      this.cost = cost;
      this.castAtPosition = castAtPosition;
    }

    WordResource(int cost, boolean disabled, String[] castAtPosition, String[] castOnEntity) {
      this(cost, disabled, castAtPosition);
      this.castOnEntity = castOnEntity;
    }

    WordResource(int cost, boolean disabled, String[] castAtPosition, String[] castOnEntity, String[] castOnItem) {
      this(cost, disabled, castAtPosition, castOnEntity);
      this.castOnItem = castOnItem;
    }
  }

  static ResourceLocation WORDS = new ResourceLocation("data/scriptor/words");
  static Type WORD_TYPE = new TypeToken<WordResource>() {}.getType();
  static Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
  static Random RANDOM = new Random();
  public static final WordReloadListener INSTANCE = new WordReloadListener();

  HashMap<String, Action> words;

  WordReloadListener() {
    super(GSON, "scriptor/words");
  }

  @Override
  protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
    words = new HashMap<>();
    object.forEach((resourceLocation, jsonElement) -> {
      WordResource resource = GSON.fromJson(jsonElement, WORD_TYPE);
      System.out.println(jsonElement);
      ScriptorMod.LOGGER.info(
        "Loaded word {}{}. Cost: {}",
        resource.disabled ? " (disabled)" : "",
        resourceLocation.toShortLanguageKey(),
        resource.cost
      );
      if(!resource.disabled)
        words.put( "action." + resourceLocation.toShortLanguageKey(),
          WordRegistry.INSTANCE.register(
            "action." + resourceLocation.toShortLanguageKey(),
            new CommandAction(resource.cost, resource.castAtPosition, resource.castOnEntity, resource.castOnItem)
          )
        );

    });
  }
}
