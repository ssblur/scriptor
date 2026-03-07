package com.ssblur.scriptor.item

import com.mojang.serialization.MapCodec
import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.helpers.loot.ArtifactItemFunction
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType

object ScriptorLoot {
  val ARTIFACT = ScriptorMod.registerLootFunction("artifact") {
    LootItemFunctionType(MapCodec.unit(ArtifactItemFunction()))
  }

  fun register() {}
}
