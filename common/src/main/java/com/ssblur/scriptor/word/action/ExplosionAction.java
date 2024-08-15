package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.api.word.Action;
import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.power.StrengthDescriptor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ExplosionAction extends Action {
  static class ExplosionActionDamageCalculator extends ExplosionDamageCalculator {
    Targetable caster;
    ExplosionActionDamageCalculator(Targetable caster) {
      this.caster = caster;
    }

    @Override
    public float getEntityDamageAmount(@NotNull Explosion explosion, @NotNull Entity entity) {
      if(caster instanceof EntityTargetable entityTargetable && entityTargetable.getTargetEntity() == entity)
        return super.getEntityDamageAmount(explosion, entity) * 0.15f;
      return super.getEntityDamageAmount(explosion, entity) * 0.3f;
    }
  }

  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    if(targetable.getLevel().isClientSide) return;
    double strength = 2;
    for(var d: descriptors) {
      if(d instanceof StrengthDescriptor strengthDescriptor)
        strength += strengthDescriptor.strengthModifier();
    }

    ServerLevel level = (ServerLevel) targetable.getLevel();
    var pos = targetable.getTargetPos();

    float power = (float) (Math.log(strength) / Math.log(1.45));

    level.explode(
      null,
      null,
      new ExplosionActionDamageCalculator(caster),
      pos.x,
      pos.y + .25,
      pos.z,
      power,
      false,
      Level.ExplosionInteraction.TNT
    );

  }

  @Override
  public Cost cost() { return new Cost(16, COSTTYPE.ADDITIVE); }

}
