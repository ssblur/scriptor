package com.ssblur.scriptor.events.reloadlisteners;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.ssblur.scriptor.data.DictionarySavedData;
import com.ssblur.scriptor.data.PlayerSpellsSavedData;
import com.ssblur.scriptor.helpers.resource.ScrapResource;
import com.ssblur.scriptor.item.ScriptorItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ScrapReloadListener extends SimpleJsonResourceReloadListener {
  static ResourceLocation SCRAPS = new ResourceLocation("data/scriptor/scraps");
  static Gson GSON = new Gson();
  static Random RANDOM = new Random();
  static Type SCRAP_TYPE = new TypeToken<ScrapResource>() {}.getType();

  public static final ScrapReloadListener INSTANCE = new ScrapReloadListener();

  public HashMap<Integer, HashMap<ResourceLocation, String>> tiers = new HashMap<>();

  public ScrapReloadListener() {
    super(GSON, "scriptor/scraps");
  }

  @Override
  protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
    object.forEach((resourceLocation, jsonElement) -> {
      ScrapResource resource = GSON.fromJson(jsonElement, SCRAP_TYPE);
      if(!tiers.containsKey(resource.getTier()))
        tiers.put(resource.getTier(), new HashMap<>());
      if(!resource.isDisabled())
        for(var key: resource.getKeys())
          tiers.get(resource.getTier()).put(resourceLocation.withSuffix("." + key.replace(":", ".")), key);
    });
  }

  public HashMap<ResourceLocation, String> getTier(int tier) {
    if(!tiers.containsKey(tier))
      tiers.put(tier, new HashMap<>());
    return tiers.get(tier);
  }

  public String getRandomScrapWord(int tier, Player player) {
    var keys = getTier(tier).keySet();
    var data = PlayerSpellsSavedData.computeIfAbsent(player);
    if(data != null) {
      var scraps = data.getScrapTier(tier);

      var keysArray = keys.toArray(new ResourceLocation[]{});
      ResourceLocation resource;
      do {
        resource = keysArray[RANDOM.nextInt(keys.size())];
      } while (scraps.containsKey(resource.toShortLanguageKey()) && scraps.size() < keys.size());
      scraps.put(resource.toShortLanguageKey(), true);
      data.setDirty();

      return tiers.get(tier).get(resource);
    }
    return tiers.get(tier).get(keys.toArray(new ResourceLocation[]{})[RANDOM.nextInt(keys.size())]);
  }

  public ItemStack getRandomScrap(int tier, Player player) {
    var key = getRandomScrapWord(tier, player);

    var scriptor = new CompoundTag();
    scriptor.putString("spell", key);
    scriptor.putString("word", DictionarySavedData.computeIfAbsent((ServerLevel) player.level()).getWord(key));

    var itemStack = new ItemStack(ScriptorItems.SCRAP.get());
    var tag = itemStack.getOrCreateTag();
    tag.put("scriptor", scriptor);

    return itemStack;
  }
}
