package com.ssblur.scriptor.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ssblur.scriptor.item.ObfuscatedSpellbook;
import com.ssblur.scriptor.item.Spellbook;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class SpellbookDyeingRecipe extends CustomRecipe {
  Ingredient addition;
  ItemStack result;
  public SpellbookDyeingRecipe(Ingredient addition, ItemStack result) {
    super(CraftingBookCategory.MISC);
    this.result = result;
    this.addition = addition;
  }

  @Override
  public boolean matches(CraftingInput container, Level level) {
    return
      container.items().stream().anyMatch(itemStack ->
        itemStack.getItem() instanceof Spellbook
          && !(itemStack.getItem() instanceof ObfuscatedSpellbook)
      )
      &&  container.items().stream().anyMatch(itemStack -> addition.test(itemStack));
  }

  @Override
  public ItemStack assemble(CraftingInput container, HolderLookup.Provider access) {
    ItemStack craftingBase = null;
    for(var item: container.items())
      if(item.getItem() instanceof Spellbook
        && !(item.getItem() instanceof ObfuscatedSpellbook)) {
        if (craftingBase == null)
          craftingBase = item;
        else
          return ItemStack.EMPTY;
      }

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
    return ScriptorRecipes.SPELLBOOK_DYEING.get();
  }

  public static class Serializer implements RecipeSerializer<SpellbookDyeingRecipe> {
    public static final MapCodec<SpellbookDyeingRecipe> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
          Ingredient.CODEC_NONEMPTY.fieldOf("addition").forGetter(recipe -> recipe.addition),
          ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
        )
        .apply(instance, SpellbookDyeingRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, SpellbookDyeingRecipe> STREAM_CODEC =  StreamCodec.composite(
      Ingredient.CONTENTS_STREAM_CODEC,
      recipe -> recipe.addition,
      ItemStack.STREAM_CODEC,
      recipe -> recipe.result,
      SpellbookDyeingRecipe::new
    );

    public Serializer() {}


    @Override
    public @NotNull MapCodec<SpellbookDyeingRecipe> codec() {
      return CODEC;
    }

    @Override
    public @NotNull StreamCodec<RegistryFriendlyByteBuf, SpellbookDyeingRecipe> streamCodec() {
      return STREAM_CODEC;
    }
  }
}
