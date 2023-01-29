package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.descriptor.StrengthDescriptor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Explosion;

public class ExplosionAction extends Action {
  @Override
  public void apply(Entity caster, Targetable targetable, Descriptor[] descriptors) {
    if(caster.level.isClientSide) return;
    int strength = 2;
    for(var d: descriptors) {
      if(d instanceof StrengthDescriptor strengthDescriptor)
        strength += strengthDescriptor.strengthModifier();
    }

    ServerLevel level = (ServerLevel) caster.level;
    var pos = targetable.getTargetPos();
    level.explode(null, pos.x, pos.y + .25, pos.z, strength, Explosion.BlockInteraction.DESTROY);
  }

  @Override
  public Cost cost() { return new Cost(16, COSTTYPE.ADDITIVE); }

}
