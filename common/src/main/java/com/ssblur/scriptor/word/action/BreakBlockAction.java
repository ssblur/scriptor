package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.api.word.Action;
import com.ssblur.scriptor.helpers.ItemTargetableHelper;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.word.descriptor.power.StrengthDescriptor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

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
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    if(targetable.getLevel().isClientSide) return;
    double strength = 1;
    for(var d: descriptors) {
      if(d instanceof StrengthDescriptor strengthDescriptor)
        strength += strengthDescriptor.strengthModifier();
    }

    var itemTarget = ItemTargetableHelper.getTargetItemStack(targetable, false, itemStack -> !itemStack.isEmpty() && itemStack.isDamageableItem());
    if(!itemTarget.isEmpty())
      if(itemTarget.isDamageableItem()) {
        itemTarget.setDamageValue(itemTarget.getDamageValue() + (int) Math.round(strength));
        return;
      }

    var pos = targetable.getOffsetBlockPos();
    var state = targetable.getLevel().getBlockState(pos);
    if(state.getBlock().defaultDestroyTime() < 0) return;
    var level = targetable.getLevel();
    var tags = state.getBlock().arch$holder().tags().collect(Collectors.toSet());
    int neededStrength = 0;
    for(var tag: tags) {
      if(toolLevelsList.containsKey(tag.location().toString()))
         neededStrength = toolLevelsList.get(tag.location().toString());
    }
    if(strength > neededStrength)
      if(caster instanceof EntityTargetable entityTargetable) {
        if(entityTargetable.getTargetEntity() instanceof Player player)
          state.getBlock().playerDestroy(level, player, pos, state, level.getBlockEntity(pos), new ItemStack(Items.NETHERITE_PICKAXE));
        level.destroyBlock(pos, false, entityTargetable.getTargetEntity(), (int) Math.round(strength));
      } else {
        level.destroyBlock(pos, true, null, (int) Math.round(strength));
      }
  }
  @Override
  public Cost cost() { return new Cost(1, COSTTYPE.ADDITIVE); }
}
