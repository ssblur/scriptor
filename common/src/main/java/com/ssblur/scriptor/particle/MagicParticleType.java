package com.ssblur.scriptor.particle;

import com.mojang.serialization.Codec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;
import org.jetbrains.annotations.Nullable;

public class MagicParticleType extends ParticleType<MagicParticleData> {
  protected MagicParticleType() {
    super(false, MagicParticleData.DESERIALIZER);
  }

  @Override
  public Codec<MagicParticleData> codec() {
    return MagicParticleData.CODEC;
  }

  public static class Factory implements ParticleProvider<MagicParticleData> {
    private final SpriteSet spriteSet;

    public Factory(SpriteSet sprite) {
      this.spriteSet = sprite;
    }

    @Nullable
    @Override
    public Particle createParticle(MagicParticleData data, ClientLevel level, double d, double e, double f, double g, double h, double i) {
      MagicParticle particle = new MagicParticle(level, d, e, f, g, h, i, 0.15f, data.r, data.g, data.b, 1);
      particle.pickSprite(spriteSet);
      return particle;
    }
  }
}
