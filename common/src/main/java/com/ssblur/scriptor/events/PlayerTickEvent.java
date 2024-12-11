package com.ssblur.scriptor.events;

import com.ssblur.scriptor.data.components.ScriptorDataComponents;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class PlayerTickEvent implements TickEvent.Player {
  @Override
  public void tick(net.minecraft.world.entity.player.Player entity) {
    for(var item: entity.getInventory().items)
      processItem(item, entity);

    for(var slot: EquipmentSlot.values())
      processItem(entity.getItemBySlot(slot), entity);
  }

  public void processItem(ItemStack item, net.minecraft.world.entity.player.Player entity) {
    var level = entity.level();

    if(item.getCount() > 0) {
      if (item.has(ScriptorDataComponents.INSTANCE.getEXPIRES()) && item.get(ScriptorDataComponents.INSTANCE.getEXPIRES()) <= level.getGameTime()) {
        item.setCount(0);
        level.playSound(null, entity.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS);
      }
    }
  }
}
