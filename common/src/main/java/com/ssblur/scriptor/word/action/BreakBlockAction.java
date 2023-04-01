package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.ItemTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.descriptor.StrengthDescriptor;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

public class BreakBlockAction extends Action {
  static HashMap<String, Integer> toolLevelsList = new HashMap<>();
  static {
    toolLevelsList.put("minecraft:needs_stone_tool", 1);
    toolLevelsList.put("minecraft:needs_iron_tool", 2);
    toolLevelsList.put("minecraft:needs_diamond_tool", 3);
    toolLevelsList.put("minecraft:needs_netherite_tool", 4);
  }

  @Override
  public void apply(Entity caster, Targetable targetable, Descriptor[] descriptors) {
    double strength = 1;
    for(var d: descriptors) {
      if(d instanceof StrengthDescriptor strengthDescriptor)
        strength += strengthDescriptor.strengthModifier();
    }

    if(targetable instanceof ItemTargetable itemTargetable) {
      var item = itemTargetable.getTargetItem();
      if(item != null && !item.isEmpty()) {
        if(item.isDamageableItem()) {
          item.setDamageValue(item.getDamageValue() + (int) Math.round(strength * 2));
          return;
        }
      }
    }

    var pos = targetable.getOffsetBlockPos();
    var state = targetable.getLevel().getBlockState(pos);
    var level = targetable.getLevel();
    var tags = Registry.BLOCK.getResourceKey(state.getBlock())
      .map(key -> Registry.BLOCK.getHolderOrThrow(key).tags().collect(Collectors.toSet()))
      .orElse(Collections.emptySet());
    int neededStrength = 0;
    for(var tag: tags) {
      if(toolLevelsList.containsKey(tag.location().toString()))
         neededStrength = toolLevelsList.get(tag.location().toString());
    }
    if(strength > neededStrength)
      level.destroyBlock(pos, true, caster, (int) Math.round(strength));
  }
  @Override
  public Cost cost() { return new Cost(1, COSTTYPE.ADDITIVE); }
}
