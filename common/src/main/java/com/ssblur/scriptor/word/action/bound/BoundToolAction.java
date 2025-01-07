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
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BoundToolAction extends Action {
  static final TagKey<Block> NEEDS_STONE_TOOL = TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("needs_stone_tool"));
  static final TagKey<Block> NEEDS_IRON_TOOL = TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("needs_iron_tool"));
  static final TagKey<Block> NEEDS_DIAMOND_TOOL = TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("needs_diamond_tool"));
  static final TagKey<Block> NEEDS_NETHERITE_TOOL = TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("needs_netherite_tool"));

  Supplier<DiggerItem> item;
  List<TagKey<Block>> tags;
  public BoundToolAction(Supplier<DiggerItem> item, List<TagKey<Block>> tags) {
    this.item = item;
    this.tags = tags;
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
    int finalToolLevel = finalStrength / 2;
    itemStack.set(ScriptorDataComponents.TOOL_MINING_LEVEL, finalToolLevel);
    itemStack.update(
      DataComponents.TOOL,
      new Tool(List.of(), finalStrength, 1),
      tool -> {
        List<Tool.Rule> rules = new ArrayList<>();
        if(finalToolLevel < 1)
          rules.add(Tool.Rule.deniesDrops(NEEDS_STONE_TOOL));

        if(finalToolLevel < 2)
          rules.add(Tool.Rule.deniesDrops(NEEDS_IRON_TOOL));

        if(finalToolLevel < 3)
          rules.add(Tool.Rule.deniesDrops(NEEDS_DIAMOND_TOOL));

        if(finalToolLevel < 4)
          rules.add(Tool.Rule.deniesDrops(NEEDS_NETHERITE_TOOL));

        rules.addAll(
          this.tags.stream().map(tag -> Tool.Rule.minesAndDrops(tag, finalStrength)).toList()
        );
        return new Tool(rules, finalStrength, tool.damagePerBlock());
      }
    );

    ItemTargetableHelper.depositItemStack(targetable, itemStack);
  }

  @Override
  public Cost cost() { return new Cost(6, COSTTYPE.ADDITIVE); }

}
