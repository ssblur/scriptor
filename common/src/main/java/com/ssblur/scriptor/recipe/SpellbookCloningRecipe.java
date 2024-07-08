package com.ssblur.scriptor.recipe;

import com.ssblur.scriptor.item.ScriptorItems;
import com.ssblur.scriptor.item.Spellbook;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class SpellbookCloningRecipe extends CustomRecipe {
  public SpellbookCloningRecipe(ResourceLocation resourceLocation, CraftingBookCategory category) {
    super(resourceLocation, category);
  }

  @Override
  public boolean matches(CraftingContainer container, Level level) {
    int paperCount = 0;
    int binderCount = 0;
    int spellbookCount = 0;
    for(var slot = 0; slot < container.getContainerSize(); slot++) {
      if(!container.getItem(slot).isEmpty())
        if(container.getItem(slot).getItem() == Items.PAPER)
          paperCount++;
        else if(container.getItem(slot).getItem() instanceof Spellbook) {
          var item = container.getItem(slot);
          var tag = item.getTag();
          if(tag != null && tag.getInt("generation") < 2)
            spellbookCount++;
        } else if(container.getItem(slot).getItem() == ScriptorItems.SPELLBOOK_BINDER.get())
          binderCount++;
    }

    return spellbookCount == 1
        && binderCount == 1
        && paperCount == 3;
  }

  @Override
  public ItemStack assemble(CraftingContainer container, RegistryAccess access) {
    ItemStack spellbook = ItemStack.EMPTY;
    for(int slot = 0; slot < container.getContainerSize(); slot++)
      if(container.getItem(slot).getItem() instanceof Spellbook)
        spellbook = container.getItem(slot);
    var copy = spellbook.copy();
    var tag = copy.getTag();
    if(tag != null) {
      tag.putInt("generation", tag.getInt("generation") + 1);
    }

    return copy;
  }

  @Override
  public boolean canCraftInDimensions(int i, int j) {
    return i * j >= 6;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ScriptorRecipes.SPELLBOOK_CLONING.get();
  }

  @Override
  public NonNullList<ItemStack> getRemainingItems(CraftingContainer craftingContainer) {
    NonNullList<ItemStack> nonNullList = NonNullList.withSize(craftingContainer.getContainerSize(), ItemStack.EMPTY);
    for (int i = 0; i < nonNullList.size(); i++) {
      ItemStack itemStack = craftingContainer.getItem(i);
      if (itemStack.getItem().hasCraftingRemainingItem()) {
        var remainingItem = itemStack.getItem().getCraftingRemainingItem();
        if(remainingItem != null)
          nonNullList.set(i, new ItemStack(remainingItem));
        continue;
      }

      if (itemStack.getItem() instanceof Spellbook) {
        ItemStack itemStack2 = itemStack.copy();
        itemStack2.setCount(itemStack.getCount());
        nonNullList.set(i, itemStack2);
      }
    }
    return nonNullList;
  }
}
