package com.ssblur.scriptor.integration.jei;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.helpers.SpellbookHelper;
import com.ssblur.scriptor.item.BookOfBooks;
import com.ssblur.scriptor.item.ObfuscatedSpellbook;
import com.ssblur.scriptor.item.ScriptorItems;
import com.ssblur.scriptor.item.ScriptorTags;
import com.ssblur.scriptor.recipe.SpellbookCloningRecipe;
import com.ssblur.scriptor.recipe.SpellbookDyeingRecipe;
import com.ssblur.scriptor.recipe.SpellbookRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;

import java.util.List;

@JeiPlugin
public class ScriptorJEIIntegration implements IModPlugin {
  public ScriptorJEIIntegration() {

  }

  @Override
  public ResourceLocation getPluginUid() {
    return ScriptorMod.location("jei_base");
  }

  @Override
  public void registerIngredients(IModIngredientRegistration registration) {
    IModPlugin.super.registerIngredients(registration);
  }

  @Override
  public void registerRecipes(IRecipeRegistration registration) {
    IModPlugin.super.registerRecipes(registration);

    var level = Minecraft.getInstance().level;
    assert level != null;
    var manager = level.getRecipeManager();

    manager.getAllRecipesFor(RecipeType.CRAFTING).forEach(holder -> {
      switch (holder.value()) {
        case SpellbookDyeingRecipe recipe -> registration.addRecipes(
          RecipeTypes.CRAFTING,
          List.of(
            new RecipeHolder<>(
              holder.id(),
              new ShapelessRecipe(
                holder.id().getPath(),
                recipe.category(),
                recipe.result,
                NonNullList.of(
                  Ingredient.of(ItemStack.EMPTY),
                  Ingredient.of(ScriptorTags.READABLE_SPELLBOOKS),
                  recipe.addition
                )
              )
            )
          )
        );
        case SpellbookCloningRecipe recipe -> registration.addRecipes(
          RecipeTypes.CRAFTING,
          SpellbookHelper.SPELLBOOKS.stream()
            .filter(book -> !(book instanceof ObfuscatedSpellbook || book instanceof BookOfBooks))
            .<RecipeHolder<CraftingRecipe>>map(spellbook ->
            new RecipeHolder<>(
              holder.id(),
              new ShapelessRecipe(
                holder.id().getPath(),
                recipe.category(),
                new ItemStack(spellbook),
                NonNullList.of(
                  Ingredient.of(ItemStack.EMPTY),
                  Ingredient.of(spellbook),
                  Ingredient.of(new ItemStack(Items.PAPER)),
                  Ingredient.of(new ItemStack(Items.PAPER)),
                  Ingredient.of(new ItemStack(Items.PAPER)),
                  Ingredient.of(new ItemStack(ScriptorItems.SPELLBOOK_BINDER))
                )
              )
            )
          ).toList()
        );
        case SpellbookRecipe recipe -> registration.addRecipes(
          RecipeTypes.CRAFTING,
          SpellbookHelper.SPELLBOOKS.stream()
            .filter(book -> !(book instanceof ObfuscatedSpellbook || book instanceof BookOfBooks))
            .<RecipeHolder<CraftingRecipe>>map(spellbook ->
              new RecipeHolder<>(
                holder.id(),
                new ShapelessRecipe(
                  holder.id().getPath(),
                  recipe.category(),
                  recipe.result,
                  NonNullList.of(
                    Ingredient.of(ItemStack.EMPTY),
                    recipe.base,
                    recipe.addition
                  )
                )
              )
          ).toList()
        );
        default -> {
        }
      }
    });
  }
}
