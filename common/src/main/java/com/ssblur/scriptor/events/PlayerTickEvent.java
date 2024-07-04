package com.ssblur.scriptor.events;

import com.ssblur.scriptor.block.ScriptorBlocks;
import com.ssblur.scriptor.blockentity.PhasedBlockBlockEntity;
import com.ssblur.scriptor.effect.ScriptorEffects;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;

public class PlayerTickEvent implements TickEvent.Player {
  @Override
  public void tick(net.minecraft.world.entity.player.Player entity) {
    if(entity.hasEffect(ScriptorEffects.PHASING.get()))
      phase(entity, 0);
    if(entity.hasEffect(ScriptorEffects.WILD_PHASING.get()))
      phase(entity, -2);
  }

  public void phase(net.minecraft.world.entity.player.Player entity, int bottom) {
    for (int x = 1; x >= -1; x--)
      for (int y = (int) Math.ceil(entity.getEyeHeight()); y >= bottom; y--)
        for (int z = 1; z >= -1; z--)
          PhasedBlockBlockEntity.phase(entity.level(), entity.blockPosition().offset(new Vec3i(x, y, z)));
  }
}
