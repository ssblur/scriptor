package com.ssblur.scriptor.word.descriptor.target

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.MathHelper
import com.ssblur.scriptor.helpers.targetable.Targetable
import net.minecraft.core.Direction
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3
import org.apache.commons.lang3.function.TriConsumer

class OffsetDescriptor(val cost: Double): Descriptor(), TargetDescriptor {
  var transforms: MutableList<TriConsumer<Targetable, Targetable, MutableList<Targetable>>> = ArrayList()
  override fun modifyTargets(originalTargetables: List<Targetable>, owner: Targetable): List<Targetable> {
    val output: MutableList<Targetable> = ArrayList()
    for (targetable in originalTargetables)
      for (transform in transforms) transform.accept(targetable, owner, output)
    return output
  }

  override fun cost() = Cost(1.25, COSTTYPE.MULTIPLICATIVE)
  override fun replacesSubjectCost() = false
  override fun allowsDuplicates() = true

  fun right(): OffsetDescriptor {
    transforms.add(TriConsumer { originalTargetable: Targetable, owner: Targetable, output: MutableList<Targetable> ->
      var targetable = originalTargetable
      targetable = targetable.simpleCopy()
      val direction = targetable.facing
      var pos = targetable.targetPos
      pos = if (direction.axis !== Direction.Axis.Y) pos.add(Vec3.atLowerCornerOf(direction.counterClockWise.normal))
      else {
        MathHelper.player_view_transform_point(targetable, owner, Vec2(0f, 1f))
      }
      targetable.targetPos = pos
      output.add(targetable)
    })
    return this
  }

  fun left(): OffsetDescriptor {
    transforms.add(TriConsumer { originalTargetable: Targetable, owner: Targetable, output: MutableList<Targetable> ->
      var targetable = originalTargetable
      targetable = targetable.simpleCopy()
      val direction = targetable.facing
      var pos = targetable.targetPos
      pos =
        if (direction.axis !== Direction.Axis.Y) pos.add(Vec3.atLowerCornerOf(direction.clockWise.normal))
        else {
          MathHelper.player_view_transform_point(targetable, owner, Vec2(0f, -1f))
        }
      targetable.targetPos = pos
      output.add(targetable)
    })
    return this
  }

  fun up(): OffsetDescriptor {
    transforms.add(TriConsumer { originalTargetable: Targetable, owner: Targetable, output: MutableList<Targetable> ->
      var targetable = originalTargetable
      targetable = targetable.simpleCopy()
      val direction = targetable.facing
      var pos = targetable.targetPos
      pos = if (direction.axis !== Direction.Axis.Y) pos.add(Vec3.atLowerCornerOf(Direction.UP.normal))
      else {
        MathHelper.player_view_transform_point(targetable, owner, Vec2(1f, 0f))
      }
      targetable.targetPos = pos
      output.add(targetable)
    })
    return this
  }

  fun down(): OffsetDescriptor {
    transforms.add(TriConsumer { originalTargetable: Targetable, owner: Targetable, output: MutableList<Targetable> ->
      var targetable = originalTargetable
      targetable = targetable.simpleCopy()
      val direction = targetable.facing
      var pos = targetable.targetPos
      pos = if (direction.axis !== Direction.Axis.Y) pos.add(Vec3.atLowerCornerOf(Direction.DOWN.normal))
      else {
        MathHelper.player_view_transform_point(targetable, owner, Vec2(-1f, 0f))
      }
      targetable.targetPos = pos
      output.add(targetable)
    })
    return this
  }

  fun forward(): OffsetDescriptor {
    transforms.add(TriConsumer { originalTargetable: Targetable, owner: Targetable, output: MutableList<Targetable> ->
      var targetable = originalTargetable
      targetable = targetable.simpleCopy()
      val direction = targetable.facing
      var pos = targetable.targetPos
      pos = pos.add(Vec3.atLowerCornerOf(direction.normal))
      targetable.targetPos = pos
      output.add(targetable)
    })
    return this
  }

  fun backwards(): OffsetDescriptor {
    transforms.add(TriConsumer { originalTargetable: Targetable, owner: Targetable, output: MutableList<Targetable> ->
      var targetable = originalTargetable
      targetable = targetable.simpleCopy()
      val direction = targetable.facing
      var pos = targetable.targetPos
      pos = pos.add(Vec3.atLowerCornerOf(direction.opposite.normal))
      targetable.targetPos = pos
      output.add(targetable)
    })
    return this
  }

  fun duplicate(): OffsetDescriptor {
    transforms.add((TriConsumer { targetable: Targetable, owner: Targetable, output: MutableList<Targetable> -> output.add(targetable.simpleCopy()) }))
    return this
  }
}
