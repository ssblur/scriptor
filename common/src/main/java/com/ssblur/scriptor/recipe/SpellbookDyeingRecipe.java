package com.ssblur.scriptor.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ssblur.scriptor.item.ObfuscatedSpellbook;
import com.ssblur.scriptor.item.ScriptorItems;
import com.ssblur.scriptor.item.Spellbook;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class SpellbookDyeingRecipe extends CustomRecipe {
  public static Codec<SpellbookDyeingRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
    Codec.STRING.fieldOf("addition").forGetter(data -> ScriptorItems.ITEMS.getRegistrar().getId(data.addition).toString()),
    Codec.STRING.fieldOf("result").forGetter(data -> ScriptorItems.ITEMS.getRegistrar().getId(data.result).toString())
  ).apply(instance, SpellbookDyeingRecipe::new));

  Item addition;
  Item result;
  public SpellbookDyeingRecipe(Item addition, Item result) {
    super(CraftingBookCategory.MISC);
    this.result = result;
    this.addition = addition;
  }

  public SpellbookDyeingRecipe(String addition, String result) {
    super(CraftingBookCategory.MISC);
    this.result = ScriptorItems.ITEMS.getRegistrar().get(new ResourceLocation(result));
    this.addition = ScriptorItems.ITEMS.getRegistrar().get(new ResourceLocation(addition));

  }

  @Override
  public boolean matches(CraftingContainer container, Level level) {
    return
      container.hasAnyMatching(itemStack ->
        itemStack.getItem() instanceof Spellbook
          && !(itemStack.getItem() instanceof ObfuscatedSpellbook)
      )
      && container.hasAnyMatching(itemStack -> itemStack.getItem() == addition);
  }

  @Override
  public ItemStack assemble(CraftingContainer container, RegistryAccess access) {
    ItemStack craftingBase = null;
    for(var item: container.getItems())
      if(item.getItem() instanceof Spellbook
        && !(item.getItem() instanceof ObfuscatedSpellbook)) {
        if (craftingBase == null)
          craftingBase = item;
        else
          return ItemStack.EMPTY;
      }

    if(craftingBase == null) return ItemStack.EMPTY;

    ItemStack craftingResult = new ItemStack(result);
    craftingResult.setTag(craftingBase.getTag());
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

    public Serializer() {}

    public SpellbookDyeingRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
      var result = ScriptorItems.ITEMS.getRegistrar().get(new ResourceLocation(jsonObject.get("result").getAsString()));
      var addition = ScriptorItems.ITEMS.getRegistrar().get(new ResourceLocation(jsonObject.get("addition").getAsString()));

      assert result != null && addition != null;
      return new SpellbookDyeingRecipe(addition, result);
    }

    @Override
    public Codec<SpellbookDyeingRecipe> codec() {
      return CODEC;
    }

    @Override
    public SpellbookDyeingRecipe fromNetwork(FriendlyByteBuf buf) {
      var nbt = buf.readNbt();
      assert nbt != null;
      var result = ScriptorItems.ITEMS.getRegistrar().get(new ResourceLocation(nbt.getString("r")));
      var base = ScriptorItems.ITEMS.getRegistrar().get(new ResourceLocation(nbt.getString("b")));
      var addition = ScriptorItems.ITEMS.getRegistrar().get(new ResourceLocation(nbt.getString("a")));

      assert result != null && base != null && addition != null;
      return new SpellbookDyeingRecipe(addition, result);
    }

    public void toNetwork(FriendlyByteBuf buf, SpellbookDyeingRecipe craftingRecipe) {
      var compound = new CompoundTag();
      compound.putString("r", Objects.requireNonNull(ScriptorItems.ITEMS.getRegistrar().getId(craftingRecipe.result)).toString());
      compound.putString("a", Objects.requireNonNull(ScriptorItems.ITEMS.getRegistrar().getId(craftingRecipe.addition)).toString());

      buf.writeNbt(compound);
    }
  }
}
