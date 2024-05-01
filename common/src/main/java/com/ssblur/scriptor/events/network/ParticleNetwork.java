package com.ssblur.scriptor.events.network;

import com.ssblur.scriptor.helpers.ParticleQueue;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import java.util.Random;

public class ParticleNetwork {
  enum TYPE {
    FIZZLE(new Vector3f(0.2f, 0.2f, 0.2f)),
    WITHER(new Vector3f(0.1f, 0.1f, 0.1f));

    public final Vector3f color;
    TYPE(Vector3f vector3f) {
      color = vector3f;
    }
  }

  public static void fizzle(Level level, BlockPos pos) {
    FriendlyByteBuf out = new FriendlyByteBuf(Unpooled.buffer());

    out.writeEnum(TYPE.FIZZLE);
    out.writeBlockPos(pos);

    NetworkManager.sendToPlayers(
      level.players().stream().map(player -> (ServerPlayer) player).toList(),
      ScriptorNetwork.CLIENT_PARTICLE,
      out
    );
  }

  public static void wither(Level level, BlockPos pos) {
    FriendlyByteBuf out = new FriendlyByteBuf(Unpooled.buffer());

    out.writeEnum(TYPE.WITHER);
    out.writeBlockPos(pos);

    NetworkManager.sendToPlayers(
      level.players().stream().map(player -> (ServerPlayer) player).toList(),
      ScriptorNetwork.CLIENT_PARTICLE,
      out
    );
  }

  public static void getParticles(FriendlyByteBuf buf, NetworkManager.PacketContext ignoredContext) {
    Minecraft client = Minecraft.getInstance();
    Random random = new Random();
    var type = buf.readEnum(TYPE.class);
    var pos = buf.readBlockPos();

    if (client.level != null)
      switch(type) {
        case FIZZLE, WITHER -> {
          for (int i = 0; i < 9; i++)
            ParticleQueue.queue(
              new DustParticleOptions(type.color, 1.0f),
              pos.getX() - 0.25f + random.nextFloat(1.5f),
              pos.getY() + 0.5f + random.nextFloat(0.7f),
              pos.getZ() - 0.25f + random.nextFloat(1.5f)
            );
        }
      }
  }
}
