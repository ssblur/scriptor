package com.ssblur.scriptor.events.reloadlisteners;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonElement;
import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.advancement.ScriptorAdvancements;
import com.ssblur.scriptor.data.saved_data.PlayerSpellsSavedData;
import com.ssblur.scriptor.helpers.resource.TomeResource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;

import java.lang.reflect.Type;
import java.util.*;

@SuppressWarnings("unused")
public class TomeReloadListener extends ScriptorReloadListener {
  static Type TOME_TYPE = new TypeToken<TomeResource>() {}.getType();
  static Random RANDOM = new Random();
  public static final TomeReloadListener INSTANCE = new TomeReloadListener();

  HashMap<Integer, HashMap<ResourceLocation, TomeResource>> tomes;
  ArrayList<ResourceLocation> keys;

  TomeReloadListener() {
    super("scriptor/tomes");
  }

  public HashMap<Integer, HashMap<ResourceLocation, TomeResource>> getTiers() {
    return tomes;
  }

  @Override
  protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
    tomes = new HashMap<>();
    keys = new ArrayList<>();
    super.apply(object, resourceManager, profilerFiller);
  }

  public void loadResource(ResourceLocation resourceLocation, JsonElement jsonElement) {
    TomeResource resource = GSON.fromJson(jsonElement, TOME_TYPE);
    if(!tomes.containsKey(resource.getTier()))
      tomes.put(resource.getTier(), new HashMap<>());
    keys.add(resourceLocation);
    tomes.get(resource.getTier()).put(resourceLocation, resource);
  }

  public TomeResource getRandomTome(int tier) {
    var keys = tomes.get(tier).keySet();
    return tomes.get(tier).get(keys.toArray(new ResourceLocation[]{})[RANDOM.nextInt(keys.size())]);
  }

  public TomeResource getRandomTome(int tier, Player player) {
    var keys = tomes.get(tier).keySet();

    if(ScriptorMod.INSTANCE.getCOMMUNITY_MODE()) {
      var level = player.level();
      if(level instanceof ServerLevel serverLevel) {
        int bracket = (int) serverLevel.getSeed() % 5;
        bracket += 10;
        bracket %= 5;
        bracket = Math.min(bracket, keys.size() - 1);

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
      var spells = data.getTier(tier);

      ScriptorAdvancements.INSTANCE.getTOME().get().trigger((ServerPlayer) player);
      if (keys.size() <= spells.size())
        return tomes.get(tier).get(keys.toArray(new ResourceLocation[]{})[RANDOM.nextInt(keys.size())]);
      else if (keys.size() <= spells.size() + 1) {
        if (tier == 1) ScriptorAdvancements.INSTANCE.getTOME_1().get().trigger((ServerPlayer) player);
        if (tier == 2) ScriptorAdvancements.INSTANCE.getTOME_2().get().trigger((ServerPlayer) player);
        if (tier == 3) ScriptorAdvancements.INSTANCE.getTOME_3().get().trigger((ServerPlayer) player);
        if (tier == 4) ScriptorAdvancements.INSTANCE.getTOME_4().get().trigger((ServerPlayer) player);
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
