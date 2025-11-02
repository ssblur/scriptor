package com.ssblur.scriptor.data.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

@JvmRecord
data class DictionaryData(val values: List<List<String>>) {
  companion object {
    val CODEC: Codec<DictionaryData> =
      RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<DictionaryData> ->
        instance.group(
          Codec.STRING.listOf().listOf().fieldOf("values").forGetter{ it.values }
        ).apply(instance) { values -> DictionaryData(values) }
      }
    val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, DictionaryData> = StreamCodec.composite(
      ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()).apply(ByteBufCodecs.list()), DictionaryData::values,
    ) { values -> DictionaryData(values) }
  }

  fun withWord(word: String, definition: String? = null, overwrite: Boolean = false): DictionaryData {
    println(word)
    return if(overwrite || values.none { it.first() == word })
      DictionaryData(values + listOf(listOf(word, definition ?: I18n.get("extra.scriptor.no_entry"))))
    else
      this
  }

  override fun equals(other: Any?): Boolean {
    if(other !is DictionaryData) return false
    return values == other.values
  }

  override fun hashCode(): Int = values.sumOf { li -> li.sumOf { it.hashCode() } }
}
