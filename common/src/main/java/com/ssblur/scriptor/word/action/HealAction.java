package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.damage.ScriptorDamage;
import com.ssblur.scriptor.helpers.ItemTargetableHelper;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.InventoryTargetable;
import com.ssblur.scriptor.helpers.targetable.ItemTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.descriptor.power.StrengthDescriptor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;

public class HealAction extends Action {
  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    double strength = 2;
    for(var d: descriptors) {
      if(d instanceof StrengthDescriptor strengthDescriptor)
        strength += strengthDescriptor.strengthModifier();
    }

    strength = Math.max(strength, 0);

    var itemTarget = ItemTargetableHelper.getTargetItemStack(targetable, false, itemStack -> !itemStack.isEmpty() && itemStack.isDamageableItem());
    if (itemTarget.isDamageableItem()) {
      itemTarget.setDamageValue(itemTarget.getDamageValue() - (int) Math.round(strength));
      return;
    }

    if(targetable instanceof ItemTargetable itemTargetable && itemTargetable.shouldTargetItem()) {
      var item = itemTargetable.getTargetItem();
      if(item != null && !item.isEmpty()) {
        if(item.isDamageableItem()) {
          item.setDamageValue(item.getDamageValue() - (int) Math.round(strength));
          return;
        }
      }
    }

    if(targetable instanceof EntityTargetable entityTargetable) {
      Entity entity = entityTargetable.getTargetEntity();
      Entity source = caster instanceof EntityTargetable casterEntity ? casterEntity.getTargetEntity() : entity;
      if(entity instanceof LivingEntity target)
        if(target.getMobType() == MobType.UNDEAD)
          target.hurt(ScriptorDamage.magic(source, source), (float) strength);
        else
          target.heal((float) strength);
    }
  }
  @Override
  public Cost cost() { return new Cost(4, COSTTYPE.ADDITIVE); }
}
