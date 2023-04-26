package com.ssblur.scriptor.item.casters;

import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CoordinateCasterCrystal extends CasterCrystal {
  public CoordinateCasterCrystal(Properties properties) {
    super(properties);
  }

  @Override
  public List<Targetable> getTargetables(ItemStack itemStack, Level level) {
    ArrayList<Targetable> list = new ArrayList<>();
    if(itemStack.getTag() != null && itemStack.getTag().contains("coordinates")){
      var coords = itemStack.getTag().getList("coordinates", ListTag.TAG_LONG_ARRAY);
      for(var tag: coords) {
        if(tag instanceof LongArrayTag array)
          list.add(new Targetable(level, new BlockPos(array.get(0).getAsLong(), array.get(1).getAsLong(), array.get(2).getAsLong())));
      }
    }
    return list;
  }
}
