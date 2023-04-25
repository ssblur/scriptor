package com.ssblur.scriptor.helpers.targetable;

import net.minecraft.world.Container;
import org.jetbrains.annotations.Nullable;

public interface InventoryTargetable {
  @Nullable
  Container getContainer();

  int getTargetedSlot();
}
