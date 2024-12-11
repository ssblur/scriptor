package com.ssblur.scriptor.events.network.client;

import com.ssblur.scriptor.events.network.ScriptorNetwork;
import com.ssblur.scriptor.events.network.ScriptorNetworkInterface;
import com.ssblur.scriptor.events.network.ScriptorStreamCodecs;
import com.ssblur.scriptor.network.server.TraceNetwork;
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
        TraceNetwork.INSTANCE.getRETURN_TRACE_DATA().invoke(new TraceNetwork.Payload(
          value.uuid,
          TraceNetwork.TYPE.BLOCK,
          blockHit,
          0,
          null
        ));
      }
      case ENTITY -> {
        EntityHitResult entityHit = (EntityHitResult) hit;
        Entity entity = entityHit.getEntity();
        TraceNetwork.INSTANCE.getRETURN_TRACE_DATA().invoke(new TraceNetwork.Payload(
          value.uuid,
          TraceNetwork.TYPE.ENTITY,
          null,
          entity.getId(),
          entity.getUUID()
        ));
      }
      default ->
        TraceNetwork.INSTANCE.getRETURN_TRACE_DATA().invoke(new TraceNetwork.Payload(
          value.uuid,
          TraceNetwork.TYPE.MISS,
          null,
          0,
          null
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
