package com.ssblur.scriptor.recipe;

import com.ssblur.scriptor.item.ScriptorItems;
import com.ssblur.scriptor.item.Spellbook;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class SpellbookObfuscationRecipe extends CustomRecipe {
  public SpellbookObfuscationRecipe(ResourceLocation resourceLocation) {
    super(resourceLocation);
    System.out.println(1);
  }

  @Override
  public boolean matches(CraftingContainer container, Level level) {
    int glowInkCount = 0;
    int spellbookCount = 0;
    for(var slot = 0; slot < container.getContainerSize(); slot++) {
      if(!container.getItem(slot).isEmpty())
        if(container.getItem(slot).getItem() == Items.GLOW_INK_SAC)
          glowInkCount++;
        else if(container.getItem(slot).getItem() instanceof Spellbook) {
          var item = container.getItem(slot);
          var tag = item.getTag();
          if(tag != null && tag.getInt("generation") < 2)
            spellbookCount++;
        }
    }

    return spellbookCount == 1
        && glowInkCount == 1;
  }

  @Override
  public ItemStack assemble(CraftingContainer container) {
    ItemStack spellbook = ItemStack.EMPTY;
    for(int slot = 0; slot < container.getContainerSize(); slot++)
      if(container.getItem(slot).getItem() instanceof Spellbook)
        spellbook = container.getItem(slot);

    var copy = new ItemStack(ScriptorItems.OBFUSCATED_SPELLBOOK.get());
    var tag = spellbook.getTag();
    if(tag != null)
      copy.setTag(tag.copy());
    return copy;
  }

  @Override
  public boolean canCraftInDimensions(int i, int j) {
    return i * j >= 2;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ScriptorRecipes.SPELLBOOK_OBFUSCATION.get();
  }
}
