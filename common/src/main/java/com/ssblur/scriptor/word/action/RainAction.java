package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.api.word.Action;
import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.api.word.descriptor.StrengthDescriptor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;

public class RainAction extends Action {
  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    if(targetable.getLevel().isClientSide) return;
    double strength = 2;
    for(var d: descriptors) {
      if(d instanceof StrengthDescriptor strengthDescriptor)
        strength += strengthDescriptor.strengthModifier();
    }

    var serverLevel = (ServerLevel) targetable.getLevel();
    serverLevel.setWeatherParameters(0, (int) strength * 2000, true, strength > 6);

    strength -= 9;
    for(double i = strength; i >= 0; i -= 5) {
      double x = Math.random() * 12 - 6;
      double y = Math.random() * -1;
      double z = Math.random() * 12 - 6;
      x += Math.signum(x) * 2;
      z += Math.signum(z) * 2;
      LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, serverLevel);
      if(caster instanceof EntityTargetable entityTargetable && entityTargetable.getTargetEntity() instanceof ServerPlayer player)
        bolt.setCause(player);
      bolt.setPos(targetable.getTargetPos().add(x, y, z));
      serverLevel.addFreshEntity(bolt);
    }
  }
  @Override
  public Cost cost() { return new Cost(51, COSTTYPE.ADDITIVE); }
}
