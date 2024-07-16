package com.ssblur.scriptor.events.network.server;

import com.ssblur.scriptor.events.network.ScriptorNetwork;
import com.ssblur.scriptor.events.network.ScriptorNetworkInterface;
import com.ssblur.scriptor.events.network.ScriptorStreamCodecs;
import com.ssblur.scriptor.events.network.client.ClientExtendedTraceNetwork;
import com.ssblur.scriptor.events.network.client.ClientTraceNetwork;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class ServerTraceNetwork implements ScriptorNetworkInterface<ServerTraceNetwork.Payload> {
  public enum TYPE {
    BLOCK,
    ENTITY,
    MISS
  }

  public interface TraceCallback {
    void run(Targetable target);
  }

  record TraceQueue(Player player, TraceCallback callback) {}

  static HashMap<UUID, TraceQueue> queue = new HashMap<>();

  public static void requestTraceData(Player player, TraceCallback callback) {
    UUID uuid = UUID.randomUUID();
    RegistryFriendlyByteBuf out = new RegistryFriendlyByteBuf(Unpooled.buffer(), RegistryAccess.EMPTY);
    out.writeUUID(uuid);
    queue.put(uuid, new TraceQueue(player, callback));
    NetworkManager.sendToPlayer((ServerPlayer) player, new ClientTraceNetwork.Payload(uuid));
  }

  public static void requestExtendedTraceData(Player player, TraceCallback callback) {
    UUID uuid = UUID.randomUUID();
    RegistryFriendlyByteBuf out = new RegistryFriendlyByteBuf(Unpooled.buffer(), RegistryAccess.EMPTY);
    out.writeUUID(uuid);
    queue.put(uuid, new TraceQueue(player, callback));
    NetworkManager.sendToPlayer((ServerPlayer) player, new ClientExtendedTraceNetwork.Payload(uuid));
  }

  public static void validateAndRun(UUID uuid, Player player, Targetable targetable) {
    var queueItem = queue.get(uuid);
    if(queueItem.player == player)
      queueItem.callback.run(targetable);
  }

  public static void validateAndDrop(UUID uuid, Player player) {
    var queueItem = queue.get(uuid);
    if(queueItem.player == player)
      queue.remove(uuid);
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
    return NetworkManager.Side.C2S;
  }

  @Override
  public void receive(Payload value, NetworkManager.PacketContext context) {
    UUID uuid = value.uuid;
    Player player = context.getPlayer();

    TYPE type = value.traceType;
    switch (type) {
      case BLOCK -> {
        var result = value.blockHitResult.get();
        var pos = result.getBlockPos().relative(result.getDirection());
        var targetable = new Targetable(player.level(), pos).setFacing(result.getDirection());

        context.queue(() -> validateAndRun(uuid, player, targetable));
      }
      case ENTITY -> {
        int entityId = value.entityId;
        var entityUUID = value.entityUUID.get();
        var level = player.level();
        var entity = level.getEntity(entityId);
        if (entity != null && entity.getUUID().equals(entityUUID))
          context.queue(() -> validateAndRun(uuid, player, new EntityTargetable(entity)));
        else
          context.queue(() -> validateAndDrop(uuid, player));
      }
      default -> context.queue(() -> validateAndDrop(uuid, player));
    }
  }

  public record Payload(
    UUID uuid,
    ServerTraceNetwork.TYPE traceType,
    Optional<BlockHitResult> blockHitResult,
    int entityId,
    Optional<UUID> entityUUID
  ) implements CustomPacketPayload {
    public static final Type<Payload> TYPE = new Type<>(ScriptorNetwork.SERVER_RETURN_TRACE_DATA);
    public static final StreamCodec<RegistryFriendlyByteBuf, Payload> STREAM_CODEC = StreamCodec.composite(
      ScriptorStreamCodecs.UUID_CODEC,
      Payload::uuid,
      ScriptorStreamCodecs.fromEnum(ServerTraceNetwork.TYPE.class),
      Payload::traceType,
      ByteBufCodecs.optional(ScriptorStreamCodecs.BLOCK_HIT_RESULT),
      Payload::blockHitResult,
      ByteBufCodecs.INT,
      Payload::entityId,
      ByteBufCodecs.optional(ScriptorStreamCodecs.UUID_CODEC),
      Payload::entityUUID,
      Payload::new
    );

    @Override
    public Type<Payload> type() {
      return TYPE;
    }
  }
}
