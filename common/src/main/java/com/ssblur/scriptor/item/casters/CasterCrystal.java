package com.ssblur.scriptor.item.casters;

import com.ssblur.scriptor.helpers.targetable.Targetable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class CasterCrystal extends Item {
  public CasterCrystal(Properties properties) {
    super(properties);
  }

  public abstract List<Targetable> getTargetables(ItemStack itemStack, Level level);
}
