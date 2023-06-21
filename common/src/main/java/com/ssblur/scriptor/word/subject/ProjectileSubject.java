package com.ssblur.scriptor.word.subject;

import com.ssblur.scriptor.entity.ScriptorEntities;
import com.ssblur.scriptor.helpers.CustomColors;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.Spell;
import com.ssblur.scriptor.word.descriptor.visual.ColorDescriptor;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.descriptor.duration.DurationDescriptor;
import com.ssblur.scriptor.word.descriptor.SpeedDescriptor;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ProjectileSubject extends Subject {
  @Override
  public Cost cost() {
    return new Cost(3, COSTTYPE.ADDITIVE);
  }

  @Override
  public CompletableFuture<List<Targetable>> getTargets(Targetable caster, Spell spell) {
    CompletableFuture<List<Targetable>> future = new CompletableFuture<>();

    int color = CustomColors.getColor(spell.deduplicatedDescriptorsForSubjects());
    double duration = 12;
    double speed = 1;
    for(Descriptor d: spell.deduplicatedDescriptorsForSubjects()) {
      if(d instanceof DurationDescriptor descriptor)
        duration += descriptor.durationModifier();
      if(d instanceof SpeedDescriptor descriptor)
        speed *= descriptor.speedModifier();
    }
    speed *= 0.8d;

    var projectile = ScriptorEntities.PROJECTILE_TYPE.get().create(caster.getLevel());
    assert projectile != null;
    if(caster instanceof EntityTargetable entityTargetable) {
      var entity = entityTargetable.getTargetEntity();
      projectile.setPos(entity.getEyePosition());
      projectile.setDeltaMovement(entity.getLookAngle().normalize().scale(speed));
      projectile.setOwner(entity);
    } else {
      projectile.setPos(caster.getTargetPos());
      var normal = caster.getFacing().getNormal();
      projectile.setDeltaMovement(new Vec3(normal.getX(), normal.getY(), normal.getZ()).scale(speed));
    }
    projectile.setOrigin(caster.getOrigin());
    projectile.setDuration((int) Math.round(10 * duration));
    projectile.setColor(color);
    projectile.setCompletable(future);
    caster.getLevel().addFreshEntity(projectile);

    return future;
  }

}
