package com.ssblur.scriptor.resources

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.unfocused.data.DataLoaderRegistry.registerSimpleDataLoader
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

object CastRecipes {
  data class CastRecipe(
    val action: String,
    val base: Ingredient,
    val result: ItemStack,
  ) // TODO: register cast recipes once Ingredients / ItemStack serde is in

  val recipes = ScriptorMod.registerSimpleDataLoader("scriptor/cast_recipes", CastRecipe::class)
}