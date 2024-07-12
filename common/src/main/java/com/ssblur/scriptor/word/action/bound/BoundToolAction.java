package com.ssblur.scriptor.word.action.bound;

import com.ssblur.scriptor.api.word.Action;
import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.color.CustomColors;
import com.ssblur.scriptor.data_components.ScriptorDataComponents;
import com.ssblur.scriptor.helpers.ItemTargetableHelper;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.duration.DurationDescriptor;
import com.ssblur.scriptor.word.descriptor.power.StrengthDescriptor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.Tool;

import java.util.List;
import java.util.function.Supplier;

public class BoundToolAction extends Action {
  Supplier<DiggerItem> item;
  public BoundToolAction(Supplier<DiggerItem> item) {
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

    itemStack.set(DataComponents.DYED_COLOR, new DyedItemColor(CustomColors.getColor(descriptors), false));
    itemStack.set(ScriptorDataComponents.EXPIRES, caster.getLevel().getGameTime() + (long) Math.floor(duration * 80));
    int finalStrength = (int) (strength * 0.666f);
    itemStack.update(
      DataComponents.TOOL,
      new Tool(List.of(), finalStrength, 1),
      tool -> new Tool(tool.rules(), finalStrength, tool.damagePerBlock())
    );

    ItemTargetableHelper.depositItemStack(targetable, itemStack);
  }

  @Override
  public Cost cost() { return new Cost(6, COSTTYPE.ADDITIVE); }

}
