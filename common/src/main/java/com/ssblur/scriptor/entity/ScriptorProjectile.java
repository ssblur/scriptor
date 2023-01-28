package com.ssblur.scriptor.entity;

import com.ssblur.scriptor.helpers.DictionarySavedData;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.Spell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ScriptorProjectile extends Entity {
  private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(ScriptorProjectile.class, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(ScriptorProjectile.class, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Integer> OWNER = SynchedEntityData.defineId(ScriptorProjectile.class, EntityDataSerializers.INT);

  CompletableFuture<List<Targetable>> completable;

  public ScriptorProjectile(EntityType<ScriptorProjectile> entityType, Level level) {
    super(entityType, level);
  }

  public void setCompletable(CompletableFuture<List<Targetable>> completable) {
    this.completable = completable;
  }

  public void setColor(int color) {
    entityData.set(COLOR, color);
  }
  public int getColor() {
    return entityData.get(COLOR);
  }

  public void setDuration(int duration) {
    entityData.set(DURATION, duration);
  }

  public void setOwner(int owner) {
    entityData.set(OWNER, owner);
  }

  public void setOwner(Entity owner) {
    entityData.set(OWNER, owner.getId());
  }

  @Override
  protected void defineSynchedData() {
    entityData.define(COLOR, 0xa020f0);
    entityData.define(DURATION, 120);
    entityData.define(OWNER, 0);
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag compoundTag) {
    CompoundTag tag = compoundTag.getCompound("scriptor:projectile_data");
    entityData.set(COLOR, tag.getInt("color"));
    entityData.set(DURATION, tag.getInt("duration"));
    entityData.set(OWNER, tag.getInt("owner"));
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag compoundTag) {
    CompoundTag tag = compoundTag.getCompound("scriptor:projectile_data");
    tag.putInt("color", entityData.get(COLOR));
    tag.putInt("duration", entityData.get(DURATION));
    tag.putInt("owner", entityData.get(OWNER));
  }

  @Override
  public Packet<?> getAddEntityPacket() {
    return new ClientboundAddEntityPacket(this, entityData.get(OWNER));
  }

  @Override
  public void tick() {
    if(level.isClientSide) return;

    int duration = entityData.get(DURATION);
    var owner = level.getEntity(entityData.get(OWNER));
    if (
      tickCount > duration
        || !(owner instanceof LivingEntity)
        || completable == null
        || completable.isDone()
    ) {
      remove(RemovalReason.KILLED);
      return;
    }

    var dest = position().add(getDeltaMovement());
    var blockHitResult = level.clip(new ClipContext(position(), dest, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
    if (blockHitResult.getType() != HitResult.Type.MISS)
      dest = blockHitResult.getLocation();
    var entityHitResult = ProjectileUtil.getEntityHitResult(
      level,
      this,
      position(),
      dest,
      getBoundingBox().expandTowards(getDeltaMovement()).inflate(1),
      e -> true
    );

    if (entityHitResult != null && entityHitResult.getEntity() instanceof LivingEntity entity && entity != owner)
      completable.complete(List.of(new EntityTargetable(entity)));
    else if(blockHitResult.getType() != HitResult.Type.MISS)
      completable.complete(List.of(new Targetable(blockHitResult.getBlockPos().offset(blockHitResult.getDirection().getNormal()))));

    setDeltaMovement(getDeltaMovement().x, getDeltaMovement().y, getDeltaMovement().z);
    setPos(position().add(getDeltaMovement()));
  }
}
