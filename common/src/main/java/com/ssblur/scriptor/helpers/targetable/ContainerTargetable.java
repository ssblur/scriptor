package com.ssblur.scriptor.helpers.targetable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ContainerTargetable extends Targetable implements InventoryTargetable {
  int slot;
  public ContainerTargetable(Level level, BlockPos pos, int slot) {
    super(level, pos);
    this.slot = slot;
  }

  @Override
  public @Nullable Container getContainer() {
    if(level.getBlockEntity(getTargetBlockPos()) instanceof Container container)
      return container;
    return null;
  }

  @Override
  public int getTargetedSlot() {
    return slot;
  }

  @Override
  public void setTargetedSlot(int slot) {
    this.slot = slot;
  }
}
