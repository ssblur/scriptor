package com.ssblur.scriptor.word.descriptor.discount;

import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.InventoryTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.AfterCastDescriptor;
import com.ssblur.scriptor.word.descriptor.CastDescriptor;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.HashMap;

public class ReagentDescriptor extends Descriptor implements CastDescriptor, AfterCastDescriptor {
  static HashMap<Item, Integer> runningTotals = new HashMap<>();

  int cost;
  Item item;

  public ReagentDescriptor(Item item, int cost) {
    this.item = item;
    this.cost = cost;
  }

  @Override
  public Cost cost() {
    return Cost.add(-cost);
  }

  @Override
  public boolean cannotCast(Targetable caster) {
    int c = runningTotals.getOrDefault(item, 0) + 1;
    runningTotals.put(item, c);
    Container container = null;

    if(caster instanceof InventoryTargetable inventory && inventory.getContainer() != null)
      container = inventory.getContainer();
    else if(caster instanceof EntityTargetable entityTargetable && entityTargetable.getTargetEntity() instanceof Player player)
      container = player.getInventory();
      
    if(container != null)
      if(container.countItem(item) >= c) 
        return false;
    
    runningTotals.remove(item);
    return true;
  }

  @Override
  public boolean allowsDuplicates() {
    return true;
  }

  @Override
  public void afterCast(Targetable caster) {
    int c = runningTotals.getOrDefault(item, 0);
    runningTotals.remove(item);
    Container container;

    if(caster instanceof InventoryTargetable inventory && inventory.getContainer() != null)
      container = inventory.getContainer();
    else if(caster instanceof EntityTargetable entityTargetable && entityTargetable.getTargetEntity() instanceof Player player)
      container = player.getInventory();
    else return;

    for(int i = 0; i < container.getContainerSize(); i++)
      if(container.getItem(i).getItem() == item) {
        int maxSize = Math.min(container.getItem(i).getCount(), c);
        c -= maxSize;
        container.getItem(i).shrink(maxSize);
        if(c <= 0) return;
      }
  }
}
