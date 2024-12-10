package com.ssblur.scriptor.blockentity;

import com.ssblur.scriptor.config.ScriptorGameRules;
import com.ssblur.scriptor.data.DictionarySavedData;
import com.ssblur.scriptor.events.network.client.ParticleNetwork;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.Spell;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ChalkBlockEntity extends BlockEntity {
  String word;
  Direction facing;
  public ChalkBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(ScriptorBlockEntities.CHALK.get(), blockPos, blockState);
    word = "";
    facing = Direction.EAST;
  }

  public ChalkBlockEntity(BlockEntityType blockEntityType, BlockPos blockPos, BlockState blockState) {
    super(blockEntityType, blockPos, blockState);
    word = "";
    facing = Direction.EAST;
  }

  public void cast() {
    this.cast(new ArrayList<>(), "", true);
  }

  public void cast(List<BlockPos> visited, String words, boolean primary) {
    boolean continued = false;
    assert level != null;
    visited.add(getBlockPos());
    if(!visited.contains(getBlockPos().north()) && level.getBlockEntity(getBlockPos().north()) instanceof ChalkBlockEntity entity) {
      entity.cast(visited, words + " " + word, primary);
      continued = true;
    }

    if(!visited.contains(getBlockPos().south()) && level.getBlockEntity(getBlockPos().south()) instanceof ChalkBlockEntity entity) {
      entity.cast(visited, words + " " + word, !continued && primary);
      continued = true;
    }

    if(!visited.contains(getBlockPos().east()) && level.getBlockEntity(getBlockPos().east()) instanceof ChalkBlockEntity entity) {
      entity.cast(visited, words + " " + word, !continued && primary);
      continued = true;
    }

    if(!visited.contains(getBlockPos().west()) && level.getBlockEntity(getBlockPos().west()) instanceof ChalkBlockEntity entity) {
      entity.cast(visited, words + " " + word, !continued && primary);
      continued = true;
    }

    if(continued)
      return;

    if(level instanceof ServerLevel server) {
      words = words + " " + word;
      Spell spell = DictionarySavedData.computeIfAbsent(server).parse(words.trim());
      if(spell != null && spell.cost() < server.getGameRules().getInt(ScriptorGameRules.CHALK_MAX_COST)) {
        var target = new Targetable(level, getBlockPos());
        target.setFacing(facing);
        for(var block: visited)
          level.setBlockAndUpdate(block, Blocks.AIR.defaultBlockState());
        level.setBlockAndUpdate(getBlockPos(), Blocks.AIR.defaultBlockState());
        spell.cast(target);
      } else if(primary) {
        ParticleNetwork.fizzle(level, visited.get(0));
        level.playSound(null, visited.get(0), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
      }
    }
  }

  @Nullable
  @Override
  public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
    var tag = super.getUpdateTag(provider);
    tag.putString("scriptor:word", word);
    tag.putInt("scriptor:facing", facing.ordinal());
    return tag;
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.loadAdditional(tag, provider);
    word = tag.getString("scriptor:word");
    facing = Direction.values()[tag.getInt("scriptor:facing")];
    setChanged();
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.saveAdditional(tag, provider);

    tag.putString("scriptor:word", word);
    tag.putInt("scriptor:facing", facing.ordinal());
  }

  public void setWord(String word) {
    this.word = word;
  }

  public String getWord() {
    return this.word;
  }

  public void setFacing(Direction facing) {
    this.facing = facing;
  }

  public Direction getFacing() {
    return this.facing;
  }
}
