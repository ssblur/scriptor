package com.ssblur.scriptor.messages;

import com.ssblur.scriptor.ScriptorMod;
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

import java.util.Objects;
import java.util.UUID;

public class TouchNetwork {
  enum TOUCHTYPE {
    BLOCK,
    ENTITY,
    MISS
  }

  public static void requestTouchData(Player player, UUID uuid) {
    FriendlyByteBuf out = new FriendlyByteBuf(Unpooled.buffer());
    out.writeUUID(uuid);
    NetworkManager.sendToPlayer((ServerPlayer) player, ScriptorMod.GET_TOUCH_DATA, out);
  }

  public static void getTouchData(FriendlyByteBuf buf, NetworkManager.PacketContext ignoredContext) {
    Minecraft client = Minecraft.getInstance();
    HitResult hit = client.hitResult;
    FriendlyByteBuf out = new FriendlyByteBuf(Unpooled.buffer());

    out.writeUUID(buf.readUUID());
    switch (Objects.requireNonNull(hit).getType()) {
      case BLOCK -> {
        BlockHitResult blockHit = (BlockHitResult) hit;
        out.writeEnum(TOUCHTYPE.BLOCK);
        out.writeBlockHitResult(blockHit);
      }
      case ENTITY -> {
        EntityHitResult entityHit = (EntityHitResult) hit;
        Entity entity = entityHit.getEntity();
        out.writeEnum(TOUCHTYPE.ENTITY);
        out.writeInt(entity.getId());
        out.writeUUID(entity.getUUID());
      }
      default -> out.writeEnum(TOUCHTYPE.MISS);
    }
    NetworkManager.sendToServer(ScriptorMod.RETURN_TOUCH_DATA, out);
  }

  public static void returnTouchData(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
    UUID uuid = buf.readUUID();
    Player player = context.getPlayer();

    TOUCHTYPE type = buf.readEnum(TOUCHTYPE.class);
    switch (type){
      case BLOCK -> {
        var result = buf.readBlockHitResult();
        var pos = result.getBlockPos().relative(result.getDirection());
        var targetable = new Targetable(pos);

        context.queue(() -> TouchSubject.castFromQueue(uuid, targetable, player));
      }
      case ENTITY -> {
        int entityId = buf.readInt();
        var entityUUID = buf.readUUID();
        var entity = player.level.getEntity(entityId);
        if(entity != null && entity.getUUID().equals(entityUUID))
          context.queue(() -> TouchSubject.castFromQueue(uuid, new EntityTargetable(entity), player));
        else
          context.queue(() -> TouchSubject.dropFromQueue(uuid, player));
      }
      default -> context.queue(() -> TouchSubject.dropFromQueue(uuid, player));
    }
  }
}
