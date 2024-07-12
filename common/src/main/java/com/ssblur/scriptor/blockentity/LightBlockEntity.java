package com.ssblur.scriptor.blockentity;

import com.ssblur.scriptor.color.interfaces.Colorable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class LightBlockEntity extends BlockEntity implements Colorable {
  int color;
  public LightBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(ScriptorBlockEntities.LIGHT.get(), blockPos, blockState);
  }

  public int getColor() {
    return color;
  }

  public void setColor(int color) {
    this.color = color;
    setChanged();
    if(level != null)
      level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
  }

  @Nullable
  @Override
  public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
    var tag = super.getUpdateTag(provider);
    tag.putInt("com/ssblur/scriptor/color", color);
    return tag;
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.loadAdditional(tag, provider);
    color = tag.getInt("com/ssblur/scriptor/color");
    setChanged();
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.saveAdditional(tag, provider);

    tag.putInt("com/ssblur/scriptor/color", color);
  }
}
