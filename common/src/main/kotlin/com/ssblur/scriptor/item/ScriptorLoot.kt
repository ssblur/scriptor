package com.ssblur.scriptor.item

import com.mojang.serialization.MapCodec
import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.helpers.loot.ArtifactItemFunction
import com.ssblur.scriptor.helpers.loot.ArtifactItemFunction.ArtifactSerializer
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType

object ScriptorLoot {
    val LOOT_ITEM_FUNCTION_TYPES: DeferredRegister<LootItemFunctionType<*>> =
        DeferredRegister.create(ScriptorMod.MOD_ID, Registries.LOOT_FUNCTION_TYPE)

    @JvmField
    val ARTIFACT: RegistrySupplier<LootItemFunctionType<ArtifactItemFunction?>> =
        LOOT_ITEM_FUNCTION_TYPES.register("artifact") {
            LootItemFunctionType(
                MapCodec.assumeMapUnsafe(
                    ArtifactSerializer()
                )
            )
        }

    fun register() {
        LOOT_ITEM_FUNCTION_TYPES.register()
    }
}
