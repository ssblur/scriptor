package com.ssblur.scriptor.events;

import com.ssblur.scriptor.block.ScriptorBlocks;
import com.ssblur.scriptor.blockentity.PhasedBlockBlockEntity;
import com.ssblur.scriptor.effect.ScriptorEffects;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.core.Vec3i;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PlayerTickEvent implements TickEvent.Player {
  @Override
  public void tick(net.minecraft.world.entity.player.Player entity) {
    var level = entity.level();

    if(entity.hasEffect(ScriptorEffects.PHASING.get()))
      phase(entity, 0);
    if(entity.hasEffect(ScriptorEffects.WILD_PHASING.get()))
      phase(entity, -2);

    for(var item: entity.getInventory().items)
      processItem(item, entity);

    for(var slot: EquipmentSlot.values())
      processItem(entity.getItemBySlot(slot), entity);
  }

  public void phase(net.minecraft.world.entity.player.Player entity, int bottom) {
    for (int x = 1; x >= -1; x--)
      for (int y = (int) Math.ceil(entity.getEyeHeight()); y >= bottom; y--)
        for (int z = 1; z >= -1; z--)
          PhasedBlockBlockEntity.phase(entity.level(), entity.blockPosition().offset(new Vec3i(x, y, z)));
  }

  public void processItem(ItemStack item, net.minecraft.world.entity.player.Player entity) {
    var level = entity.level();

    if(item.getCount() > 0) {
      var scriptor = item.getTagElement("scriptor");

      // Expire bound items
      if (scriptor != null) {
        if (scriptor.contains("expire"))
          if (scriptor.getLong("expire") <= level.getGameTime()) {
            item.setCount(0);
            level.playSound(null, entity.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS);
          }

      }
    }
  }
}
