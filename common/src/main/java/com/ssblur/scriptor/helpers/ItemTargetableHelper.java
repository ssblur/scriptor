package com.ssblur.scriptor.helpers;

import com.ssblur.scriptor.helpers.targetable.InventoryTargetable;
import com.ssblur.scriptor.helpers.targetable.ItemTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Function;
import java.util.function.Predicate;

public class ItemTargetableHelper {
  public static ItemStack getTargetItemStack(Targetable targetable, boolean aggressive) {
    if(targetable instanceof ItemTargetable itemTargetable && (itemTargetable.shouldTargetItem() || aggressive))
      return itemTargetable.getTargetItem();
    else if(targetable instanceof InventoryTargetable inventoryTargetable)
      if(inventoryTargetable.getContainer() != null) {
        int slot;
        if (inventoryTargetable.shouldIgnoreTargetedSlot())
          slot = inventoryTargetable.getFirstFilledSlot();
        else
          slot = inventoryTargetable.getTargetedSlot();
        return inventoryTargetable.getContainer().getItem(slot);
      }
    return ItemStack.EMPTY;
  }

  public static ItemStack getTargetItemStack(Targetable targetable, boolean aggressive, Predicate<ItemStack> condition) {
    if(targetable instanceof ItemTargetable itemTargetable && (itemTargetable.shouldTargetItem() || aggressive)) {
      if (condition.test(itemTargetable.getTargetItem()))
        return itemTargetable.getTargetItem();
    } else if(targetable instanceof InventoryTargetable inventoryTargetable)
      if(inventoryTargetable.getContainer() != null) {
        int slot;
        if (inventoryTargetable.shouldIgnoreTargetedSlot())
          slot = inventoryTargetable.getFirstMatchingSlot(condition);
        else
          slot = inventoryTargetable.getTargetedSlot();
        if(condition.test(inventoryTargetable.getContainer().getItem(slot)))
          return inventoryTargetable.getContainer().getItem(slot);
      }
    return ItemStack.EMPTY;
  }

  public static ItemStack getTargetItemStack(Targetable targetable) {
    return getTargetItemStack(targetable, false);
  }

  public static ItemStack getTargetItemStackAggressively(Targetable targetable) {
    return getTargetItemStack(targetable, true);
  }

  public static void depositItemStack(Targetable targetable, ItemStack itemStack) {
    if(targetable instanceof InventoryTargetable inventoryTargetable) {
      if (inventoryTargetable.getContainer() != null) {
        int slot = inventoryTargetable.getFirstMatchingSlotNotEmpty(itemStack);
        if(slot >= 0) {
          var item = inventoryTargetable.getContainer().getItem(slot);
          if (item.getCount() + itemStack.getCount() < item.getMaxStackSize()) {
            item.grow(itemStack.getCount());
            return;
          } else {
            int diff = item.getMaxStackSize() - item.getCount();
            item.grow(diff);
            itemStack.shrink(diff);
          }
        }

        slot = inventoryTargetable.getFirstMatchingSlot(ItemStack::isEmpty);
        if(slot >= 0) {
          inventoryTargetable.getContainer().setItem(slot, itemStack.copy());
          return;
        }
      }
    }

    if(targetable instanceof ItemTargetable itemTargetable)
      if(itemTargetable.getTargetEntity() instanceof Player player)
        if(player.addItem(itemStack))
          return;

    var pos = targetable.getTargetPos();
    ItemEntity entity = new ItemEntity(
      targetable.getLevel(),
      pos.x(),
      pos.y() + 1,
      pos.z(),
      itemStack
    );
    targetable.getLevel().addFreshEntity(entity);
  }
}
