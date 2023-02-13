package com.ssblur.scriptor.block.blockstates;

import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;

public enum HorizontalFacing implements StringRepresentable {
  NORTH(Direction.NORTH),
  SOUTH(Direction.SOUTH),
  EAST(Direction.EAST),
  WEST(Direction.WEST);

  public final Direction direction;
  HorizontalFacing(Direction direction) {
    this.direction = direction;
  }

  public static HorizontalFacing of(Direction direction) {
    for(var value: HorizontalFacing.values())
      if(value.direction == direction)
        return value;
    return NORTH;
  }

  public static HorizontalFacing forFacing(BlockPlaceContext context) {
    return of(context.getHorizontalDirection().getOpposite());
  }

  @Override
  public String getSerializedName() {
    return this.name().toLowerCase();
  }
}
