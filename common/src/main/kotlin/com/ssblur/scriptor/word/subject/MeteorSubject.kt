package com.ssblur.scriptor.word.subject

import com.ssblur.scriptor.api.word.Subject
import com.ssblur.scriptor.color.CustomColors.getColor
import com.ssblur.scriptor.entity.ScriptorEntities
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.LecternTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.network.server.TraceNetwork
import com.ssblur.scriptor.word.Spell
import com.ssblur.scriptor.word.descriptor.SpeedDescriptor
import com.ssblur.scriptor.word.descriptor.duration.DurationDescriptor
import com.ssblur.scriptor.word.descriptor.target.CollideWithWaterDescriptor
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import java.util.concurrent.CompletableFuture

class MeteorSubject: Subject() {
  override fun getTargets(caster: Targetable, spell: Spell): CompletableFuture<List<Targetable>> {
    val result = CompletableFuture<List<Targetable>>()
    if (caster is EntityTargetable && caster.targetEntity is Player) {
      val player = caster.targetEntity as Player
      TraceNetwork.requestExtendedTraceData(
        player,
        spell.deduplicatedDescriptorsForSubjects().contains(CollideWithWaterDescriptor)
      ) { target: Targetable ->
        spawnMeteorOver(target.level, target.targetPos, spell, result)
      }
    } else if (caster is LecternTargetable) {
      val pos = caster.targetBlockPos.relative(caster.facing)
      val normal = caster.facing.normal
      var x = normal.x * 19
      var z = normal.z * 19
      val entities = caster.level.getEntitiesOfClass(
        LivingEntity::class.java,
        AABB.ofSize(
          Vec3(
            pos.x.toDouble(),
            pos.y.toDouble(),
            pos.z.toDouble()
          ),
          (1 + x).toDouble(),
          1.0,
          (1 + z).toDouble()
        )
      )

      if (entities.isEmpty()) {
        var i = 0
        x = caster.facing.stepX
        z = caster.facing.stepZ
        while (i < 19) {
          if (!caster.level.getBlockState(pos.offset(x * i, 0, z * i)).isAir) {
            spawnMeteorOver(caster.level, pos.offset(x * i, 0, z * i).center, spell, result)
            return result
          }
          i++
        }
      } else
        spawnMeteorOver(caster.level, entities.first().position(), spell, result)
    } else {
      result.complete(listOf(caster.simpleCopy()))
    }
    return result
  }

  fun spawnMeteorOver(level: Level, position: Vec3, spell: Spell, action: CompletableFuture<List<Targetable>>) {
    val future = CompletableFuture<List<Targetable>>()

    val color = getColor(spell.deduplicatedDescriptorsForSubjects())
    var duration = 12.0
    var speed = 1.5
    for (d in spell.deduplicatedDescriptorsForSubjects()) {
      if (d is DurationDescriptor) duration += d.durationModifier()
      if (d is SpeedDescriptor) speed *= d.speedModifier()
    }
    speed *= 0.8

    val projectile = checkNotNull(ScriptorEntities.PROJECTILE_TYPE.get().create(level))
    projectile.setPos(position.add(0.0, 16.0, 0.0))
    projectile.deltaMovement = Vec3(0.0, -1.0, 0.0).scale(speed)
    projectile.setDuration(Math.round(10 * duration).toInt())
    projectile.color = color
    projectile.completable = future
    level.addFreshEntity(projectile)

    future.thenAccept {
      action.complete(it)
    }
  }

  override fun cost() = Cost(8.0, COSTTYPE.ADDITIVE)
}
