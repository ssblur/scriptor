package com.ssblur.scriptor.data.components

import com.mojang.serialization.Codec
import com.ssblur.scriptor.ScriptorMod
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.codec.ByteBufCodecs
import java.util.function.Consumer

object ScriptorDataComponents {
  val EXPIRES: DataComponentType<Long> = registerLong("expires")
  val COMMUNITY_MODE: DataComponentType<Boolean> = registerBool("community_mode")
  val CHARGES: DataComponentType<Int> = registerInt("charge")
  val TOOL_MINING_LEVEL: DataComponentType<Int> = registerInt("tool_mining_level")
  val SPELL = registerString("spell")
  val INVENTORY_CAST: DataComponentType<Boolean> = registerBool("inventory_cast")
  val PLAYER_FOCUS = registerString("player_focus")
  val PLAYER_NAME = registerString("player_name")
  val TOME_NAME = registerString("tome_name")
  val BOOK_OF_BOOKS: DataComponentType<BookOfBooksData> = register("book_of_books") { builder ->
    builder.persistent(BookOfBooksData.CODEC).networkSynchronized(BookOfBooksData.STREAM_CODEC)
  }
  val DICTIONARY_DATA: DataComponentType<DictionaryData> = register("dictionary_data") { builder ->
    builder.persistent(DictionaryData.CODEC).networkSynchronized(DictionaryData.STREAM_CODEC)
  }
  val IDENTIFIED: DataComponentType<List<String>?> = register("identified") { builder ->
    builder.persistent(Codec.STRING.listOf()).networkSynchronized(ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()))
  }
  val COORDINATES: DataComponentType<List<List<Int>>?> = register("coordinates") { builder ->
    builder.persistent(Codec.INT.listOf().listOf())
      .networkSynchronized(ByteBufCodecs.INT.apply(ByteBufCodecs.list()).apply(ByteBufCodecs.list()))
  }

  private fun <T> register(name: String, customizer: Consumer<DataComponentType.Builder<T>>) =
    ScriptorMod.registerDataComponent(name, customizer)

  private fun registerLong(name: String) =
    ScriptorMod.registerDataComponent(name) { it.persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG) }

  private fun registerBool(name: String) =
    ScriptorMod.registerDataComponent(name) { it.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL) }

  private fun registerInt(name: String) =
    ScriptorMod.registerDataComponent(name) { it.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT) }

  private fun registerString(name: String) = ScriptorMod.registerDataComponent(name) {
    it.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8)
  }

  fun register() {}
}
