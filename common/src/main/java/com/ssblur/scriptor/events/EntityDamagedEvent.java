package com.ssblur.scriptor.events;

import com.google.common.base.MoreObjects;
import com.ssblur.scriptor.data.components.ScriptorDataComponents;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class EntityDamagedEvent implements EntityEvent.LivingHurt {
  @Override
  public EventResult hurt(LivingEntity entity, DamageSource source, float amount) {
    var weapon = source.getWeaponItem();
    if(weapon != null) {
      int data = MoreObjects.firstNonNull(weapon.get(ScriptorDataComponents.INSTANCE.getCHARGES()), 0);
      if (data > 0) {
        entity.setHealth(entity.getHealth() - 3);
        weapon.set(ScriptorDataComponents.INSTANCE.getCHARGES(), data - 1);
      }
    }
    return EventResult.pass();
  }
}
