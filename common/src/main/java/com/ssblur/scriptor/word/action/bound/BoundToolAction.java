package com.ssblur.scriptor.word.action.bound;

import com.ssblur.scriptor.api.word.Action;
import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.color.CustomColors;
import com.ssblur.scriptor.helpers.ItemTargetableHelper;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.item.ScriptorItems;
import com.ssblur.scriptor.word.descriptor.duration.DurationDescriptor;
import com.ssblur.scriptor.word.descriptor.power.StrengthDescriptor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class BoundToolAction extends Action {
  Supplier<Item> item;
  public BoundToolAction(Supplier<Item> item) {
    this.item = item;
  }
  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    float strength = 6;
    double duration = 4;
    for(var d: descriptors) {
      if(d instanceof StrengthDescriptor strengthDescriptor)
        strength += (float) strengthDescriptor.strengthModifier();
      if(d instanceof DurationDescriptor durationDescriptor)
        duration += durationDescriptor.durationModifier();
    }

    var itemStack = new ItemStack(this.item.get());

    var tag = new CompoundTag();

    var display = new CompoundTag();
    display.putInt("color", CustomColors.getColor(descriptors));
    tag.put("display", display);

    var scriptor = new CompoundTag();
    scriptor.putLong("expire", caster.getLevel().getGameTime() + (long) Math.floor(duration * 80));
    scriptor.putFloat("efficiency", strength * 0.666f);
    tag.put("scriptor", scriptor);

    itemStack.setTag(tag);

    ItemTargetableHelper.depositItemStack(targetable, itemStack);
  }

  @Override
  public Cost cost() { return new Cost(6, COSTTYPE.ADDITIVE); }

}
