package com.ssblur.scriptor.particle

import com.mojang.serialization.MapCodec
import com.ssblur.scriptor.ScriptorMod
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

  fun register() {}
}
