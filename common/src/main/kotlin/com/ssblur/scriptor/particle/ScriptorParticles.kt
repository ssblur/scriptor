package com.ssblur.scriptor.particle

import com.mojang.serialization.MapCodec
import com.ssblur.scriptor.ScriptorMod
import dev.architectury.platform.Platform
import dev.architectury.registry.client.particle.ParticleProviderRegistry
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.fabricmc.api.EnvType
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec

object ScriptorParticles {
    val PARTICLE_TYPES: DeferredRegister<ParticleType<*>> =
        DeferredRegister.create(ScriptorMod.MOD_ID, Registries.PARTICLE_TYPE)

    val MAGIC: RegistrySupplier<ParticleType<MagicParticleData>> = PARTICLE_TYPES.register(
        "magic"
    ) {
        object : ParticleType<MagicParticleData>(true) {
            override fun codec(): MapCodec<MagicParticleData> {
                return MagicParticleData.CODEC
            }

            override fun streamCodec(): StreamCodec<in RegistryFriendlyByteBuf, MagicParticleData> {
                return MagicParticleData.STREAM_CODEC
            }
        }
    }

    fun register() {
        if (Platform.getEnv() == EnvType.CLIENT) {
            ParticleProviderRegistry.register(MAGIC) { data: MagicParticleData?, level: ClientLevel?, d: Double, e: Double, f: Double, xd: Double, yd: Double, zd: Double ->
                MagicParticle(
                    data!!, level, d, e, f, xd, yd, zd
                )
            }
        }

        PARTICLE_TYPES.register()
    }
}
