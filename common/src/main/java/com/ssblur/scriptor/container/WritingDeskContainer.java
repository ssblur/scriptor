package com.ssblur.scriptor.container;

import com.ssblur.scriptor.blockentity.WritingDeskBlockEntity;
import net.minecraft.client.gui.screens.inventory.FurnaceScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.jetbrains.annotations.Nullable;

public class WritingDeskContainer extends AbstractContainerMenu {
  Container container;

  public WritingDeskContainer(int id, Inventory inventory, WritingDeskBlockEntity blockEntity) {
    super(ScriptorContainers.WRITING_DESK_CONTAINER.get(), id);

    if(inventory.player.level.isClientSide)
      container = new SimpleContainer(4);
    else
      container = blockEntity;

    int j;
    for(j = 0; j < 3; ++j) {
      for(int k = 0; k < 9; ++k) {
        this.addSlot(new Slot(inventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
      }
    }

    for(j = 0; j < 9; ++j) {
      this.addSlot(new Slot(inventory, j, 8 + j * 18, 142));
    }

    this.addSlot(new Slot(container, 0, 8, 30));
    this.addSlot(new Slot(container, 1, 8, 48));
    this.addSlot(new Slot(container, 2, 26, 30));
    this.addSlot(new Slot(container, 3, 26, 48));
  }

  @Override
  public ItemStack quickMoveStack(Player player, int i) {
    var stack = this.slots.get(i).getItem();
    for(var slot: this.slots.subList(36, 40)) {
      var toStack = slot.getItem();
      if(!moveItemStackTo(stack, i, 0, false)) {
        return ItemStack.EMPTY;
      }
    }
    return this.slots.get(i).getItem();
  }

  @Override
  public boolean stillValid(Player player) {
    return true;
  }
}
