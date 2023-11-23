package com.ssblur.scriptor.events.reloadlisteners;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.ssblur.scriptor.advancement.ScriptorAdvancements;
import com.ssblur.scriptor.data.PlayerSpellsSavedData;
import com.ssblur.scriptor.helpers.resource.ArtifactResource;
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

public class ArtifactReloadListener extends SimpleJsonResourceReloadListener {
  static ResourceLocation TOMES = new ResourceLocation("data/scriptor/artifacts");
  static Type ARTIFACT_TYPE = new TypeToken<ArtifactResource>() {}.getType();
  static Gson GSON = new Gson();
  static Random RANDOM = new Random();
  public static final ArtifactReloadListener INSTANCE = new ArtifactReloadListener();

  HashMap<ResourceLocation, ArtifactResource> artifacts;
  ArrayList<ResourceLocation> keys;

  ArtifactReloadListener() {
    super(GSON, "scriptor/artifacts");
  }

  @Override
  protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
    artifacts = new HashMap<>();
    keys = new ArrayList<>();
    object.forEach((resourceLocation, jsonElement) -> {
      ArtifactResource resource = GSON.fromJson(jsonElement, ARTIFACT_TYPE);
      if(!resource.isDisabled()) {
        keys.add(resourceLocation);
        artifacts.put(resourceLocation, resource);
      }
    });
  }

  public ArtifactResource getRandomArtifact() {
    var keys = artifacts.keySet();
    return artifacts.get(keys.toArray(new ResourceLocation[]{})[RANDOM.nextInt(keys.size())]);
  }
}
