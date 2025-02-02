package com.ssblur.scriptor.recipe

import com.ssblur.scriptor.ScriptorMod
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer

object ScriptorRecipes {
  val SPELLBOOK_CLONING = ScriptorMod.registerRecipeSerializer("spellbook_cloning") {
    SimpleCraftingRecipeSerializer { category -> SpellbookCloningRecipe(category) }
  }
  val SPELLBOOK = ScriptorMod.registerRecipeSerializer("spellbook") { SpellbookRecipe.Serializer() }
  val SPELLBOOK_DYEING = ScriptorMod.registerRecipeSerializer("spellbook_dyeing") { SpellbookDyeingRecipe.Serializer() }

  fun register() {}
}
