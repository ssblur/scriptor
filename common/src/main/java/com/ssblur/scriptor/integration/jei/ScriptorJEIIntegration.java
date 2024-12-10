package com.ssblur.scriptor.integration.jei;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.integration.recipes.RecipeIntegration;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.List;

@JeiPlugin
public class ScriptorJEIIntegration implements IModPlugin {
  public ScriptorJEIIntegration() {

  }

  @Override
  public ResourceLocation getPluginUid() {
    return ScriptorMod.INSTANCE.location("jei_base");
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

    RecipeIntegration.registerItemInfo((location, itemStacks, components) -> {
      registration.addItemStackInfo(
        itemStacks,
        components
      );
    });

    RecipeIntegration.registerRecipes(() -> manager.getAllRecipesFor(RecipeType.CRAFTING), ((ingredients, result, id) -> {
      var list = NonNullList.of(
        Ingredient.of(ItemStack.EMPTY)
      );
      list.addAll(ingredients);

      registration.addRecipes(
        RecipeTypes.CRAFTING,
        List.of(
          new RecipeHolder<>(
            id,
            new ShapelessRecipe(
              id.getPath(),
              CraftingBookCategory.MISC,
              result,
              list
            )
          )
        )
      );
    }));
  }
}
