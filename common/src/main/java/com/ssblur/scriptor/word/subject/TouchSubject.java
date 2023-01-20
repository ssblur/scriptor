package com.ssblur.scriptor.word.subject;

import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.word.Spell;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class TouchSubject extends Subject{
  public static EntityHitResult getTargetedEntity(Entity entity) {
    var start = entity.getEyePosition();
    var addition = entity.getLookAngle();
    return ProjectileUtil.getEntityHitResult(
      entity.level,
      entity,
      start,
      start.add(addition),
      entity.getBoundingBox().expandTowards(entity.getDeltaMovement()).inflate(3.0D),
      (val) -> true);
  }

  @Override
  public void cast(Entity caster, Spell spell) {
    var result = getTargetedEntity(caster);
    if(result.getEntity() instanceof LivingEntity living)
      spell.action().apply(caster, new EntityTargetable(living), spell.descriptors());

  }

  @Override
  public int cost() { return 1; }
}
