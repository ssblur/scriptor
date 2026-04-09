package com.ssblur.scriptor.word.descriptor.target

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.Targetable
import net.minecraft.core.Direction
import net.minecraft.world.phys.Vec3
import java.util.function.BiConsumer

class OffsetDescriptor(val cost: Double): Descriptor(), TargetDescriptor {
  var transforms: MutableList<BiConsumer<Targetable, MutableList<Targetable>>> = ArrayList()
  override fun modifyTargets(originalTargetables: List<Targetable>, owner: Targetable): List<Targetable> {
    val output: MutableList<Targetable> = ArrayList()
    for (targetable in originalTargetables)
      for (transform in transforms) transform.accept(targetable, output)
    return output
  }

  override fun cost() = Cost(cost, COSTTYPE.MULTIPLICATIVE)
  override fun replacesSubjectCost() = false
  override fun allowsDuplicates() = true

  fun right(): OffsetDescriptor {
    transforms.add(BiConsumer { originalTargetable: Targetable, output: MutableList<Targetable> ->
      var targetable = originalTargetable
      targetable = targetable.simpleCopy()
      val direction = targetable.facing
      var pos = targetable.targetPos
      pos = if (direction.axis !== Direction.Axis.Y) pos.add(Vec3.atLowerCornerOf(direction.clockWise.normal))
      else pos.add(Vec3.atLowerCornerOf(Direction.EAST.normal))
      targetable.targetPos = pos
      output.add(targetable)
    })
    return this
  }

  fun left(): OffsetDescriptor {
    transforms.add(BiConsumer { originalTargetable: Targetable, output: MutableList<Targetable> ->
      var targetable = originalTargetable
      targetable = targetable.simpleCopy()
      val direction = targetable.facing
      var pos = targetable.targetPos
      pos =
        if (direction.axis !== Direction.Axis.Y) pos.add(Vec3.atLowerCornerOf(direction.counterClockWise.normal))
        else pos.add(Vec3.atLowerCornerOf(Direction.WEST.normal))
      targetable.targetPos = pos
      output.add(targetable)
    })
    return this
  }

  fun up(): OffsetDescriptor {
    transforms.add(BiConsumer { originalTargetable: Targetable, output: MutableList<Targetable> ->
      var targetable = originalTargetable
      targetable = targetable.simpleCopy()
      val direction = targetable.facing
      var pos = targetable.targetPos
      pos = if (direction.axis !== Direction.Axis.Y) pos.add(Vec3.atLowerCornerOf(Direction.UP.normal))
      else pos.add(Vec3.atLowerCornerOf(Direction.NORTH.normal))
      targetable.targetPos = pos
      output.add(targetable)
    })
    return this
  }

  fun down(): OffsetDescriptor {
    transforms.add(BiConsumer { originalTargetable: Targetable, output: MutableList<Targetable> ->
      var targetable = originalTargetable
      targetable = targetable.simpleCopy()
      val direction = targetable.facing
      var pos = targetable.targetPos
      pos = if (direction.axis !== Direction.Axis.Y) pos.add(Vec3.atLowerCornerOf(Direction.DOWN.normal))
      else pos.add(Vec3.atLowerCornerOf(Direction.SOUTH.normal))
      targetable.targetPos = pos
      output.add(targetable)
    })
    return this
  }

  fun forward(): OffsetDescriptor {
    transforms.add(BiConsumer { originalTargetable: Targetable, output: MutableList<Targetable> ->
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
    transforms.add(BiConsumer { originalTargetable: Targetable, output: MutableList<Targetable> ->
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
    transforms.add((BiConsumer { targetable: Targetable, output: MutableList<Targetable> -> output.add(targetable.simpleCopy()) }))
    return this
  }
}
