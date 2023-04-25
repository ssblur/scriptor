package com.ssblur.scriptor.helpers.targetable;

import com.mojang.math.Vector3f;
import com.ssblur.scriptor.blockentity.CastingLecternBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class LecternTargetable extends Targetable implements InventoryTargetable {
  public LecternTargetable(Level level, Vec3 pos) {
    super(level, pos);
  }

  public LecternTargetable(Level level, Vector3f pos) {
    super(level, pos);
  }

  public LecternTargetable(Level level, BlockPos pos) {
    super(level, pos);
  }

  @Override
  public @Nullable Container getContainer() {
    if(level.getBlockEntity(getTargetBlockPos()) instanceof CastingLecternBlockEntity)
      if(level.getBlockEntity(getTargetBlockPos().below()) instanceof Container container)
        return container;
    return null;
  }

  @Override
  public int getTargetedSlot() {
    return 0;
  }
}
