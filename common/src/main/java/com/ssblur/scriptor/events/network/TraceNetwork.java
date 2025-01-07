package com.ssblur.scriptor.events.network;

import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class TraceNetwork {
  enum TYPE {
    BLOCK,
    ENTITY,
    MISS
  }

  public interface TraceCallback {
    void run(Targetable target);
  }

  record TraceQueue(WeakReference<Player> player, TraceCallback callback) {}

  static HashMap<UUID, TraceQueue> queue = new HashMap<>();

  public static void requestTraceData(Player player, TraceCallback callback) {
    UUID uuid = UUID.randomUUID();
    FriendlyByteBuf out = new FriendlyByteBuf(Unpooled.buffer());
    out.writeUUID(uuid);
    queue.put(uuid, new TraceQueue(new WeakReference<>(player), callback));
    NetworkManager.sendToPlayer((ServerPlayer) player, ScriptorNetwork.CLIENT_GET_TRACE_DATA, out);
  }

  public static void requestExtendedTraceData(Player player, TraceCallback callback) {
    UUID uuid = UUID.randomUUID();
    FriendlyByteBuf out = new FriendlyByteBuf(Unpooled.buffer());
    out.writeUUID(uuid);
    queue.put(uuid, new TraceQueue(new WeakReference<>(player), callback));
    NetworkManager.sendToPlayer((ServerPlayer) player, ScriptorNetwork.CLIENT_GET_HITSCAN_DATA, out);
  }

  public static void validateAndRun(UUID uuid, Player player, Targetable targetable) {
    var queueItem = queue.get(uuid);
    if(queueItem.player.get() == player)
      queueItem.callback.run(targetable);
  }

  public static void validateAndDrop(UUID uuid, Player player) {
    var queueItem = queue.get(uuid);
    if(queueItem.player.get() == player)
      queue.remove(uuid);
  }

  public static void getExtendedTraceData(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
    FriendlyByteBuf out = new FriendlyByteBuf(Unpooled.buffer());
    out.writeUUID(buf.readUUID());

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
        out.writeEnum(TYPE.BLOCK);
        out.writeBlockHitResult(blockHitResult);
        NetworkManager.sendToServer(ScriptorNetwork.SERVER_RETURN_TRACE_DATA, out);
        return;
      }
      Entity entity = entityHitResult.getEntity();
      out.writeEnum(TYPE.ENTITY);
      out.writeInt(entity.getId());
      out.writeUUID(entity.getUUID());
      NetworkManager.sendToServer(ScriptorNetwork.SERVER_RETURN_TRACE_DATA, out);
      return;
    }
    if (blockHitResult.getType() != HitResult.Type.MISS) {
      out.writeEnum(TYPE.BLOCK);
      out.writeBlockHitResult(blockHitResult);
      NetworkManager.sendToServer(ScriptorNetwork.SERVER_RETURN_TRACE_DATA, out);
      return;
    }

    out.writeEnum(TYPE.MISS);
    NetworkManager.sendToServer(ScriptorNetwork.SERVER_RETURN_TRACE_DATA, out);
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
    NetworkManager.sendToServer(ScriptorNetwork.SERVER_RETURN_TRACE_DATA, out);
  }

  public static void returnTraceData(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
    UUID uuid = buf.readUUID();
    Player player = context.getPlayer();

    TYPE type = buf.readEnum(TYPE.class);
    switch (type){
      case BLOCK -> {
        var result = buf.readBlockHitResult();
        var pos = result.getBlockPos().relative(result.getDirection());
        var targetable = new Targetable(player.level(), pos).setFacing(result.getDirection());

        context.queue(() -> validateAndRun(uuid, player, targetable));
      }
      case ENTITY -> {
        int entityId = buf.readInt();
        var entityUUID = buf.readUUID();
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
}
