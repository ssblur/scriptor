package com.ssblur.scriptor.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class SpellbookRecipe extends CustomRecipe {
  Ingredient base;
  Ingredient addition;
  ItemStack result;
  public SpellbookRecipe(Ingredient base, Ingredient addition, ItemStack result) {
    super(CraftingBookCategory.MISC);
    this.result = result;
    this.base = base;
    this.addition = addition;
  }

  @Override
  public boolean matches(CraftingInput container, Level level) {
    return
      container.items().stream().anyMatch(itemStack -> base.test(itemStack))
      && container.items().stream().anyMatch(itemStack -> addition.test(itemStack));
  }

  @Override
  public ItemStack assemble(CraftingInput container, HolderLookup.Provider access) {
    ItemStack craftingBase = null;
    for(var item: container.items())
      if(base.test(item))
        craftingBase = item;

    if(craftingBase == null) return ItemStack.EMPTY;

    ItemStack craftingResult = result.copy();
    craftingResult.applyComponents(craftingBase.getComponents());
    return craftingResult;
  }

  @Override
  public boolean canCraftInDimensions(int i, int j) {
    return i * j >= 2;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ScriptorRecipes.SPELLBOOK.get();
  }

  public static class Serializer implements RecipeSerializer<SpellbookRecipe> {
    public static final MapCodec<SpellbookRecipe> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
          Ingredient.CODEC_NONEMPTY.fieldOf("base").forGetter(recipe -> recipe.base),
          Ingredient.CODEC_NONEMPTY.fieldOf("addition").forGetter(recipe -> recipe.addition),
          ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
        )
        .apply(instance, SpellbookRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, SpellbookRecipe> STREAM_CODEC =  StreamCodec.composite(
      Ingredient.CONTENTS_STREAM_CODEC,
      recipe -> recipe.base,
      Ingredient.CONTENTS_STREAM_CODEC,
      recipe -> recipe.addition,
      ItemStack.STREAM_CODEC,
      recipe -> recipe.result,
      SpellbookRecipe::new
    );

    public Serializer() {}


    @Override
    public @NotNull MapCodec<SpellbookRecipe> codec() {
      return CODEC;
    }

    @Override
    public @NotNull StreamCodec<RegistryFriendlyByteBuf, SpellbookRecipe> streamCodec() {
      return STREAM_CODEC;
    }
  }
}
