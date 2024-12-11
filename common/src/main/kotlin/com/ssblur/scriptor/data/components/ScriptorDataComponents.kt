package com.ssblur.scriptor.data.components

import com.mojang.serialization.Codec
import com.ssblur.scriptor.ScriptorMod
import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import java.util.function.Consumer

object ScriptorDataComponents {
    val COMPONENTS: DeferredRegister<DataComponentType<*>> =
        DeferredRegister.create(ScriptorMod.MOD_ID, Registries.DATA_COMPONENT_TYPE)

    val EXPIRES: DataComponentType<Long> = registerLong("expires")
    val COMMUNITY_MODE: DataComponentType<Boolean> = registerBool("community_mode")
    val CHARGES: DataComponentType<Int> = registerInt("charge")
    val TOOL_MINING_LEVEL: DataComponentType<Int> = registerInt("tool_mining_level")
    val SPELL: DataComponentType<String?> = register("spell") { builder ->
        builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8)
    }
    val PLAYER_FOCUS = register("player_focus") { builder ->
        builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8)
    }
    val PLAYER_NAME: DataComponentType<String?> = register("player_name") { builder ->
        builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8)
    }
    val TOME_NAME: DataComponentType<String?> = register("tome_name") { builder ->
        builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8)
    }
    val BOOK_OF_BOOKS: DataComponentType<BookOfBooksData> = register("book_of_books") { builder ->
        builder.persistent(BookOfBooksData.CODEC).networkSynchronized(BookOfBooksData.STREAM_CODEC)
    }
    val IDENTIFIED: DataComponentType<List<String>?> = register("identified"){ builder ->
        builder.persistent(Codec.STRING.listOf()).networkSynchronized(ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()))
    }
    val COORDINATES: DataComponentType<List<List<Int>>?> = register("coordinates") { builder ->
        builder.persistent(Codec.INT.listOf().listOf()).networkSynchronized(ByteBufCodecs.INT.apply(ByteBufCodecs.list()).apply(ByteBufCodecs.list()))
    }

    private fun <T> register(name: String, customizer: Consumer<DataComponentType.Builder<T>>): DataComponentType<T> {
        val builder = DataComponentType.builder<T>()
        customizer.accept(builder)
        val componentType = builder.build()
        COMPONENTS.register(name) { componentType }
        return componentType
    }

    private fun registerLong(name: String): DataComponentType<Long> {
        val builder = DataComponentType.builder<Long>()
        builder.persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG)
        val componentType = builder.build()
        COMPONENTS.register(name) { componentType }
        return componentType
    }

    private fun registerBool(name: String): DataComponentType<Boolean> {
        val builder = DataComponentType.builder<Boolean>()
        builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
        val componentType = builder.build()
        COMPONENTS.register(name) { componentType }
        return componentType
    }

    private fun registerInt(name: String): DataComponentType<Int> {
        val builder = DataComponentType.builder<Int>()
        builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT)
        val componentType = builder.build()
        COMPONENTS.register(name) { componentType }
        return componentType
    }

    fun register() {
        COMPONENTS.register()
    }
}
