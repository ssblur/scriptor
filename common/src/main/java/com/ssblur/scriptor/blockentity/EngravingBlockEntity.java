package com.ssblur.scriptor.blockentity;

import com.ssblur.scriptor.block.ChalkBlock;
import com.ssblur.scriptor.data.DictionarySavedData;
import com.ssblur.scriptor.events.network.ParticleNetwork;
import com.ssblur.scriptor.gamerules.ScriptorGameRules;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.Spell;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EngravingBlockEntity extends ChalkBlockEntity {
  int cooldown;
  public EngravingBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(ScriptorBlockEntities.ENGRAVING.get(), blockPos, blockState);
  }

  public void cast(List<BlockPos> visited, String words, boolean primary) {
    if(cooldown > 0) {
      ParticleNetwork.fizzle(level, getBlockPos());
      level.playSound(null, visited.get(0), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
      return;
    }

    boolean continued = false;
    assert level != null;
    visited.add(getBlockPos());
    if(!visited.contains(getBlockPos().north()) && level.getBlockEntity(getBlockPos().north()) instanceof EngravingBlockEntity entity) {
      entity.cast(visited, words + " " + word, primary);
      continued = true;
    }

    if(!visited.contains(getBlockPos().south()) && level.getBlockEntity(getBlockPos().south()) instanceof EngravingBlockEntity entity) {
      entity.cast(visited, words + " " + word, !continued && primary);
      continued = true;
    }

    if(!visited.contains(getBlockPos().east()) && level.getBlockEntity(getBlockPos().east()) instanceof EngravingBlockEntity entity) {
      entity.cast(visited, words + " " + word, !continued && primary);
      continued = true;
    }

    if(!visited.contains(getBlockPos().west()) && level.getBlockEntity(getBlockPos().west()) instanceof EngravingBlockEntity entity) {
      entity.cast(visited, words + " " + word, !continued && primary);
      continued = true;
    }

    if(continued)
      return;

    if(level instanceof ServerLevel server) {
      words = words + " " + word;
      Spell spell = DictionarySavedData.computeIfAbsent(server).parse(words.trim());
      if(spell != null) {
        var target = new Targetable(level, getBlockPos());
        target.setFacing(facing);
        for(var block: visited)
          if(level.getBlockEntity(block) instanceof EngravingBlockEntity engraving)
            engraving.cooldown += spell.cost() * 20;
        this.cooldown += spell.cost() * 20;
        spell.cast(target);
      } else if(primary) {
        ParticleNetwork.fizzle(level, visited.get(0));
        level.playSound(null, visited.get(0), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
      }
    }
  }

  public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T entity) {
    if(level.isClientSide) return;
    if(entity instanceof EngravingBlockEntity tile) tile.cooldown = Math.max(0, tile.cooldown - 1);
  }
}
