package com.ssblur.scriptor.word.subject;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.entity.ScriptorProjectile;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.Spell;
import com.ssblur.scriptor.word.descriptor.ColorDescriptor;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import net.minecraft.world.entity.Entity;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ProjectileSubject extends Subject {
  @Override
  public Cost cost() {
    return new Cost(3, COSTTYPE.ADDITIVE);
  }

  @Override
  public CompletableFuture<List<Targetable>> getTargets(Entity caster, Spell spell) {
    CompletableFuture<List<Targetable>> future = new CompletableFuture<>();

    int color = 0xa020f0;
    for(Descriptor d: spell.descriptors())
      if(d instanceof ColorDescriptor descriptor)
        color = descriptor.getColor();

    var projectile = ScriptorMod.PROJECTILE_TYPE.get().create(caster.level);
    assert projectile != null;
    projectile.setPos(caster.getEyePosition());
    projectile.setDeltaMovement(caster.getLookAngle());
    projectile.setOwner(caster);
    projectile.setDuration(240);
    projectile.setColor(color);
    projectile.setCompletable(future);
    caster.level.addFreshEntity(projectile);

    return future;
  }

}
