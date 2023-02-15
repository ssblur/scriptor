package com.ssblur.scriptor.container;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class WritingDeskContainer extends AbstractContainerMenu {

  public WritingDeskContainer(int id, Inventory inventory) {
    super(ScriptorContainers.WRITING_DESK_CONTAINER.get(), id);
  }

  @Override
  public ItemStack quickMoveStack(Player player, int i) {
    return null;
  }

  @Override
  public boolean stillValid(Player player) {
    return true;
  }
}
