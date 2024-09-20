package com.ssblur.scriptor.word.descriptor.target;

import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.api.word.descriptor.TargetDescriptor;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class OffsetDescriptor extends Descriptor implements TargetDescriptor {
  List<BiConsumer<Targetable, List<Targetable>>> transforms = new ArrayList<>();
  @Override
  public Cost cost() {
    return new Cost(1.25d, COSTTYPE.MULTIPLICATIVE);
  }

  @Override
  public List<Targetable> modifyTargets(List<Targetable> targetables, Targetable owner) {
    List<Targetable> output = new ArrayList<>();
    for(var targetable: targetables) {
      for (var transform: transforms)
        transform.accept(targetable, output);
    }

    return output;
  }

  @Override
  public boolean replacesSubjectCost() {
    return false;
  }

  @Override
  public boolean allowsDuplicates() { return true; }

  public OffsetDescriptor right() {
    transforms.add((targetable, output) -> {
      targetable = targetable.simpleCopy();
      var direction = targetable.getFacing();
      var pos = targetable.getTargetPos();
      if(direction.getAxis() != Direction.Axis.Y)
        pos = pos.add(Vec3.atLowerCornerOf(direction.getClockWise().getNormal()));
      else
        pos = pos.add(Vec3.atLowerCornerOf(Direction.EAST.getNormal()));
      targetable.setTargetPos(pos);
      output.add(targetable);
    });
    return this;
  }

  public OffsetDescriptor left() {
    transforms.add((targetable, output) -> {
      targetable = targetable.simpleCopy();
      var direction = targetable.getFacing();
      var pos = targetable.getTargetPos();
      if(direction.getAxis() != Direction.Axis.Y)
        pos = pos.add(Vec3.atLowerCornerOf(direction.getCounterClockWise().getNormal()));
      else
        pos = pos.add(Vec3.atLowerCornerOf(Direction.WEST.getNormal()));
      targetable.setTargetPos(pos);
      output.add(targetable);
    });
    return this;
  }

  public OffsetDescriptor up() {
    transforms.add((targetable, output) -> {
      targetable = targetable.simpleCopy();
      var direction = targetable.getFacing();
      var pos = targetable.getTargetPos();
      if(direction.getAxis() != Direction.Axis.Y)
        pos = pos.add(Vec3.atLowerCornerOf(Direction.UP.getNormal()));
      else
        pos = pos.add(Vec3.atLowerCornerOf(Direction.NORTH.getNormal()));
      targetable.setTargetPos(pos);
      output.add(targetable);
    });
    return this;
  }

  public OffsetDescriptor down() {
    transforms.add((targetable, output) -> {
      targetable = targetable.simpleCopy();
      var direction = targetable.getFacing();
      var pos = targetable.getTargetPos();
      if(direction.getAxis() != Direction.Axis.Y)
        pos = pos.add(Vec3.atLowerCornerOf(Direction.DOWN.getNormal()));
      else
        pos = pos.add(Vec3.atLowerCornerOf(Direction.SOUTH.getNormal()));
      targetable.setTargetPos(pos);
      output.add(targetable);
    });
    return this;
  }

  public OffsetDescriptor forward() {
    transforms.add((targetable, output) -> {
      targetable = targetable.simpleCopy();
      var direction = targetable.getFacing();
      var pos = targetable.getTargetPos();
      pos = pos.add(Vec3.atLowerCornerOf(direction.getNormal()));
      targetable.setTargetPos(pos);
      output.add(targetable);
    });
    return this;
  }

  public OffsetDescriptor backwards() {
    transforms.add((targetable, output) -> {
      targetable = targetable.simpleCopy();
      var direction = targetable.getFacing();
      var pos = targetable.getTargetPos();
      pos = pos.add(Vec3.atLowerCornerOf(direction.getOpposite().getNormal()));
      targetable.setTargetPos(pos);
      output.add(targetable);
    });
    return this;
  }

  public OffsetDescriptor duplicate() {
    transforms.add(((targetable, output) -> output.add(targetable.simpleCopy())));
    return this;
  }
}
