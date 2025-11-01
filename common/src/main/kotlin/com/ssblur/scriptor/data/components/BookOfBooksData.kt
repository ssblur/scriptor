package com.ssblur.scriptor.data.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack

@JvmRecord
data class BookOfBooksData(val items: List<ItemStack>, val active: Int) {
  companion object {
    val CODEC: Codec<BookOfBooksData> =
      RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<BookOfBooksData> ->
        instance.group(
          ItemStack.CODEC.listOf().fieldOf("items").forGetter { data: BookOfBooksData -> data.items },
          Codec.INT.fieldOf("active").forGetter { data: BookOfBooksData -> data.active }
        ).apply(instance) { items: List<ItemStack>, active: Int -> BookOfBooksData(items, active) }
      }
    val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, BookOfBooksData> = StreamCodec.composite(
      ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), BookOfBooksData::items,
      ByteBufCodecs.VAR_INT, BookOfBooksData::active
    ) { items: List<ItemStack>, active: Int -> BookOfBooksData(items, active) }
  }

  override fun equals(other: Any?): Boolean {
    if(other !is BookOfBooksData) return false
    if(other.items.zip(items).any{ it.first != it.second }) return false
    return true
  }

  override fun hashCode(): Int = items.sumOf { it.hashCode() }
}
