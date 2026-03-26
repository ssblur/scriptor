package com.ssblur.scriptor.integration.emi;

import com.ssblur.scriptor.integration.recipes.RecipeIntegration;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Arrays;

@EmiEntrypoint
public class ScriptorEMIIntegration implements EmiPlugin {
  @Override
  public void register(EmiRegistry registry) {
    RecipeIntegration.registerItemInfo(((location, items, components) -> registry.addRecipe(new EmiInfoRecipe(
      items.stream().map(item -> EmiIngredient.of(Ingredient.of(item))).toList(),
      Arrays.stream(components).toList(),
      location
    ))));

    var manager = registry.getRecipeManager();
    RecipeIntegration.registerRecipes(() -> manager.getAllRecipesFor(RecipeType.CRAFTING), ((ingredients, result, id) -> registry.addRecipe(
      new EmiCraftingRecipe(
        ingredients.stream().map(EmiIngredient::of).toList(),
        EmiStack.of(result),
        id
      )
    )));
  }
}
