package com.ssblur.scriptor.particle;

import com.mojang.serialization.MapCodec;
import com.ssblur.scriptor.ScriptorMod;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.EnvType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class ScriptorParticles {
  public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ScriptorMod.MOD_ID, Registries.PARTICLE_TYPE);

  public static final RegistrySupplier<ParticleType<MagicParticleData>> MAGIC = PARTICLE_TYPES.register(
    "magic",
    () -> new ParticleType<>(true) {
      @Override
      public MapCodec<MagicParticleData> codec() {
        return MagicParticleData.CODEC;
      }

      @Override
      public StreamCodec<? super RegistryFriendlyByteBuf, MagicParticleData> streamCodec() {
        return MagicParticleData.STREAM_CODEC;
      }
    }
  );

  public static void register() {
    if(Platform.getEnv() == EnvType.CLIENT) {
      ParticleProviderRegistry.register(MAGIC, MagicParticle::new);
    }

    PARTICLE_TYPES.register();
  }
}
