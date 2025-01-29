package com.ssblur.scriptor.item

import com.mojang.serialization.MapCodec
import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.helpers.loot.ArtifactItemFunction.ArtifactSerializer
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType

object ScriptorLoot {
    val ARTIFACT = ScriptorMod.registerLootFunction("artifact") {
        LootItemFunctionType(MapCodec.assumeMapUnsafe(ArtifactSerializer()))
    }

    fun register() {}
}
