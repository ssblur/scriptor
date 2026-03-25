package com.ssblur.scriptor.data.saved_data

import com.ssblur.scriptor.mixin.DimensionDataStorageAccessor
import com.ssblur.unfocused.serialization.KClassCodec
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

class PlayerScriptionarySavedData: SavedData {
  data class Data(var unlocks: MutableList<String> = mutableListOf(), var observations: MutableList<Pair<String, String>> = mutableListOf())
  var data = Data()
  val unlocks: MutableList<String>
      get() = data.unlocks
  val observations: MutableList<Pair<String, String>>
    get() = data.observations

  constructor()

  constructor(data: Data) {
    this.data = data
  }

  override fun save(tag: CompoundTag, provider: HolderLookup.Provider): CompoundTag {
    CODEC.encodeStart(NbtOps.INSTANCE, this.data).ifSuccess {
      tag.put("scriptor:dictionary_records", it)
    }
    return tag
  }

  companion object {
    val CODEC = KClassCodec.codec(Data::class)

    fun load(tag: CompoundTag, @Suppress("unused_parameter") provider: HolderLookup.Provider?): PlayerScriptionarySavedData? {
      return tag["scriptor:dictionary_records"]?.let {
        CODEC.decode(NbtOps.INSTANCE, it).mapOrElse({
          PlayerScriptionarySavedData(it.first)
        }, {null})
      }
    }

    /**
     * A helper for calling computeIfAbsent for this class from the Overworld
     * @param player Any ServerPlayer
     * @return The PlayerScriptionarySavedData for this world
     */
    fun computeIfAbsent(player: Player): PlayerScriptionarySavedData? {
      val level = player.level()
      val minecraft = level.server ?: return null
      val server = minecraft.getLevel(Level.OVERWORLD) ?: return null

      try {
        val storage = server.dataStorage as DimensionDataStorageAccessor

        if (!Files.exists(storage.dataFolder.toPath().resolve("scriptor_players")))
          Files.createDirectory(storage.dataFolder.toPath().resolve("scriptor_players"))
      } catch (e: IOException) {
        throw RuntimeException(e)
      }
      Objects.requireNonNull(server)
      return server.dataStorage.computeIfAbsent(
        Factory(
          { PlayerScriptionarySavedData() },
          { tag: CompoundTag, provider: HolderLookup.Provider? -> load(tag, provider) },
          DataFixTypes.SAVED_DATA_MAP_DATA
        ),
        "scriptor_players/dictionary_" + player.stringUUID
      )
    }
  }
}
