package com.ssblur.scriptor.particle

import com.mojang.serialization.MapCodec
import com.ssblur.scriptor.ScriptorMod
import com.ssblur.unfocused.rendering.ParticleFactories.registerFactory
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.core.particles.ParticleType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec

object ScriptorParticles {
    val MAGIC = ScriptorMod.registerParticleType("magic") {
        object: ParticleType<MagicParticleData>(true) {
            override fun codec(): MapCodec<MagicParticleData> {
                return MagicParticleData.CODEC
            }

            override fun streamCodec(): StreamCodec<in RegistryFriendlyByteBuf, MagicParticleData> {
                return MagicParticleData.STREAM_CODEC
            }
        }
    }

    fun register() {
        try{ registerClient() } catch(_: NoSuchMethodError) {}
    }

    fun registerClient() {
        MAGIC.registerFactory{ data: MagicParticleData?, level: ClientLevel?, d: Double, e: Double, f: Double, xd: Double, yd: Double, zd: Double ->
            MagicParticle(
                data!!, level, d, e, f, xd, yd, zd
            )
        }
    }
}
