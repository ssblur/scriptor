package com.ssblur.scriptor.recipe;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.word.Spell;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.world.item.crafting.ArmorDyeRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class ScriptorRecipes {
  public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ScriptorMod.MOD_ID, Registries.RECIPE_SERIALIZER);

  public static final RegistrySupplier<RecipeSerializer<?>> SPELLBOOK_CLONING = RECIPES.register("spellbook_cloning",
    () -> new SimpleCraftingRecipeSerializer<>(SpellbookCloningRecipe::new));
  public static final RegistrySupplier<RecipeSerializer<?>> SPELLBOOK_OBFUSCATION = RECIPES.register("spellbook_obfuscation",
    () -> new SimpleCraftingRecipeSerializer<>(SpellbookObfuscationRecipe::new));

  public static void register() { RECIPES.register(); }

}
