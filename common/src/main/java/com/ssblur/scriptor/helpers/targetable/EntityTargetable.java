package com.ssblur.scriptor.helpers.targetable;

import net.minecraft.world.entity.Entity;

public class EntityTargetable extends Targetable {
  Entity targetEntity;

  public EntityTargetable(Entity entity) {
    super(entity.position());
    targetEntity = entity;
  }

  public Entity getTargetEntity() {
    return targetEntity;
  }
}
