package com.ssblur.scriptor.blockentity;

import com.ssblur.scriptor.container.WritingDeskContainer;
import com.ssblur.scriptor.helpers.DictionarySavedData;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.Spell;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class WritingDeskBlockEntity extends BlockEntity implements ExtendedMenuProvider {
  ItemStack ink;
  ItemStack paper;
  ItemStack binder;
  ItemStack output;

  public WritingDeskBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(ScriptorBlockEntities.RUNE.get(), blockPos, blockState);
  }

  @Nullable
  @Override
  public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public CompoundTag getUpdateTag() {
    var tag = super.getUpdateTag();

    saveAdditional(tag);

    return tag;
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);

    if(tag.contains("ink"))
      ink = ItemStack.of(tag.getCompound("ink"));
    if(tag.contains("paper"))
      paper = ItemStack.of(tag.getCompound("paper"));
    if(tag.contains("binder"))
      binder = ItemStack.of(tag.getCompound("binder"));
    if(tag.contains("output"))
      output = ItemStack.of(tag.getCompound("output"));

    setChanged();
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);

    if(ink != null && !ink.isEmpty()) {
      CompoundTag itemTag = new CompoundTag();
      ink.save(itemTag);
      tag.put("ink", itemTag);
    }

    if(paper != null && !paper.isEmpty()) {
      CompoundTag itemTag = new CompoundTag();
      paper.save(itemTag);
      tag.put("paper", itemTag);
    }

    if(binder != null && !binder.isEmpty()) {
      CompoundTag itemTag = new CompoundTag();
      binder.save(itemTag);
      tag.put("binder", itemTag);
    }

    if(output != null && !output.isEmpty()) {
      CompoundTag itemTag = new CompoundTag();
      output.save(itemTag);
      tag.put("output", itemTag);
    }
  }

  @Override
  public void saveExtraData(FriendlyByteBuf buf) {

  }

  @Override
  public Component getDisplayName() {
    return Component.literal("Writing Desk");
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
    return new WritingDeskContainer(i, inventory);
  }
}
