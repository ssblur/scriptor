package com.ssblur.scriptor.events;

import com.ssblur.scriptor.ScriptorMod;
import dev.architectury.event.events.common.LootEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

public class AddLootEvent implements LootEvent.ModifyLootTable {
  ResourceLocation[] tomePools = new ResourceLocation[]{
    new ResourceLocation("minecraft", "chests/stronghold_library"),
    new ResourceLocation("minecraft", "gameplay/hero_of_the_village/librarian_gift"),
    new ResourceLocation("minecraft", "chests/bastion_other"),
    new ResourceLocation("minecraft", "chests/woodland_mansion"),
  };

  @Override
  public void modifyLootTable(LootTables lootTables, ResourceLocation id, LootEvent.LootTableModificationContext context, boolean builtin) {
    if(builtin) {
      for(var i: tomePools)
        if(id.equals(i)) {
          LootPool.Builder builder = LootPool.lootPool();
          builder.add(LootItem.lootTableItem(ScriptorMod.TOME.get()));
          context.addPool(builder);
        }
    }
  }
}
