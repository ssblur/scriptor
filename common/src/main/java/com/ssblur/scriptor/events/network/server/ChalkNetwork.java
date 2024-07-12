package com.ssblur.scriptor.events.network.server;

import com.ssblur.scriptor.block.ScriptorBlocks;
import com.ssblur.scriptor.blockentity.ChalkBlockEntity;
import com.ssblur.scriptor.blockentity.EngravingBlockEntity;
import com.ssblur.scriptor.events.network.ScriptorNetwork;
import com.ssblur.scriptor.events.network.ScriptorNetworkInterface;
import com.ssblur.scriptor.events.network.ScriptorStreamCodecs;
import com.ssblur.scriptor.item.Chalk;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ChalkNetwork implements ScriptorNetworkInterface<ChalkNetwork.Payload> {
  public static void sendChalkMessage(boolean permanent) {
    Minecraft client = Minecraft.getInstance();
    HitResult hit = client.hitResult;

    if(hit instanceof BlockHitResult blockHitResult)
      NetworkManager.sendToServer(new Payload(blockHitResult, permanent));

  }

  public static void sendChalkMessage() {
    sendChalkMessage(false);
  }

  @Override
  public void receive(Payload value, NetworkManager.PacketContext context) {
    var player = context.getPlayer();
    var itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
    if(!(itemStack.getItem() instanceof Chalk))
      itemStack = player.getItemInHand(InteractionHand.OFF_HAND);
    if(!(itemStack.getItem() instanceof Chalk))
      return;

    var text = itemStack.getHoverName().getString();
    var blockHitResult = value.hitResult;
    var pos = blockHitResult.getBlockPos().relative(blockHitResult.getDirection());
    boolean permanent = value.permanent;
    if(player.level().getBlockState(pos).canBeReplaced()) {
      var level = player.level();
      if(permanent)
        level.setBlock(pos, ScriptorBlocks.ENGRAVING.get().defaultBlockState(), 11);
      else
        level.setBlock(pos, ScriptorBlocks.CHALK.get().defaultBlockState(), 11);

      ChalkBlockEntity blockEntity;
      if(permanent)
        blockEntity = new EngravingBlockEntity(pos, level.getBlockState(pos));
      else
        blockEntity = new ChalkBlockEntity(pos, level.getBlockState(pos));

      blockEntity.setWord(text);
      blockEntity.setFacing(player.getDirection());
      level.setBlockEntity(blockEntity);

      var blockState = level.getBlockState(pos);
      level.sendBlockUpdated(pos, blockState, blockState, 11);
    }
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

  public record Payload(BlockHitResult hitResult, boolean permanent) implements CustomPacketPayload {
    public static final Type<Payload> TYPE = new Type<>(ScriptorNetwork.SERVER_RECEIVE_CHALK_MESSAGE);
    public static final StreamCodec<RegistryFriendlyByteBuf, Payload> STREAM_CODEC = StreamCodec.composite(
      ScriptorStreamCodecs.BLOCK_HIT_RESULT,
      Payload::hitResult,
      ByteBufCodecs.BOOL,
      Payload::permanent,
      Payload::new
    );

    @Override
    public Type<Payload> type() {
      return TYPE;
    }
  }
}
