package com.ssblur.scriptor.word.subject;

import com.ssblur.scriptor.block.ScriptorBlocks;
import com.ssblur.scriptor.blockentity.RuneBlockEntity;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.messages.TraceNetwork;
import com.ssblur.scriptor.word.Spell;
import com.ssblur.scriptor.word.descriptor.visual.ColorDescriptor;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RuneSubject extends Subject implements InventorySubject{

  @Override
  public CompletableFuture<List<Targetable>> getTargets(Targetable caster, Spell spell) {
    var result = new CompletableFuture<List<Targetable>>();
    if(caster instanceof EntityTargetable entityTargetable && entityTargetable.getTargetEntity() instanceof Player player) {
      TraceNetwork.requestTraceData(player, target -> {

        int color = 0xa020f0;
        for(Descriptor d: spell.descriptors())
          if(d instanceof ColorDescriptor descriptor)
            color = descriptor.getColor();

        BlockPos pos = target.getTargetBlockPos();
        Level level = caster.getLevel();

        if(!level.getBlockState(pos).getMaterial().isReplaceable())
          return;

        level.setBlockAndUpdate(pos, ScriptorBlocks.RUNE.get().defaultBlockState());
        var entity = level.getBlockEntity(pos);
        if(entity instanceof RuneBlockEntity runeBlockEntity) {
          runeBlockEntity.owner = player;
          runeBlockEntity.future = result;
          runeBlockEntity.spell = spell;
          runeBlockEntity.color = color;
          runeBlockEntity.setChanged();
        }
      });
    } else {
      int color = 0xa020f0;
      for(Descriptor d: spell.descriptors())
        if(d instanceof ColorDescriptor descriptor)
          color = descriptor.getColor();

      BlockPos pos = caster.getTargetBlockPos();
      Level level = caster.getLevel();

      if(!level.getBlockState(pos).getMaterial().isReplaceable()) {
        result.complete(List.of());
        return result;
      }

      level.setBlockAndUpdate(pos, ScriptorBlocks.RUNE.get().defaultBlockState());
      var entity = level.getBlockEntity(pos);
      if(entity instanceof RuneBlockEntity runeBlockEntity) {
        runeBlockEntity.future = result;
        runeBlockEntity.spell = spell;
        runeBlockEntity.color = color;
        runeBlockEntity.setChanged();
      }
    }
    return result;
  }

  @Override
  public Cost cost() { return new Cost(1, COSTTYPE.ADDITIVE); }

  @Override
  public void castOnItem(Spell spell, Player player, ItemStack slot) {
    // add a one-time cast enchant
  }
}
