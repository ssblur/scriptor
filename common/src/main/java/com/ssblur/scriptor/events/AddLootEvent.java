package com.ssblur.scriptor.events;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.item.ScriptorItems;
import dev.architectury.event.events.common.LootEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;

public class AddLootEvent implements LootEvent.ModifyLootTable {
  record LootItemRecord(ResourceLocation location, LootItemCondition.Builder... conditions) {}

  LootItemRecord[] tomePoolsTier1 = new LootItemRecord[]{
    new LootItemRecord(new ResourceLocation("minecraft", "chests/village/village_plains_house")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/village/village_snowy_house")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/village/village_temple")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/igloo_chest")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/spawn_bonus_chest")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/shipwreck_treasure")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/abandoned_mineshaft")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/ruined_portal")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/simple_dungeon")),
  };
  LootItemRecord[] tomePoolsTier2 = new LootItemRecord[]{
    new LootItemRecord(new ResourceLocation("minecraft", "chests/woodland_mansion")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/bastion_other")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/bastion_bridge")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/woodland_mansion")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/underwater_ruin_big")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/underwater_ruin_small")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/stronghold_corridor")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/nether_bridge")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/stronghold_crossing")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/shipwreck_supply")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/shipwreck_treasure")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/ancient_city")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/ancient_city_ice_box")),
  };
  LootItemRecord[] tomePoolsTier3 = new LootItemRecord[]{
    new LootItemRecord(new ResourceLocation("minecraft", "chests/stronghold_library")),
    new LootItemRecord(new ResourceLocation("minecraft", "gameplay/hero_of_the_village/librarian_gift")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/bastion_other")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/bastion_bridge")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/woodland_mansion")),
    new LootItemRecord(new ResourceLocation("minecraft", "entities/wither")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/ancient_city")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/ancient_city_ice_box")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/nether_bridge")),
  };
  LootItemRecord[] tomePoolsTier4 = new LootItemRecord[]{
    new LootItemRecord(new ResourceLocation("minecraft", "entities/ender_dragon")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/end_city_treasure")),
  };

  LootItemRecord[] scrollPools = new LootItemRecord[]{
    new LootItemRecord(new ResourceLocation("minecraft", "chests/end_city_treasure")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/bastion_other")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/bastion_bridge")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/woodland_mansion")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/stronghold_library")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/ancient_city")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/ancient_city_ice_box")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/nether_bridge")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/stronghold_crossing")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/shipwreck_supply")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/village/village_plains_house")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/village/village_snowy_house")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/village/village_temple")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/igloo_chest")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/spawn_bonus_chest")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/shipwreck_treasure")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/abandoned_mineshaft")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/ruined_portal")),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/simple_dungeon")),
  };

  @Override
  public void modifyLootTable(LootTables lootTables, ResourceLocation id, LootEvent.LootTableModificationContext context, boolean builtin) {
    if(builtin) {
      for(var i: tomePoolsTier1)
        if(id.equals(i.location)) {
          LootPool.Builder builder = LootPool.lootPool();
          builder.add(LootItem.lootTableItem(ScriptorItems.TOME_TIER1.get()));
          for(var condition: i.conditions)
            builder.when(condition);
          context.addPool(builder);
        }
      for(var i: tomePoolsTier2)
        if(id.equals(i.location)) {
          LootPool.Builder builder = LootPool.lootPool();
          builder.add(LootItem.lootTableItem(ScriptorItems.TOME_TIER2.get()));
          for(var condition: i.conditions)
            builder.when(condition);
          context.addPool(builder);
        }
      for(var i: tomePoolsTier3)
        if(id.equals(i.location)) {
          LootPool.Builder builder = LootPool.lootPool();
          builder.add(LootItem.lootTableItem(ScriptorItems.TOME_TIER3.get()));
          for(var condition: i.conditions)
            builder.when(condition);
          context.addPool(builder);
        }
      for(var i: tomePoolsTier4)
        if(id.equals(i.location)) {
          LootPool.Builder builder = LootPool.lootPool();
          builder.add(LootItem.lootTableItem(ScriptorItems.TOME_TIER4.get()));
          for(var condition: i.conditions)
            builder.when(condition);
          context.addPool(builder);
        }
      for(var i: scrollPools)
        if(id.equals(i.location)) {
          LootPool.Builder builder = LootPool.lootPool();
          builder.add(LootItem.lootTableItem(ScriptorItems.IDENTIFY_SCROLL.get()).setWeight(4));
          for(var condition: i.conditions)
            builder.when(condition);
          context.addPool(builder);
        }
    }
  }
}
