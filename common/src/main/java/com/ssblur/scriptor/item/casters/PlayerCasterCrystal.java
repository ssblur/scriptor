package com.ssblur.scriptor.item.casters;

import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;

public class PlayerCasterCrystal extends CasterCrystal {
  public PlayerCasterCrystal(Properties properties) {
    super(properties);
  }

  @Override
  public List<Targetable> getTargetables(ItemStack itemStack, Level level) {
    if(itemStack.getTag() != null && itemStack.getTag().contains("playerUUID")){
      var player = level.getPlayerByUUID(UUID.fromString(itemStack.getTag().getString("playerUUID")));
      if(player != null)
        return List.of(new EntityTargetable(player));
    }
    return List.of();
  }
}
