package com.ssblur.scriptor.entity

import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import java.util.concurrent.CompletableFuture

class ScriptorProjectile(entityType: EntityType<ScriptorProjectile?>, level: Level): Entity(entityType, level) {
  var completable: CompletableFuture<List<Targetable>>? = null
  var origin: Vec3? = null
  var collidesWithWater: Boolean = false

  var color: Int
    get() = entityData.get(COLOR)
    set(color) {
      entityData.set(COLOR, color)
    }

  fun setDuration(duration: Int) {
    entityData.set(DURATION, duration)
  }

  fun setOwner(owner: Int) {
    entityData.set(OWNER, owner)
  }

  fun setOwner(owner: Entity) {
    setOwner(owner.id)
  }

  fun setOrigin(origin: BlockPos?) {
    if (origin == null) this.origin = null
    else this.origin = Vec3(origin.x + 0.5, origin.y + 0.5, origin.z + 0.5)
  }

  override fun readAdditionalSaveData(compoundTag: CompoundTag) {
    val tag = compoundTag.getCompound("scriptor:projectile_data")
    entityData.set(COLOR, tag.getInt("scriptor:color"))
    entityData.set(DURATION, tag.getInt("duration"))
    entityData.set(OWNER, tag.getInt("owner"))
  }

  override fun addAdditionalSaveData(compoundTag: CompoundTag) {
    val tag = compoundTag.getCompound("scriptor:projectile_data")
    tag.putInt("scriptor:color", entityData.get(COLOR))
    tag.putInt("duration", entityData.get(DURATION))
    tag.putInt("owner", entityData.get(OWNER))
  }

  override fun defineSynchedData(builder: SynchedEntityData.Builder) {
    builder.define(COLOR, 0xa020f0)
    builder.define(DURATION, 120)
    builder.define(OWNER, 0)
  }

  override fun tick() {
    val level = level()

    setDeltaMovement(deltaMovement.x, deltaMovement.y, deltaMovement.z)
    setPos(position().add(deltaMovement))
    if (level.isClientSide) return

    if (this.origin != null) if (position().distanceTo(this.origin!!) <= 1) return

    val duration = entityData.get(DURATION)
    if (tickCount > duration || completable == null || completable!!.isDone) {
      remove(RemovalReason.KILLED)
      return
    }

    var dest = position().add(deltaMovement)
    val blockHitResult =
      level.clip(
        ClipContext(
          position(),
          dest,
          ClipContext.Block.COLLIDER,
          if(collidesWithWater) ClipContext.Fluid.ANY else ClipContext.Fluid.NONE,
          this
        )
      )
    if (blockHitResult.type != HitResult.Type.MISS) dest = blockHitResult.location
    val entityHitResult = ProjectileUtil.getEntityHitResult(
      level(),
      this,
      position(),
      dest,
      boundingBox.expandTowards(deltaMovement).inflate(1.0)
    ) { _: Entity? -> true }


    if (entityHitResult != null) {
      val entity = entityHitResult.entity
      if (entity is LivingEntity)
        completable!!.complete(listOf(EntityTargetable(entity)))
    } else if (blockHitResult.type != HitResult.Type.MISS
      && !(origin != null && blockHitResult.type == HitResult.Type.BLOCK && origin!!.distanceToSqr(dest) < 0.55)
    )
      completable!!.complete(
        listOf(
          Targetable(
            level(),
            blockHitResult.blockPos.offset(blockHitResult.direction.normal)
          ).setFacing(blockHitResult.direction)
        )
      )
  }

  companion object {
    private val DURATION: EntityDataAccessor<Int> =
      SynchedEntityData.defineId(ScriptorProjectile::class.java, EntityDataSerializers.INT)
    private val COLOR: EntityDataAccessor<Int> =
      SynchedEntityData.defineId(ScriptorProjectile::class.java, EntityDataSerializers.INT)
    private val OWNER: EntityDataAccessor<Int> =
      SynchedEntityData.defineId(ScriptorProjectile::class.java, EntityDataSerializers.INT)
  }
}
