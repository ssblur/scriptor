package com.ssblur.scriptor.events.network.client;

import com.ssblur.scriptor.events.network.ScriptorNetwork;
import com.ssblur.scriptor.events.network.ScriptorNetworkInterface;
import com.ssblur.scriptor.events.network.ScriptorStreamCodecs;
import com.ssblur.scriptor.events.network.server.ServerTraceNetwork;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;

import java.util.Optional;
import java.util.UUID;

public class ClientExtendedTraceNetwork implements ScriptorNetworkInterface<ClientExtendedTraceNetwork.Payload> {
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
    var player = context.getPlayer();
    var level = player.level();
    var position = player.getEyePosition();
    var angle = player.getLookAngle().normalize().multiply(20, 20, 20);
    var dest = angle.add(position);
    var blockHitResult = level.clip(new ClipContext(position, dest, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));

    var entityHitResult = ProjectileUtil.getEntityHitResult(
      level,
      player,
      position,
      dest,
      AABB.ofSize(position.subtract(0.1, 0.1, 0.1), 0.2, 0.2, 0.2).expandTowards(angle).inflate(1),
      e -> true
    );
    if (entityHitResult != null && entityHitResult.getType() != HitResult.Type.MISS) {
      if (blockHitResult.getType() != HitResult.Type.MISS && blockHitResult.distanceTo(player) < entityHitResult.distanceTo(player)) {

        NetworkManager.sendToServer(new ServerTraceNetwork.Payload(
          value.uuid,
          ServerTraceNetwork.TYPE.BLOCK,
          Optional.of(blockHitResult),
          0,
          Optional.empty()
        ));
        return;
      }
      Entity entity = entityHitResult.getEntity();
      NetworkManager.sendToServer(new ServerTraceNetwork.Payload(
        value.uuid,
        ServerTraceNetwork.TYPE.ENTITY,
        Optional.of(blockHitResult),
        entity.getId(),
        Optional.of(entity.getUUID())
      ));
      return;
    }
    if (blockHitResult.getType() != HitResult.Type.MISS) {
      NetworkManager.sendToServer(new ServerTraceNetwork.Payload(
        value.uuid,
        ServerTraceNetwork.TYPE.BLOCK,
        Optional.of(blockHitResult),
        0,
        Optional.empty()
      ));
      return;
    }

    NetworkManager.sendToServer(new ServerTraceNetwork.Payload(
      value.uuid,
      ServerTraceNetwork.TYPE.MISS,
      Optional.of(blockHitResult),
      0,
      Optional.empty()
    ));
  }

  public record Payload(UUID uuid) implements CustomPacketPayload {
    public static final Type<Payload> TYPE = new Type<>(ScriptorNetwork.CLIENT_GET_HITSCAN_DATA);
    public static final StreamCodec<RegistryFriendlyByteBuf, Payload> STREAM_CODEC = StreamCodec.composite(
      ScriptorStreamCodecs.UUID_CODEC,
      Payload::uuid,
      Payload::new
    );

    @Override
    public Type<Payload> type() {
      return TYPE;
    }
  }
}
