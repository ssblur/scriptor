package com.ssblur.scriptor.events.network.client;

import com.ssblur.scriptor.events.network.ScriptorNetwork;
import com.ssblur.scriptor.events.network.ScriptorNetworkInterface;
import com.ssblur.scriptor.events.network.ScriptorStreamCodecs;
import com.ssblur.scriptor.events.network.server.ServerTraceNetwork;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class ClientTraceNetwork implements ScriptorNetworkInterface<ClientTraceNetwork.Payload> {
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
    HitResult hit = client.hitResult;

    switch (Objects.requireNonNull(hit).getType()) {
      case BLOCK -> {
        BlockHitResult blockHit = (BlockHitResult) hit;
        NetworkManager.sendToServer(new ServerTraceNetwork.Payload(
          value.uuid,
          ServerTraceNetwork.TYPE.BLOCK,
          Optional.of(blockHit),
          0,
          Optional.empty()
        ));
      }
      case ENTITY -> {
        EntityHitResult entityHit = (EntityHitResult) hit;
        Entity entity = entityHit.getEntity();
        NetworkManager.sendToServer(new ServerTraceNetwork.Payload(
          value.uuid,
          ServerTraceNetwork.TYPE.ENTITY,
          Optional.empty(),
          entity.getId(),
          Optional.of(entity.getUUID())
        ));
      }
      default ->
        NetworkManager.sendToServer(new ServerTraceNetwork.Payload(
          value.uuid,
          ServerTraceNetwork.TYPE.MISS,
          Optional.empty(),
          0,
          Optional.empty()
        ));
    }

  }

  public record Payload(UUID uuid) implements CustomPacketPayload {
    public static final Type<Payload> TYPE = new Type<>(ScriptorNetwork.CLIENT_GET_TRACE_DATA);
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
