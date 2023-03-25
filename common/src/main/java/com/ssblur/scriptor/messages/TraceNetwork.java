package com.ssblur.scriptor.messages;

import com.ssblur.scriptor.events.ScriptorEvents;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.subject.TouchSubject;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Callable;

public class TraceNetwork {
  enum TYPE {
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
    FriendlyByteBuf out = new FriendlyByteBuf(Unpooled.buffer());
    out.writeUUID(uuid);
    queue.put(uuid, new TraceQueue(player, callback));
    NetworkManager.sendToPlayer((ServerPlayer) player, ScriptorEvents.GET_TRACE_DATA, out);
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

  public static void getTraceData(FriendlyByteBuf buf, NetworkManager.PacketContext ignoredContext) {
    Minecraft client = Minecraft.getInstance();
    HitResult hit = client.hitResult;
    FriendlyByteBuf out = new FriendlyByteBuf(Unpooled.buffer());

    out.writeUUID(buf.readUUID());
    switch (Objects.requireNonNull(hit).getType()) {
      case BLOCK -> {
        BlockHitResult blockHit = (BlockHitResult) hit;
        out.writeEnum(TYPE.BLOCK);
        out.writeBlockHitResult(blockHit);
      }
      case ENTITY -> {
        EntityHitResult entityHit = (EntityHitResult) hit;
        Entity entity = entityHit.getEntity();
        out.writeEnum(TYPE.ENTITY);
        out.writeInt(entity.getId());
        out.writeUUID(entity.getUUID());
      }
      default -> out.writeEnum(TYPE.MISS);
    }
    NetworkManager.sendToServer(ScriptorEvents.RETURN_TRACE_DATA, out);
  }

  public static void returnTraceData(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
    UUID uuid = buf.readUUID();
    Player player = context.getPlayer();

    TYPE type = buf.readEnum(TYPE.class);
    switch (type){
      case BLOCK -> {
        var result = buf.readBlockHitResult();
        var pos = result.getBlockPos().relative(result.getDirection());
        var targetable = new Targetable(player.level, pos).setFacing(result.getDirection());

        context.queue(() -> validateAndRun(uuid, player, targetable));
      }
      case ENTITY -> {
        int entityId = buf.readInt();
        var entityUUID = buf.readUUID();
        var entity = player.level.getEntity(entityId);
        if(entity != null && entity.getUUID().equals(entityUUID))
          context.queue(() -> validateAndRun(uuid, player, new EntityTargetable(entity)));
        else
          context.queue(() -> validateAndDrop(uuid, player));
      }
      default -> context.queue(() -> validateAndDrop(uuid, player));
    }
  }
}
