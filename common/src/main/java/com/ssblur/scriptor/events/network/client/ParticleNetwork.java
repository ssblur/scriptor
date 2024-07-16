package com.ssblur.scriptor.events.network.client;

import com.ssblur.scriptor.events.network.ScriptorNetwork;
import com.ssblur.scriptor.events.network.ScriptorNetworkInterface;
import com.ssblur.scriptor.events.network.ScriptorStreamCodecs;
import com.ssblur.scriptor.helpers.ParticleQueue;
import com.ssblur.scriptor.particle.MagicParticleData;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.Optional;
import java.util.Random;

public class ParticleNetwork implements ScriptorNetworkInterface<ParticleNetwork.Payload> {
  enum TYPE {
    FIZZLE(new Vector3f(0.2f, 0.2f, 0.2f)),
    WITHER(new Vector3f(0.1f, 0.1f, 0.1f)),
    MAGIC(new Vector3f(0f, 0f, 0f));

    public final Vector3f color;
    TYPE(Vector3f vector3f) {
      color = vector3f;
    }
  }

  @Override
  public CustomPacketPayload.Type<Payload> type() {
    return Payload.TYPE;
  }

  @Override
  public StreamCodec<RegistryFriendlyByteBuf, Payload> streamCodec() {
    return Payload.STREAM_CODEC;
  }

  @Override
  public NetworkManager.Side side() {
    return NetworkManager.Side.S2C;
  }

  @Override
  public void receive(Payload value, NetworkManager.PacketContext context) {
    Minecraft client = Minecraft.getInstance();
    Random random = new Random();
    var type = value.particleType;

    if (client.level != null)
      switch(type) {
        case FIZZLE, WITHER -> {
          var pos = value.from;
          for (int i = 0; i < 9; i++)
            ParticleQueue.queue(
              new DustParticleOptions(type.color, 1.0f),
              pos.x() - 0.25f + random.nextFloat(1.5f),
              pos.y() + 0.5f + random.nextFloat(0.7f),
              pos.z() - 0.25f + random.nextFloat(1.5f)
            );
        }
        case MAGIC -> {
          int c = value.color;
          int r = (c & 0xff0000) >> 16;
          int g = (c & 0x00ff00) >> 8;
          int b = c & 0x0000ff;

          var from = value.from;
          var to = value.to;
          Vec3 pos;
          for (double i = 1; i < 10; i++) {
            pos = from.lerp(to.get(), i / 10d);
            ParticleQueue.queue(
              MagicParticleData.magic(r, g, b),
              pos.x(),
              pos.y(),
              pos.z(),
              0,
              0,
              0
            );
          }
        }
      }
  }

  public static void fizzle(Level level, BlockPos pos) {
    NetworkManager.sendToPlayers(
      level.players().stream().map(player -> (ServerPlayer) player).toList(),
      new Payload(TYPE.FIZZLE, 0, new Vec3(pos.getX(), pos.getY(), pos.getZ()), Optional.empty())
    );
  }

  public static void wither(Level level, BlockPos pos) {
    NetworkManager.sendToPlayers(
      level.players().stream().map(player -> (ServerPlayer) player).toList(),
      new Payload(TYPE.WITHER, 0, new Vec3(pos.getX(), pos.getY(), pos.getZ()), Optional.empty())
    );
  }

  public static void magicTrail(Level level, int color, Vec3 from, Vec3 to) {
    NetworkManager.sendToPlayers(
      level.players().stream().map(player -> (ServerPlayer) player).toList(),
      new Payload(TYPE.MAGIC, color, from, Optional.of(to))
    );
  }

  public record Payload(ParticleNetwork.TYPE particleType, int color, Vec3 from, Optional<Vec3> to) implements CustomPacketPayload {
    public static final Type<Payload> TYPE = new Type<>(ScriptorNetwork.CLIENT_PARTICLE);
    public static final StreamCodec<RegistryFriendlyByteBuf, Payload> STREAM_CODEC = StreamCodec.composite(
      ScriptorStreamCodecs.fromEnum(ParticleNetwork.TYPE.class),
      Payload::particleType,
      ByteBufCodecs.INT,
      Payload::color,
      ScriptorStreamCodecs.VEC3,
      Payload::from,
      ByteBufCodecs.optional(ScriptorStreamCodecs.VEC3),
      Payload::to,
      Payload::new
    );

    @Override
    public Type<Payload> type() {
      return TYPE;
    }
  }
}
