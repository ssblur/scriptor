package com.ssblur.scriptor.entity;

import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ScriptorProjectile extends Entity {
  private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(ScriptorProjectile.class, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(ScriptorProjectile.class, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Integer> OWNER = SynchedEntityData.defineId(ScriptorProjectile.class, EntityDataSerializers.INT);

  CompletableFuture<List<Targetable>> completable;
  Vec3 origin;

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

  public void setOrigin(@Nullable BlockPos origin) {
    if(origin == null)
      this.origin = null;
    else
      this.origin = new Vec3(origin.getX() + 0.5, origin.getY() + 0.5, origin.getZ() + 0.5);
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag compoundTag) {
    CompoundTag tag = compoundTag.getCompound("scriptor:projectile_data");
    entityData.set(COLOR, tag.getInt("com/ssblur/scriptor/color"));
    entityData.set(DURATION, tag.getInt("duration"));
    entityData.set(OWNER, tag.getInt("owner"));
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag compoundTag) {
    CompoundTag tag = compoundTag.getCompound("scriptor:projectile_data");
    tag.putInt("com/ssblur/scriptor/color", entityData.get(COLOR));
    tag.putInt("duration", entityData.get(DURATION));
    tag.putInt("owner", entityData.get(OWNER));
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.Builder builder) {
    builder.define(COLOR, 0xa020f0);
    builder.define(DURATION, 120);
    builder.define(OWNER, 0);
  }

  @Override
  public void tick() {
    var level = level();
    if (level.isClientSide) return;

//    int c = CustomColors.getColor(getColor(), level.getGameTime());
//    int r, g, b;
//    r = (c & 0xff0000) >> 16;
//    g = (c & 0x00ff00) >> 8;
//    b = c & 0x0000ff;
//
//    var particle = MagicParticleData.magic(r, g, b);
//    level.addParticle(
//      particle,
//      getX(),
//      getY(),
//      getZ(),
//      0,
//      0,
//      0
//    );

    int duration = entityData.get(DURATION);
    var owner = level.getEntity(entityData.get(OWNER));
    if (
      tickCount > duration
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
      level(),
      this,
      position(),
      dest,
      getBoundingBox().expandTowards(getDeltaMovement()).inflate(1),
      e -> true
    );

    if (entityHitResult != null && entityHitResult.getEntity() instanceof LivingEntity entity && entity != owner)
      completable.complete(List.of(new EntityTargetable(entity)));
    else if (
      blockHitResult.getType() != HitResult.Type.MISS
        && !(origin != null && blockHitResult.getType() == HitResult.Type.BLOCK && origin.distanceToSqr(dest) < 0.55)
    )
      completable.complete(List.of(
        new Targetable(this.level(), blockHitResult.getBlockPos().offset(blockHitResult.getDirection().getNormal()))
          .setFacing(blockHitResult.getDirection())
      ));

    setDeltaMovement(getDeltaMovement().x, getDeltaMovement().y, getDeltaMovement().z);
    setPos(position().add(getDeltaMovement()));
  }
}
