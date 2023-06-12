package com.ssblur.scriptor.recipe;

import com.google.gson.JsonObject;
import com.ssblur.scriptor.ScriptorMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.*;

public class ScriptorRecipes {
  public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ScriptorMod.MOD_ID, Registries.RECIPE_SERIALIZER);

  public static final RegistrySupplier<RecipeSerializer<?>> SPELLBOOK_CLONING = RECIPES.register("spellbook_cloning",
    () -> new SimpleCraftingRecipeSerializer<>(SpellbookCloningRecipe::new));
  public static final RegistrySupplier<RecipeSerializer<?>> SPELLBOOK_OBFUSCATION = RECIPES.register("spellbook_obfuscation",
    () -> new SimpleCraftingRecipeSerializer<>(SpellbookObfuscationRecipe::new));

  public static final RegistrySupplier<RecipeSerializer<?>> SPELLBOOK = RECIPES.register("spellbook",
    SpellbookRecipe.Serializer::new);

  public static void register() { RECIPES.register(); }
}
