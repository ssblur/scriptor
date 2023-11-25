package com.ssblur.scriptor.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;

public class MagicParticleData implements ParticleOptions {
  public static final Codec<MagicParticleData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
    Codec.INT.fieldOf("r").forGetter(data -> data.r),
    Codec.INT.fieldOf("g").forGetter(data -> data.g),
    Codec.INT.fieldOf("b").forGetter(data -> data.b)
  ).apply(instance, MagicParticleData::new));

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

  @Override
  public void writeToNetwork(FriendlyByteBuf buf) {
    buf.writeInt(r);
    buf.writeInt(g);
    buf.writeInt(b);
  }

  @Override
  public String writeToString() {
    return String.format("%s %d %d %d", BuiltInRegistries.PARTICLE_TYPE.getKey(getType()), this.r, this.g, this.b);
  }

  public static final Deserializer<MagicParticleData> DESERIALIZER = new Deserializer<>() {
    @Override
    public MagicParticleData fromCommand(ParticleType<MagicParticleData> type, StringReader reader) throws CommandSyntaxException {
      reader.expect(' ');
      int r = reader.readInt();
      reader.expect(' ');
      int g = reader.readInt();
      reader.expect(' ');
      int b = reader.readInt();
      return new MagicParticleData(r, g, b);
    }

    @Override
    public MagicParticleData fromNetwork(ParticleType<MagicParticleData> type, FriendlyByteBuf buf) {
      return new MagicParticleData(buf.readInt(), buf.readInt(), buf.readInt());
    }
  };
}
