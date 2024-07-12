package com.ssblur.scriptor.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class MagicParticleData implements ParticleOptions {
  public static final MapCodec<MagicParticleData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    Codec.INT.fieldOf("r").forGetter(data -> data.r),
    Codec.INT.fieldOf("g").forGetter(data -> data.g),
    Codec.INT.fieldOf("b").forGetter(data -> data.b)
  ).apply(instance, MagicParticleData::new));

  public static final StreamCodec<RegistryFriendlyByteBuf, MagicParticleData> STREAM_CODEC = StreamCodec.composite(
    ByteBufCodecs.INT, magicParticleData -> magicParticleData.r,
    ByteBufCodecs.INT, magicParticleData -> magicParticleData.g,
    ByteBufCodecs.INT, magicParticleData -> magicParticleData.b,
    MagicParticleData::new
  );

  public final int r, g, b;

  public static MagicParticleData magic(int r, int g, int b) {
    return new MagicParticleData(r, g, b);
  }

  protected MagicParticleData(int r, int g, int b) {
    this.r = r;
    this.g = g;
    this.b = b;
  }

  @Override
  public ParticleType<MagicParticleData> getType() {
    return ScriptorParticles.MAGIC.get();
  }
}
