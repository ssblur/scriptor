package com.ssblur.scriptor.blockentity;

import com.ssblur.scriptor.block.ScriptorBlockTags;
import com.ssblur.scriptor.block.ScriptorBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PhasedBlockBlockEntity extends BlockEntity {
  static final int ANIM_DURATION = 5;
  CompoundTag data;
  BlockState blockState;
  int countdown;
  long created;

  public PhasedBlockBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(ScriptorBlockEntities.PHASED_BLOCK.get(), blockPos, blockState);
    created = -1;
    countdown = -1;
  }

  public void tick() {
    countdown--;
    if(countdown <= 0 && level != null) {
      level.setBlockAndUpdate(getBlockPos(), blockState);
      if(data != null && !level.isClientSide) {
        var entity = BlockEntity.loadStatic(getBlockPos(), blockState, data, level.registryAccess());
        if(entity != null)
          level.setBlockEntity(entity);
      }
    }
  }

  public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T entity) {
    if(entity instanceof PhasedBlockBlockEntity tile) tile.tick();
  }

  public static void phase(Level level, BlockPos pos) {
    phase(level, pos, 5);
  }

  public static void phase(Level level, BlockPos pos, int duration) {
    if(level.getBlockEntity(pos) instanceof PhasedBlockBlockEntity blockEntity) {
      blockEntity.countdown = duration;
      return;
    }

    var state = level.getBlockState(pos);
    var entity = level.getBlockEntity(pos);

    if(state.is(ScriptorBlockTags.DO_NOT_PHASE) || state.liquid() || state.isAir()) return;

    var newState = ScriptorBlocks.PHASED_BLOCK.get().defaultBlockState();
    var newEntity = new PhasedBlockBlockEntity(pos, newState);
    newEntity.blockState = state;
    if(entity != null)
      newEntity.data = entity.saveWithFullMetadata(level.registryAccess());
    newEntity.countdown = duration;

    level.removeBlockEntity(pos);
    level.setBlock(pos, newState, 22);
    level.setBlockEntity(newEntity);

    level.sendBlockUpdated(pos, newState, newState, 7);
  }

  @Nullable
  @Override
  public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
    var tag = super.getUpdateTag(provider);

    saveAdditional(tag, provider);

    return tag;
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.loadAdditional(tag, provider);

    data = tag.getCompound("data");
    var state = tag.get("blockState");
    BlockState.CODEC.decode(NbtOps.INSTANCE, state).result().ifPresent(
      result -> blockState = result.getFirst()
    );

    setChanged();
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.saveAdditional(tag, provider);

    if(data != null)
      tag.put("data", data);

    var state = BlockState.CODEC.encodeStart(NbtOps.INSTANCE, blockState);
    state.result().ifPresent(result -> tag.put("blockState", result));
  }

  public BlockState getPhasedBlockState() {
    return blockState;
  }

  public float getAnim() {
    if(level == null) return 0;

    if(created == -1) created = level.getGameTime();

    float f = ((float) ANIM_DURATION) / 5f;
    long anim = Math.min(ANIM_DURATION, level.getGameTime() - created);
    if(countdown == -1) return anim / (float) ANIM_DURATION;
    return Math.min(anim, countdown * f) / (float) ANIM_DURATION;
  }
}
