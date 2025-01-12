package com.ssblur.scriptor.events

import com.ssblur.scriptor.helpers.loot.ArtifactItemFunction
import com.ssblur.scriptor.item.ScriptorItems.ARTIFACT_1
import com.ssblur.scriptor.item.ScriptorItems.ARTIFACT_2
import com.ssblur.scriptor.item.ScriptorItems.ARTIFACT_3
import com.ssblur.scriptor.item.ScriptorItems.ARTIFACT_4
import com.ssblur.scriptor.item.ScriptorItems.IDENTIFY_SCROLL
import com.ssblur.scriptor.item.ScriptorItems.SCRAP_TIER1
import com.ssblur.scriptor.item.ScriptorItems.SCRAP_TIER2
import com.ssblur.scriptor.item.ScriptorItems.SCRAP_TIER3
import com.ssblur.scriptor.item.ScriptorItems.TOME_TIER1
import com.ssblur.scriptor.item.ScriptorItems.TOME_TIER2
import com.ssblur.scriptor.item.ScriptorItems.TOME_TIER3
import com.ssblur.scriptor.item.ScriptorItems.TOME_TIER4
import com.ssblur.unfocused.event.common.LootTablePopulateEvent
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.LootItemFunction
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition

object AddLootEvent {
    class LootItemRecord(
        val location: ResourceLocation?,
        val chance: Float,
        vararg val builders: LootItemFunction.Builder
    )
    var pools: HashMap<Holder<Item>, Array<LootItemRecord>> = HashMap()

    init {
        val tier1 = arrayOf(
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/village/village_plains_house"), 0.2f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/village/village_snowy_house"), 0.2f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/village/village_temple"), 0.2f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/igloo_chest"), 0.2f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/spawn_bonus_chest"), 1f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/shipwreck_treasure"), 0.6f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/abandoned_mineshaft"), 0.6f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/ruined_portal"), 0.3f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/simple_dungeon"), 0.6f),
        )
        pools[TOME_TIER1] = tier1
        pools[SCRAP_TIER1] = tier1

        val tier2 = arrayOf(
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/woodland_mansion"), 0.3f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/bastion_other"), 0.6f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/bastion_bridge"), 0.6f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/underwater_ruin_big"), 0.6f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/underwater_ruin_small"), 0.6f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/stronghold_corridor"), 0.4f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/nether_bridge"), 0.6f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/stronghold_crossing"), 0.6f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/shipwreck_supply"), 0.4f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/shipwreck_treasure"), 0.8f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/ancient_city"), 0.8f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/ancient_city_ice_box"), 0.6f),
        )
        pools[TOME_TIER2] = tier2
        pools[SCRAP_TIER2] = tier2

        val tier3 = arrayOf(
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/stronghold_library"), 1f),
            LootItemRecord(
                ResourceLocation.tryBuild("minecraft", "gameplay/hero_of_the_village/librarian_gift"),
                1f
            ),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/bastion_other"), 0.6f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/bastion_bridge"), 0.6f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/woodland_mansion"), 0.6f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "entities/wither"), 1f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/ancient_city"), 0.8f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/ancient_city_ice_box"), 0.6f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/nether_bridge"), 0.6f),
        )
        pools[TOME_TIER3] = tier3
        pools[SCRAP_TIER3] = tier3

        pools[TOME_TIER4] = arrayOf(
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "entities/ender_dragon"), 1f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/end_city_treasure"), 0.9f),
        )

        pools[IDENTIFY_SCROLL] = arrayOf(
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/end_city_treasure"), 0.3f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/bastion_other"), 0.3f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/bastion_bridge"), 0.3f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/woodland_mansion"), 0.3f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/stronghold_library"), 0.3f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/ancient_city"), 0.3f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/ancient_city_ice_box"), 0.3f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/nether_bridge"), 0.3f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/stronghold_crossing"), 0.3f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/shipwreck_supply"), 0.3f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/village/village_plains_house"), 0.2f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/village/village_snowy_house"), 0.2f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/village/village_temple"), 0.2f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/igloo_chest"), 0.3f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/spawn_bonus_chest"), 1f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/shipwreck_treasure"), 0.3f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/abandoned_mineshaft"), 0.3f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/ruined_portal"), 0.3f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "chests/simple_dungeon"), 0.3f),
            LootItemRecord(ResourceLocation.tryBuild("minecraft", "entities/witch"), 0.05f),
        )

        val numberOfArtifacts = 4f
        val artifacts = arrayOf(
            LootItemRecord(
                ResourceLocation.tryBuild("minecraft", "chests/end_city_treasure"), 0.05f / numberOfArtifacts,
                LootItemFunction.Builder { ArtifactItemFunction() }
            ),
            LootItemRecord(
                ResourceLocation.tryBuild("minecraft", "chests/stronghold_library"), 0.05f / numberOfArtifacts,
                LootItemFunction.Builder { ArtifactItemFunction() }
            ),
            LootItemRecord(
                ResourceLocation.tryBuild("minecraft", "chests/bastion_other"), 0.05f / numberOfArtifacts,
                LootItemFunction.Builder { ArtifactItemFunction() }),
            LootItemRecord(
                ResourceLocation.tryBuild("minecraft", "chests/bastion_bridge"), 0.05f / numberOfArtifacts,
                LootItemFunction.Builder { ArtifactItemFunction() }),
            LootItemRecord(
                ResourceLocation.tryBuild("minecraft", "chests/woodland_mansion"), 0.1f / numberOfArtifacts,
                LootItemFunction.Builder { ArtifactItemFunction() }),
            LootItemRecord(
                ResourceLocation.tryBuild("minecraft", "chests/ancient_city"), 0.1f / numberOfArtifacts,
                LootItemFunction.Builder { ArtifactItemFunction() }),
            LootItemRecord(
                ResourceLocation.tryBuild("minecraft", "chests/ancient_city_ice_box"), 0.1f / numberOfArtifacts,
                LootItemFunction.Builder { ArtifactItemFunction() }),
            LootItemRecord(
                ResourceLocation.tryBuild("minecraft", "chests/nether_bridge"), 0.05f / numberOfArtifacts,
                LootItemFunction.Builder { ArtifactItemFunction() }),
            LootItemRecord(
                ResourceLocation.tryBuild("minecraft", "chests/ocean_ruin_warm"), 0.1f / numberOfArtifacts,
                LootItemFunction.Builder { ArtifactItemFunction() }),
            LootItemRecord(
                ResourceLocation.tryBuild("minecraft", "chests/ocean_ruin_cold"), 0.1f / numberOfArtifacts,
                LootItemFunction.Builder { ArtifactItemFunction() }),
            LootItemRecord(
                ResourceLocation.tryBuild("minecraft", "chests/desert_pyramid"), 0.1f / numberOfArtifacts,
                LootItemFunction.Builder { ArtifactItemFunction() }),
        )
        pools[ARTIFACT_1] = artifacts
        pools[ARTIFACT_2] = artifacts
        pools[ARTIFACT_3] = artifacts
        pools[ARTIFACT_4] = artifacts

        LootTablePopulateEvent.register{ context ->
            if (context.isBuiltin)
                for (pool in pools.keys)
                    for (i in pools[pool]!!)
                        if (context.id.location() == i.location) {
                            val builder = LootPool.lootPool()
                            builder.`when`(LootItemRandomChanceCondition.randomChance(i.chance))
                            builder.add(LootItem.lootTableItem(pool.value()))
                            for (b in i.builders) builder.apply(b)
                            context.pool += builder
                        }
        }
    }
}