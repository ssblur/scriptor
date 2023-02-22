package com.ssblur.scriptor.blockentity;

import com.ssblur.scriptor.container.WritingDeskContainer;
import com.ssblur.scriptor.helpers.DictionarySavedData;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.Spell;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
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

public class WritingDeskBlockEntity extends BlockEntity implements ExtendedMenuProvider, WorldlyContainer {
  public static final int INK_SLOT = 0;
  public static final int INK_RETURN_SLOT = 1;
  public static final int PAPER_SLOT = 2;
  public static final int BINDER_SLOT = 3;
  public static final int OUTPUT_SLOT = 4;

  NonNullList<ItemStack> items;

  public WritingDeskBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(ScriptorBlockEntities.WRITING_DESK.get(), blockPos, blockState);
    items = NonNullList.withSize(5, ItemStack.EMPTY);
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

    ContainerHelper.loadAllItems(tag, items);

    setChanged();
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    ContainerHelper.saveAllItems(tag, items);
  }

  @Override
  public void saveExtraData(FriendlyByteBuf buf) {
    CompoundTag tag = new CompoundTag();
    saveAdditional(tag);
    buf.writeNbt(tag);
  }

  @Override
  public Component getDisplayName() {
    return Component.literal("Writing Desk");
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
    return new WritingDeskContainer(i, inventory, this);
  }

  public NonNullList<ItemStack> getItems() { return items; }

  @Override
  public int getContainerSize() {
    return 0;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  public ItemStack getItem(int i) {
    return this.items.get(i);
  }

  public ItemStack removeItem(int i, int j) {
    return ContainerHelper.removeItem(this.items, i, j);
  }

  public ItemStack removeItemNoUpdate(int i) {
    return ContainerHelper.takeItem(this.items, i);
  }

  public void setItem(int i, ItemStack item) { this.items.set(i, item); }

  @Override
  public boolean stillValid(Player player) {
    return false;
  }

  @Override
  public int[] getSlotsForFace(Direction direction) {
    if(direction == Direction.DOWN)
      return new int[] {1, 4};
    return new int[] {};
  }

  @Override
  public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
    return false;
  }

  @Override
  public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
    return direction == Direction.DOWN;
  }

  @Override
  public void clearContent() {
    items.clear();
  }
}
