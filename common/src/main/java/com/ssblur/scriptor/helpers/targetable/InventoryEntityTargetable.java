package com.ssblur.scriptor.helpers.targetable;

import com.ssblur.scriptor.gamerules.ScriptorGameRules;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class InventoryEntityTargetable extends EntityTargetable implements InventoryTargetable {
  int slot;
  public InventoryEntityTargetable(Entity entity, int slot) {
    super(entity);
    this.slot = slot;
  }

  @Override
  public @Nullable Container getContainer() {
    if(targetEntity instanceof Container container)
      return container;
    if(targetEntity instanceof Player player && player.level().getGameRules().getBoolean(ScriptorGameRules.CAN_TARGET_PLAYER_INVENTORIES))
      return player.getInventory();
    if(targetEntity instanceof AbstractHorse horse)
      return horse.inventory;
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
