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
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;

public class AddLootEvent implements LootEvent.ModifyLootTable {
  record LootItemRecord(ResourceLocation location, float chance, LootItemCondition.Builder... conditions) {}

  LootItemRecord[] tomePoolsTier1 = new LootItemRecord[]{
    new LootItemRecord(new ResourceLocation("minecraft", "chests/village/village_plains_house"), 0.2f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/village/village_snowy_house"), 0.2f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/village/village_temple"), 0.2f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/igloo_chest"), 0.2f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/spawn_bonus_chest"), 1f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/shipwreck_treasure"), 0.6f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/abandoned_mineshaft"), 0.6f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/ruined_portal"), 0.3f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/simple_dungeon"), 0.6f),
  };
  LootItemRecord[] tomePoolsTier2 = new LootItemRecord[]{
    new LootItemRecord(new ResourceLocation("minecraft", "chests/woodland_mansion"), 0.3f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/bastion_other"), 0.6f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/bastion_bridge"), 0.6f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/underwater_ruin_big"), 0.6f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/underwater_ruin_small"), 0.6f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/stronghold_corridor"), 0.4f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/nether_bridge"), 0.6f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/stronghold_crossing"), 0.6f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/shipwreck_supply"), 0.4f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/shipwreck_treasure"), 0.8f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/ancient_city"), 0.8f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/ancient_city_ice_box"), 0.6f),
  };
  LootItemRecord[] tomePoolsTier3 = new LootItemRecord[]{
    new LootItemRecord(new ResourceLocation("minecraft", "chests/stronghold_library"), 1f),
    new LootItemRecord(new ResourceLocation("minecraft", "gameplay/hero_of_the_village/librarian_gift"), 1f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/bastion_other"), 0.6f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/bastion_bridge"), 0.6f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/woodland_mansion"), 0.6f),
    new LootItemRecord(new ResourceLocation("minecraft", "entities/wither"), 1f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/ancient_city"), 0.8f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/ancient_city_ice_box"), 0.6f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/nether_bridge"), 0.6f),
  };
  LootItemRecord[] tomePoolsTier4 = new LootItemRecord[]{
    new LootItemRecord(new ResourceLocation("minecraft", "entities/ender_dragon"), 1f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/end_city_treasure"), 0.9f),
  };

  LootItemRecord[] scrollPools = new LootItemRecord[]{
    new LootItemRecord(new ResourceLocation("minecraft", "chests/end_city_treasure"), 0.3f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/bastion_other"), 0.3f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/bastion_bridge"), 0.3f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/woodland_mansion"), 0.3f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/stronghold_library"), 0.3f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/ancient_city"), 0.3f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/ancient_city_ice_box"), 0.3f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/nether_bridge"), 0.3f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/stronghold_crossing"), 0.3f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/shipwreck_supply"), 0.3f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/village/village_plains_house"), 0.2f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/village/village_snowy_house"), 0.2f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/village/village_temple"), 0.2f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/igloo_chest"), 0.3f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/spawn_bonus_chest"), 1f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/shipwreck_treasure"), 0.3f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/abandoned_mineshaft"), 0.3f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/ruined_portal"), 0.3f),
    new LootItemRecord(new ResourceLocation("minecraft", "chests/simple_dungeon"), 0.3f),
    new LootItemRecord(new ResourceLocation("minecraft", "entities/witch"), 0.05f),
  };

  @Override
  public void modifyLootTable(LootTables lootTables, ResourceLocation id, LootEvent.LootTableModificationContext context, boolean builtin) {
    if(builtin) {
      for(var i: tomePoolsTier1)
        if(id.equals(i.location)) {
          LootPool.Builder builder = LootPool.lootPool();
          builder.when(LootItemRandomChanceCondition.randomChance(i.chance));
          builder.add(LootItem.lootTableItem(ScriptorItems.TOME_TIER1.get()));
          for(var condition: i.conditions)
            builder.when(condition);
          context.addPool(builder);
        }
      for(var i: tomePoolsTier2)
        if(id.equals(i.location)) {
          LootPool.Builder builder = LootPool.lootPool();
          builder.when(LootItemRandomChanceCondition.randomChance(i.chance));
          builder.add(LootItem.lootTableItem(ScriptorItems.TOME_TIER2.get()));
          for(var condition: i.conditions)
            builder.when(condition);
          context.addPool(builder);
        }
      for(var i: tomePoolsTier3)
        if(id.equals(i.location)) {
          LootPool.Builder builder = LootPool.lootPool();
          builder.when(LootItemRandomChanceCondition.randomChance(i.chance));
          builder.add(LootItem.lootTableItem(ScriptorItems.TOME_TIER3.get()));
          for(var condition: i.conditions)
            builder.when(condition);
          context.addPool(builder);
        }
      for(var i: tomePoolsTier4)
        if(id.equals(i.location)) {
          LootPool.Builder builder = LootPool.lootPool();
          builder.when(LootItemRandomChanceCondition.randomChance(i.chance));
          builder.add(LootItem.lootTableItem(ScriptorItems.TOME_TIER4.get()));
          for(var condition: i.conditions)
            builder.when(condition);
          context.addPool(builder);
        }
      for(var i: scrollPools)
        if(id.equals(i.location)) {
          LootPool.Builder builder = LootPool.lootPool();
          builder.when(LootItemRandomChanceCondition.randomChance(i.chance));
          builder.add(LootItem.lootTableItem(ScriptorItems.IDENTIFY_SCROLL.get()).setWeight(4));
          for(var condition: i.conditions)
            builder.when(condition);
          context.addPool(builder);
        }
    }
  }
}
