package com.ssblur.scriptor.events.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class ScriptorStreamCodecs {
  public static final StreamCodec<ByteBuf, Vec3> VEC3 = StreamCodec.composite(
    ByteBufCodecs.DOUBLE,
    Vec3::x,
    ByteBufCodecs.DOUBLE,
    Vec3::y,
    ByteBufCodecs.DOUBLE,
    Vec3::z,
    Vec3::new
  );

  public static final StreamCodec<FriendlyByteBuf, BlockHitResult> BLOCK_HIT_RESULT = StreamCodec.of(
    FriendlyByteBuf::writeBlockHitResult,
    FriendlyByteBuf::readBlockHitResult
  );

  public static final StreamCodec<FriendlyByteBuf, UUID> UUID_CODEC = StreamCodec.of(
    (buf, uuid) -> {
      buf.writeLong(uuid.getMostSignificantBits());
      buf.writeLong(uuid.getLeastSignificantBits());
    },
    buf -> new UUID(buf.readLong(), buf.readLong())
  );

  public static final <T extends Enum<T>> StreamCodec<FriendlyByteBuf, T> fromEnum(Class<T> c) {
    return StreamCodec.of(FriendlyByteBuf::writeEnum, e -> e.readEnum(c));
  }
//    (boolean bl, Vec3 vec3, Direction direction, BlockPos blockPos, boolean bl2)
}
