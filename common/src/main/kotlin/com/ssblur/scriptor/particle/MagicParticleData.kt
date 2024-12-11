package com.ssblur.scriptor.particle

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

class MagicParticleData protected constructor(val r: Int, val g: Int, val b: Int) : ParticleOptions {
    override fun getType(): ParticleType<MagicParticleData> {
        return ScriptorParticles.MAGIC.get()
    }

    companion object {
        val CODEC: MapCodec<MagicParticleData> =
            RecordCodecBuilder.mapCodec { instance: RecordCodecBuilder.Instance<MagicParticleData> ->
                instance.group(
                    Codec.INT.fieldOf("r").forGetter { data: MagicParticleData -> data.r },
                    Codec.INT.fieldOf("g").forGetter { data: MagicParticleData -> data.g },
                    Codec.INT.fieldOf("b").forGetter { data: MagicParticleData -> data.b }
                ).apply(instance) { r: Int, g: Int, b: Int -> MagicParticleData(r, g, b) }
            }
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, MagicParticleData> = StreamCodec.composite(
            ByteBufCodecs.INT, { magicParticleData: MagicParticleData -> magicParticleData.r },
            ByteBufCodecs.INT, { magicParticleData: MagicParticleData -> magicParticleData.g },
            ByteBufCodecs.INT, { magicParticleData: MagicParticleData -> magicParticleData.b },
            { r: Int, g: Int, b: Int -> MagicParticleData(r, g, b) }
        )
        fun magic(r: Int, g: Int, b: Int): MagicParticleData {
            return MagicParticleData(r, g, b)
        }
    }
}
