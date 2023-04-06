package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.enchant.ChargedEnchant;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.ItemTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.descriptor.DurationDescriptor;
import com.ssblur.scriptor.word.descriptor.StrengthDescriptor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SmiteAction extends Action {
  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    if(targetable.getLevel().isClientSide) return;
    int strength = 1;
    for(var d: descriptors) {
      if(d instanceof StrengthDescriptor strengthDescriptor)
        strength += strengthDescriptor.strengthModifier();
    }

    if(targetable instanceof ItemTargetable itemTargetable) {
      if(itemTargetable.getTargetItem() != null && !itemTargetable.getTargetItem().isEmpty())
        ChargedEnchant.chargeItem(itemTargetable.getTargetItem(), strength);
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
