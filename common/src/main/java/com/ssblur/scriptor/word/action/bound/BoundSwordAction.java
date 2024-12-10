package com.ssblur.scriptor.word.action.bound;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.api.word.Action;
import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.color.CustomColors;
import com.ssblur.scriptor.data.components.ScriptorDataComponents;
import com.ssblur.scriptor.helpers.ItemTargetableHelper;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.item.ScriptorItems;
import com.ssblur.scriptor.word.descriptor.duration.DurationDescriptor;
import com.ssblur.scriptor.word.descriptor.power.StrengthDescriptor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class BoundSwordAction extends Action {
  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    double strength = 5;
    double duration = 4;
    for(var d: descriptors) {
      if(d instanceof StrengthDescriptor strengthDescriptor)
        strength += strengthDescriptor.strengthModifier();
      if(d instanceof DurationDescriptor durationDescriptor)
        duration += durationDescriptor.durationModifier();
    }

    var itemStack = new ItemStack(ScriptorItems.INSTANCE.getBOUND_SWORD().get());

    itemStack.set(DataComponents.DYED_COLOR, new DyedItemColor(CustomColors.INSTANCE.getColor(descriptors), false));
    itemStack.set(ScriptorDataComponents.EXPIRES, caster.getLevel().getGameTime() + (long) Math.floor(duration * 80));

    final double finalStrength = strength;
    itemStack.update(
      DataComponents.ATTRIBUTE_MODIFIERS,
      ItemAttributeModifiers.EMPTY,
      modifiers -> modifiers.withModifierAdded(
        Attributes.ATTACK_DAMAGE,
        new AttributeModifier(
          ScriptorMod.INSTANCE.location("bound_sword"),
          finalStrength,
          AttributeModifier.Operation.ADD_VALUE
        ),
        EquipmentSlotGroup.MAINHAND
      )
    );

    ItemTargetableHelper.depositItemStack(targetable, itemStack);
  }

  @Override
  public Cost cost() { return new Cost(6, COSTTYPE.ADDITIVE); }

}
