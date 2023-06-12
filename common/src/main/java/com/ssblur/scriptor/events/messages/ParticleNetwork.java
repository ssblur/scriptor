package com.ssblur.scriptor.events.messages;

import com.ssblur.scriptor.events.ScriptorEvents;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.apache.logging.log4j.core.jmx.Server;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class ParticleNetwork {
  enum TYPE {
    FIZZLE(new Vector3f(0.2f, 0.2f, 0.2f));

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
      ScriptorEvents.PARTICLE,
      out
    );
  }

  public static void getParticles(FriendlyByteBuf buf, NetworkManager.PacketContext ignoredContext) {
    Minecraft client = Minecraft.getInstance();
    Random random = new Random();
    var type = buf.readEnum(TYPE.class);
    var pos = buf.readBlockPos();

    if(type == TYPE.FIZZLE) {
      if (client.level != null)
        for (int i = 0; i < 9; i++)
          client.level.addParticle(
            new DustParticleOptions(type.color, 1.0f),
            pos.getX() - 0.25f + random.nextFloat(1.5f),
            pos.getY() + 0.5f + random.nextFloat(0.7f),
            pos.getZ() - 0.25f + random.nextFloat(1.5f),
            0,
            0,
            0
          );
    }
  }
}
