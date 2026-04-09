package com.ssblur.scriptor.helpers.loot

import com.ssblur.scriptor.item.ScriptorLoot
import com.ssblur.scriptor.resources.Artifacts
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.functions.LootItemFunction
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType

class ArtifactItemFunction: LootItemFunction {
  override fun getType(): LootItemFunctionType<*> {
    return ScriptorLoot.ARTIFACT.get()
  }

  override fun apply(itemStack: ItemStack, lootContext: LootContext): ItemStack {
    return Artifacts.getRandomArtifact().applyToItem(itemStack, lootContext.level)
  }
}
