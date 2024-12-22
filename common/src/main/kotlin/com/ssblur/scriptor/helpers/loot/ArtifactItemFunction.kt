package com.ssblur.scriptor.helpers.loot

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.data.saved_data.DictionarySavedData.Companion.computeIfAbsent
import com.ssblur.scriptor.item.ScriptorLoot
import com.ssblur.scriptor.resources.Artifacts
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.functions.LootItemFunction
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType

class ArtifactItemFunction : LootItemFunction {
    override fun getType(): LootItemFunctionType<*> {
        return ScriptorLoot.ARTIFACT.get()
    }

    override fun apply(itemStack: ItemStack, lootContext: LootContext): ItemStack {
        val artifact = Artifacts.getRandomArtifact()
        val spell = computeIfAbsent(lootContext.level).generate(artifact.getSpell())

        itemStack.set(ScriptorDataComponents.SPELL, spell)
        itemStack[DataComponents.ITEM_NAME] = Component.translatable(artifact.name ?: "")
        return itemStack
    }

    class ArtifactSerializer : Codec<ArtifactItemFunction> {
        override fun <T> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<ArtifactItemFunction, T>> {
            return DataResult.success(null)
        }

        override fun <T> encode(input: ArtifactItemFunction, ops: DynamicOps<T>, prefix: T): DataResult<T> {
            return DataResult.success(null)
        }
    }
}
