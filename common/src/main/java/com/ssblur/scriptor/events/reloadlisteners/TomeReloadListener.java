package com.ssblur.scriptor.events.reloadlisteners;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.ssblur.scriptor.advancement.ScriptorAdvancements;
import com.ssblur.scriptor.data.PlayerSpellsSavedData;
import com.ssblur.scriptor.helpers.resource.TomeResource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TomeReloadListener extends SimpleJsonResourceReloadListener {
  static ResourceLocation TOMES = new ResourceLocation("data/scriptor/tomes");
  static Type TOME_TYPE = new TypeToken<TomeResource>() {}.getType();
  static Gson GSON = new Gson();
  static Random RANDOM = new Random();
  public static final TomeReloadListener INSTANCE = new TomeReloadListener();

  HashMap<Integer, HashMap<ResourceLocation, TomeResource>> tomes;
  ArrayList<ResourceLocation> keys;

  TomeReloadListener() {
    super(GSON, "scriptor/tomes");
  }

  public HashMap<Integer, HashMap<ResourceLocation, TomeResource>> getTiers() {
    return tomes;
  }

  @Override
  protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
    tomes = new HashMap<>();
    keys = new ArrayList<>();
    object.forEach((resourceLocation, jsonElement) -> {
      TomeResource resource = GSON.fromJson(jsonElement, TOME_TYPE);
      if(!tomes.containsKey(resource.getTier()))
        tomes.put(resource.getTier(), new HashMap<>());
      if(!resource.isDisabled()) {
        keys.add(resourceLocation);
        tomes.get(resource.getTier()).put(resourceLocation, resource);
      }
    });
  }

  public TomeResource getRandomTome(int tier) {
    var keys = tomes.get(tier).keySet();
    return tomes.get(tier).get(keys.toArray(new ResourceLocation[]{})[RANDOM.nextInt(keys.size())]);
  }

  public TomeResource getRandomTome(int tier, Player player) {
    var keys = tomes.get(tier).keySet();
    var data = PlayerSpellsSavedData.computeIfAbsent(player);
    if(data != null) {
      var spells = data.getTier(tier);

      ScriptorAdvancements.TOME.get().trigger((ServerPlayer) player);
      if (keys.size() <= spells.size())
        return tomes.get(tier).get(keys.toArray(new ResourceLocation[]{})[RANDOM.nextInt(keys.size())]);
      else if (keys.size() <= spells.size() + 1) {
        if (tier == 1) ScriptorAdvancements.TOME_1.get().trigger((ServerPlayer) player);
        if (tier == 2) ScriptorAdvancements.TOME_2.get().trigger((ServerPlayer) player);
        if (tier == 3) ScriptorAdvancements.TOME_3.get().trigger((ServerPlayer) player);
        if (tier == 4) ScriptorAdvancements.TOME_4.get().trigger((ServerPlayer) player);
      }


      var keysArray = keys.toArray(new ResourceLocation[]{});
      int maxAttempts = 10;
      ResourceLocation resource;
      do {
        resource = keysArray[RANDOM.nextInt(keys.size())];
        maxAttempts--;
        if(maxAttempts <= 0) break;
      } while (spells.containsKey(resource.toShortLanguageKey()));
      spells.put(resource.toShortLanguageKey(), true);
      data.setDirty();

      return tomes.get(tier).get(resource);
    }
    return tomes.get(tier).get(keys.toArray(new ResourceLocation[]{})[RANDOM.nextInt(keys.size())]);
  }
}
