package com.ssblur.scriptor.word.action.bound;

import com.ssblur.scriptor.api.word.Action;
import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.color.CustomColors;
import com.ssblur.scriptor.color.interfaces.Colorable;
import com.ssblur.scriptor.color.interfaces.ColorableBlock;
import com.ssblur.scriptor.color.interfaces.ColorableItem;
import com.ssblur.scriptor.helpers.ItemTargetableHelper;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.item.ScriptorItems;
import com.ssblur.scriptor.registry.colorable.ColorableBlockRegistry;
import com.ssblur.scriptor.word.descriptor.duration.DurationDescriptor;
import com.ssblur.scriptor.word.descriptor.power.StrengthDescriptor;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

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

    var itemStack = new ItemStack(ScriptorItems.BOUND_SWORD.get());

    var tag = new CompoundTag();

    var display = new CompoundTag();
    display.putInt("color", CustomColors.getColor(descriptors));
    tag.put("display", display);

    var scriptor = new CompoundTag();
    scriptor.putLong("expire", caster.getLevel().getGameTime() + (long) Math.floor(duration * 80));
    tag.put("scriptor", scriptor);

    itemStack.setTag(tag);

    itemStack.addAttributeModifier(
      Attributes.ATTACK_DAMAGE,
      new AttributeModifier("Bound Sword", strength,
        AttributeModifier.Operation.ADDITION), EquipmentSlot.MAINHAND
    );

    ItemTargetableHelper.depositItemStack(targetable, itemStack);
  }

  @Override
  public Cost cost() { return new Cost(6, COSTTYPE.ADDITIVE); }

}
