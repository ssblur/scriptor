package com.ssblur.scriptor.data_components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record BookOfBooksData(List<ItemStack> items, int active) {
  public static final Codec<BookOfBooksData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
    ItemStack.CODEC.listOf().fieldOf("items").forGetter(data -> data.items),
    Codec.INT.fieldOf("active").forGetter(data -> data.active)
  ).apply(instance, BookOfBooksData::new));
  public static final StreamCodec<RegistryFriendlyByteBuf, BookOfBooksData> STREAM_CODEC = StreamCodec.composite(
    ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), BookOfBooksData::items,
    ByteBufCodecs.VAR_INT, BookOfBooksData::active,
    BookOfBooksData::new
  );
}
