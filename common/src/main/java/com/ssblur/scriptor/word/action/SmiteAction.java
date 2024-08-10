package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.api.word.Action;
import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.data_components.ScriptorDataComponents;
import com.ssblur.scriptor.helpers.ItemTargetableHelper;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.power.StrengthDescriptor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;

public class SmiteAction extends Action {
  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    double strength = 1;
    for(var d: descriptors) {
      if(d instanceof StrengthDescriptor strengthDescriptor)
        strength += strengthDescriptor.strengthModifier();
    }

    var itemTarget = ItemTargetableHelper.getTargetItemStack(targetable);
    if(!itemTarget.isEmpty()) {
      itemTarget.set(ScriptorDataComponents.CHARGES, (int) Math.floor(strength));
      return;
    }

    ServerLevel level = (ServerLevel) targetable.getLevel();
    LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
    if(caster instanceof EntityTargetable entityTargetable && entityTargetable.getTargetEntity() instanceof ServerPlayer player)
      bolt.setCause(player);
    bolt.setPos(targetable.getTargetPos());
    level.addFreshEntity(bolt);
  }

  @Override
  public Cost cost() { return new Cost(12, COSTTYPE.ADDITIVE); }

}
