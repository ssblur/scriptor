package com.ssblur.scriptor.word.descriptor.target

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.Targetable
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

class NetherDescriptor: Descriptor(), TargetDescriptor {
  override fun modifyTargets(originalTargetables: List<Targetable>, owner: Targetable): List<Targetable> {
    val list: MutableList<Targetable> = ArrayList()

    for (targetable in originalTargetables) {
      val level = targetable.level as? ServerLevel ?: continue
      val nether = level.server.getLevel(Level.NETHER)
      if (nether == null || !level.server.isLevelEnabled(nether)) return originalTargetables
      if (level.dimension() !== Level.OVERWORLD && level.dimension() !== Level.NETHER) {
        list.add(targetable)
        continue
      }

      val targetLevel = level.server.getLevel(if (level.dimension() === Level.NETHER) Level.OVERWORLD else Level.NETHER)
      val scale = if (level.dimension() === Level.NETHER) 8.0 else 0.125
      val pos = targetable.targetPos
      val x = pos.x * scale
      val z = pos.z * scale

      list.add(Targetable(targetLevel!!, Vec3(x, pos.y, z)))
    }

    return list
  }

  override fun replacesSubjectCost() = false
  override fun cost() = Cost.add(20.0)
}
