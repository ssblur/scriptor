package com.ssblur.scriptor.events.reloadlisteners;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonElement;
import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.data.components.ScriptorDataComponents;
import com.ssblur.scriptor.data.saved_data.DictionarySavedData;
import com.ssblur.scriptor.data.saved_data.PlayerSpellsSavedData;
import com.ssblur.scriptor.helpers.resource.ScrapResource;
import com.ssblur.scriptor.item.ScriptorItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Type;
import java.util.*;

public class ScrapReloadListener extends ScriptorReloadListener {
  static Random RANDOM = new Random();
  static Type SCRAP_TYPE = new TypeToken<ScrapResource>() {}.getType();

  public static final ScrapReloadListener INSTANCE = new ScrapReloadListener();

  public HashMap<Integer, HashMap<ResourceLocation, String>> tiers = new HashMap<>();

  public ScrapReloadListener() {
    super("scriptor/scraps");
  }

  @Override
  public void loadResource(ResourceLocation resourceLocation, JsonElement jsonElement) {
    ScrapResource resource = GSON.fromJson(jsonElement, SCRAP_TYPE);
    if(!tiers.containsKey(resource.getTier()))
      tiers.put(resource.getTier(), new HashMap<>());
    if(!resource.isDisabled())
      for(var key: resource.getKeys())
        tiers.get(resource.getTier()).put(resourceLocation.withSuffix("." + key.replace(":", ".")), key);
  }

  public HashMap<ResourceLocation, String> getTier(int tier) {
    if(!tiers.containsKey(tier))
      tiers.put(tier, new HashMap<>());
    return tiers.get(tier);
  }

  public String getRandomScrapWord(int tier, Player player) {
    var keys = getTier(tier).keySet();

    if(ScriptorMod.INSTANCE.getCOMMUNITY_MODE()) {
      var level = player.level();
      if(level instanceof ServerLevel serverLevel) {
        int bracket = (int) serverLevel.getSeed() % 5;
        bracket = Math.min(bracket, keys.size());

        Set<ResourceLocation> filteredKeys = new HashSet<>();
        var array = keys.toArray(new ResourceLocation[]{});
        for(int i = 0; i < keys.size(); i++) {
          if(bracket == i % 5)
            filteredKeys.add(array[i]);
        }
        keys = filteredKeys;
      } else {
        return null;
      }
    }

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
    var itemStack = new ItemStack(ScriptorItems.INSTANCE.getSCRAP().get());

    itemStack.set(ScriptorDataComponents.SPELL, key);
    itemStack.set(
      DataComponents.ITEM_NAME,
      Component.literal(Objects.requireNonNull(DictionarySavedData.computeIfAbsent((ServerLevel) player.level()).getWord(key)))
    );
    if(ScriptorMod.INSTANCE.getCOMMUNITY_MODE())
      itemStack.set(ScriptorDataComponents.COMMUNITY_MODE, true);

    return itemStack;
  }
}
