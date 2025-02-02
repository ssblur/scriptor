package com.ssblur.scriptor.data.saved_data

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.ssblur.scriptor.mixin.DimensionDataStorageAccessor
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.util.datafix.DataFixTypes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.saveddata.SavedData
import java.io.IOException
import java.nio.file.Files
import java.util.*

class PlayerSpellsSavedData: SavedData {
  var spells: HashMap<Int, HashMap<String, Boolean>> = HashMap()
  var scraps: HashMap<Int, HashMap<String, Boolean>> = HashMap()

  constructor(spellList: List<String>, scrapList: List<String>) {
    for (pair in spellList) {
      val split = pair.split(":".toRegex(), limit = 2).toTypedArray()
      val first = split[0].toInt()
      if (!spells.containsKey(first)) spells[first] = HashMap()
      spells[first]!![split[1]] = true
    }

    for (pair in scrapList) {
      val split = pair.split(":".toRegex(), limit = 2).toTypedArray()
      val first = split[0].toInt()
      if (!spells.containsKey(first)) spells[first] = HashMap()
      spells[first]!![split[1]] = true
    }
  }

  constructor()

  fun getTier(tier: Int): HashMap<String, Boolean> {
    if (!spells.containsKey(tier)) spells[tier] = HashMap()
    return spells[tier]!!
  }

  fun getScrapTier(tier: Int): HashMap<String, Boolean> {
    if (!scraps.containsKey(tier)) scraps[tier] = HashMap()
    return scraps[tier]!!
  }

  override fun save(tag: CompoundTag, provider: HolderLookup.Provider): CompoundTag {
    CODEC.encodeStart(NbtOps.INSTANCE, this).ifSuccess { tag.put("scriptor:obtained_spells", it) }
    return tag
  }

  companion object {
    val CODEC: Codec<PlayerSpellsSavedData?> =
      RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<PlayerSpellsSavedData?> ->
        instance.group(
          Codec.STRING.listOf()
            .fieldOf("spells")
            .forGetter { data: PlayerSpellsSavedData? ->
              val list = ArrayList<String>()
              for (i in data!!.spells.keys) for (s in data.spells[i]!!.keys) list.add("$i:$s")
              list
            },
          Codec.STRING.listOf()
            .fieldOf("scraps")
            .forGetter {
              it?.let {
                val list = ArrayList<String>()
                for (i in it.scraps.keys) for (s in it.scraps[i]!!.keys) list.add("$i:$s")
                list
              }
            }
        ).apply(instance) { spellList: List<String>, scrapList: List<String> ->
          PlayerSpellsSavedData(
            spellList,
            scrapList
          )
        }
      }

    fun load(tag: CompoundTag, @Suppress("unused_parameter") provider: HolderLookup.Provider?): PlayerSpellsSavedData? {
      val input = tag["scriptor:obtained_spells"]
      if (input != null) {
        val result = CODEC.decode(NbtOps.INSTANCE, input).result()
        if (result.isPresent && result.get().first != null) return result.get().first
      }
      return null
    }

    /**
     * A helper for calling computeIfAbsent for this class from the Overworld
     * @param player Any ServerPlayer
     * @return The DictionarySavedData for this world
     */
    @JvmStatic
    fun computeIfAbsent(player: Player): PlayerSpellsSavedData? {
      val level = player.level()
      val minecraft = level.server ?: return null
      val server = minecraft.getLevel(Level.OVERWORLD) ?: return null

      try {
        val storage = server.dataStorage as DimensionDataStorageAccessor

        if (!Files.exists(storage.dataFolder.toPath().resolve("scriptor_players"))) Files.createDirectory(
          storage.dataFolder.toPath().resolve("scriptor_players")
        )
      } catch (e: IOException) {
        throw RuntimeException(e)
      }
      Objects.requireNonNull(server)
      return server.dataStorage.computeIfAbsent(
        Factory(
          { PlayerSpellsSavedData() },
          { tag: CompoundTag, provider: HolderLookup.Provider? -> load(tag, provider) },
          DataFixTypes.SAVED_DATA_MAP_DATA
        ),
        "scriptor_players/spells_" + player.stringUUID
      )
    }
  }
}
