package com.ssblur.scriptor.data_components;

import com.mojang.serialization.Codec;
import com.ssblur.scriptor.ScriptorMod;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;

import java.util.List;
import java.util.function.Consumer;

public class ScriptorDataComponents {
  public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create(ScriptorMod.MOD_ID, Registries.DATA_COMPONENT_TYPE);

  public static final DataComponentType<Long> EXPIRES = registerLong("expires");
  public static final DataComponentType<Boolean> COMMUNITY_MODE = registerBool("community_mode");
  public static final DataComponentType<Integer> CHARGES = registerInt("charge");
  public static final DataComponentType<String> SPELL = register("spell",
    builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8)
  );
  public static final DataComponentType<String> PLAYER_FOCUS = register("player_focus",
    builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8)
  );
  public static final DataComponentType<String> PLAYER_NAME = register("player_name",
    builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8)
  );
  public static final DataComponentType<String> TOME_NAME = register("tome_name",
    builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8)
  );
  public static final DataComponentType<BookOfBooksData> BOOK_OF_BOOKS = register("book_of_books",
    builder -> builder.persistent(BookOfBooksData.CODEC).networkSynchronized(BookOfBooksData.STREAM_CODEC)
  );
  public static final DataComponentType<List<String>> IDENTIFIED = register("identified",
    builder -> builder.persistent(Codec.STRING.listOf()).networkSynchronized(ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()))
  );
  public static final DataComponentType<List<List<Integer>>> COORDINATES = register("coordinates",
    builder -> builder.persistent(Codec.INT.listOf().listOf()).networkSynchronized(ByteBufCodecs.INT.apply(ByteBufCodecs.list()).apply(ByteBufCodecs.list()))
  );

  private static <T> DataComponentType<T> register(String name, Consumer<DataComponentType.Builder<T>> customizer) {
    var builder = DataComponentType.<T>builder();
    customizer.accept(builder);
    var componentType = builder.build();
    COMPONENTS.register(name, () -> componentType);
    return componentType;
  }

  private static DataComponentType<Long> registerLong(String name) {
    var builder = DataComponentType.<Long>builder();
    builder.persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG);
    var componentType = builder.build();
    COMPONENTS.register(name, () -> componentType);
    return componentType;
  }

  private static DataComponentType<Boolean> registerBool(String name) {
    var builder = DataComponentType.<Boolean>builder();
    builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL);
    var componentType = builder.build();
    COMPONENTS.register(name, () -> componentType);
    return componentType;
  }

  private static DataComponentType<Integer> registerInt(String name) {
    var builder = DataComponentType.<Integer>builder();
    builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT);
    var componentType = builder.build();
    COMPONENTS.register(name, () -> componentType);
    return componentType;
  }

  public static void register() {
    COMPONENTS.register();
  }
}
