package com.ssblur.scriptor.helpers;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PlayerSpellsSavedData extends SavedData {
  public HashMap<Integer, HashMap<String, Boolean>> spells = new HashMap<>();
  public static final Codec<PlayerSpellsSavedData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
    Codec.STRING.listOf()
      .fieldOf("spells")
      .forGetter(
        data -> {
          ArrayList<String> list = new ArrayList<>();
          for(int i: data.spells.keySet())
            for(var s: data.spells.get(i).keySet())
              list.add(i + ":" + s);
          return list;
        }
      )
  ).apply(instance, PlayerSpellsSavedData::new));

  public PlayerSpellsSavedData(List<String> pairs) {
    for(var pair: pairs) {
      var split = pair.split(":", 2);
      int first = Integer.parseInt(split[0]);
      if(!spells.containsKey(first))
        spells.put(first, new HashMap<>());
      spells.get(first).put(split[1], true);
    }
  }

  public PlayerSpellsSavedData() {}

  public HashMap<String, Boolean> getTier(int tier) {
    if(!spells.containsKey(tier))
      spells.put(tier, new HashMap<>());
    return spells.get(tier);
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    var result = CODEC.encodeStart(NbtOps.INSTANCE, this).get().left();
    result.ifPresent(value -> tag.put("scriptor:obtained_spells", value));
    return tag;
  }

  public static PlayerSpellsSavedData load(CompoundTag tag) {
    var input = tag.get("scriptor:obtained_spells");
    if(input != null) {
      var result = CODEC.decode(NbtOps.INSTANCE, input).get().left();
      if(result.isPresent() && result.get().getFirst() != null)
        return result.get().getFirst();
    }
    return null;
  }

  /**
   * A helper for calling computeIfAbsent for this class from the Overworld
   * @param player Any ServerPlayer
   * @return The DictionarySavedData for this world
   */
  @Nullable
  public static PlayerSpellsSavedData computeIfAbsent(Player player) {
    MinecraftServer minecraft;
    var level = player.level();
    minecraft = level.getServer();
    if(minecraft == null) return null;

    var server = minecraft.getLevel(Level.OVERWORLD);
    if(server == null) return null;

    try {
      var storage = server.getDataStorage();
      if(storage == null) return null;

      if(!Files.exists(storage.dataFolder.toPath().resolve("scriptor_players")))
        Files.createDirectory(storage.dataFolder.toPath().resolve("scriptor_players"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Objects.requireNonNull(server);
    return server.getDataStorage().computeIfAbsent(
      PlayerSpellsSavedData::load,
      PlayerSpellsSavedData::new,
      "scriptor_players/spells_" + player.getStringUUID()
    );
  }
}
