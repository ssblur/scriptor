package com.ssblur.scriptor.word.action

import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.extension.Vec3Extension.blockPos
import com.ssblur.scriptor.helpers.DescriptorHelper.strength
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import net.minecraft.core.Direction
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.EvokerFangs
import net.minecraft.world.level.Level
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.Vec3

class EvokerFangsAction: Action() {
  override fun apply(
    caster: Targetable,
    targetable: Targetable,
    descriptors: Array<Descriptor>,
    spellData: MutableList<String>
  ) {
    val normal = targetable.targetPos.subtract(caster.targetPos).normalize().scale(-1.0)
    val origin = targetable.targetPos
    val level = targetable.level
    val fangs = 4 + descriptors.strength().toInt()
    val entity = if(caster is EntityTargetable) caster.targetEntity as? LivingEntity else null
    for(i in -1..(fangs-2)) {
      var pos = origin.add(normal.scale(i.toDouble()))
      for(y in 0..-4)
        if(!level.getBlockState(pos.blockPos).isFaceSturdy(level, pos.blockPos, Direction.UP))
          pos = pos.add(0.0, -1.0, 0.0)
        else break
      if(caster.targetPos.distanceTo(pos) < 0.5) break
      summonFangs(level, pos, entity, fangs - i + 2)
    }
  }

  override fun cost(): Cost = Cost.add(1.6)

  fun summonFangs(level: Level, pos: Vec3, owner: LivingEntity?, delay: Int, angle: Float = 0.0f) {
    level.addFreshEntity(EvokerFangs(level, pos.x, pos.y, pos.z, angle, delay, owner))
    level.gameEvent(GameEvent.ENTITY_PLACE, pos, GameEvent.Context.of(owner))
  }
}