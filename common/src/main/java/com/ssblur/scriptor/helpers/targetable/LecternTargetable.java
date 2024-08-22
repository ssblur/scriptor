package com.ssblur.scriptor.helpers.targetable;

import com.ssblur.scriptor.blockentity.CastingLecternBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class LecternTargetable extends Targetable implements InventoryTargetable {
  int slot;
  public LecternTargetable(Level level, Vec3 pos) {
    super(level, pos);
    slot = 0;
  }

  public LecternTargetable(Level level, Vector3f pos) {
    super(level, new Vec3(pos.x, pos.y, pos.z));
    slot = 0;
  }

  public LecternTargetable(Level level, BlockPos pos) {
    super(level, pos);
    slot = 0;
  }

  @Override
  public @Nullable Container getContainer() {
    if(level.getBlockEntity(getTargetBlockPos()) instanceof CastingLecternBlockEntity lectern) {
      if (level.getBlockEntity(getTargetBlockPos().below()) instanceof Container container)
        return container;
      else if(level.getBlockEntity(getTargetBlockPos().relative(this.getFacing().getOpposite())) instanceof Container container)
        return container;
    }
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

  @Override
  public BlockPos getOrigin() {
    return getTargetBlockPos();
  }
}
