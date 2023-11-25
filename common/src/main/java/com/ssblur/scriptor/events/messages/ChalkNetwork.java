package com.ssblur.scriptor.events.messages;

import com.ssblur.scriptor.block.ChalkBlock;
import com.ssblur.scriptor.block.ScriptorBlocks;
import com.ssblur.scriptor.blockentity.ChalkBlockEntity;
import com.ssblur.scriptor.events.ScriptorEvents;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.item.Chalk;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class ChalkNetwork {
  public static void sendChalkMessage() {
    Minecraft client = Minecraft.getInstance();
    HitResult hit = client.hitResult;

    if(hit instanceof BlockHitResult blockHitResult) {
      FriendlyByteBuf out = new FriendlyByteBuf(Unpooled.buffer());
      out.writeBlockHitResult(blockHitResult);
      NetworkManager.sendToServer(ScriptorEvents.RECEIVE_CHALK_MESSAGE, out);
    }

  }

  public static void receiveChalkMessage(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
    var player = context.getPlayer();
    var itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
    if(!(itemStack.getItem() instanceof Chalk))
      itemStack = player.getItemInHand(InteractionHand.OFF_HAND);
    if(!(itemStack.getItem() instanceof Chalk))
      return;

    var text = itemStack.getHoverName().getString();
    var blockHitResult = buf.readBlockHitResult();
    var pos = blockHitResult.getBlockPos().relative(blockHitResult.getDirection());
    if(player.level().getBlockState(pos).canBeReplaced()) {
      player.level().setBlock(pos, ScriptorBlocks.CHALK.get().defaultBlockState(), 11);

      var blockEntity = new ChalkBlockEntity(pos, player.level().getBlockState(pos));
      blockEntity.setWord(text);
      blockEntity.setFacing(player.getDirection());
      player.level().setBlockEntity(blockEntity);
    }
  }
}
