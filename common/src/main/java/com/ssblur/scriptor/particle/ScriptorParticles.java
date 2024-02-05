package com.ssblur.scriptor.particle;

import com.ssblur.scriptor.ScriptorMod;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.EnvType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;

public class ScriptorParticles {
  public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ScriptorMod.MOD_ID, Registries.PARTICLE_TYPE);

  public static final RegistrySupplier<ParticleType<MagicParticleData>> MAGIC = PARTICLE_TYPES.register(
    "magic",
    MagicParticleType::new
  );

  public static void register() {
    PARTICLE_TYPES.register();

    if(Platform.getEnv() == EnvType.CLIENT) {
      ParticleProviderRegistry.register(MAGIC, MagicParticleType.Factory::new);
    }
  }
}
