package com.ssblur.scriptor.recipe

import com.ssblur.scriptor.ScriptorMod
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer

object ScriptorRecipes {
    val RECIPES: DeferredRegister<RecipeSerializer<*>> =
        DeferredRegister.create(ScriptorMod.MOD_ID, Registries.RECIPE_SERIALIZER)

    @JvmField
    val SPELLBOOK_CLONING: RegistrySupplier<RecipeSerializer<*>> = RECIPES.register(
        "spellbook_cloning"
    ) { SimpleCraftingRecipeSerializer { category: CraftingBookCategory? -> SpellbookCloningRecipe(category) } }

    @JvmField
    val SPELLBOOK: RegistrySupplier<RecipeSerializer<*>> = RECIPES.register(
        "spellbook"
    ) { SpellbookRecipe.Serializer() }

    @JvmField
    val SPELLBOOK_DYEING: RegistrySupplier<RecipeSerializer<*>> = RECIPES.register(
        "spellbook_dyeing"
    ) { SpellbookDyeingRecipe.Serializer() }

    fun register() {
        RECIPES.register()
    }
}
