package com.ssblur.scriptor.recipe;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ssblur.scriptor.data.DictionarySavedData;
import com.ssblur.scriptor.item.ScriptorItems;
import com.ssblur.scriptor.item.Spellbook;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpellbookRecipe extends CustomRecipe {
  public static Codec<SpellbookRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
    Codec.STRING.fieldOf("base").forGetter(data -> ScriptorItems.ITEMS.getRegistrar().getId(data.base).toString()),
    Codec.STRING.fieldOf("addition").forGetter(data -> ScriptorItems.ITEMS.getRegistrar().getId(data.addition).toString()),
    Codec.STRING.fieldOf("result").forGetter(data -> ScriptorItems.ITEMS.getRegistrar().getId(data.result).toString())
  ).apply(instance, SpellbookRecipe::new));

  Item base;
  Item addition;
  Item result;
  public SpellbookRecipe(CraftingBookCategory category, Item base, Item addition, Item result) {
    super(category);
    this.result = result;
    this.base = base;
    this.addition = addition;
  }

  public SpellbookRecipe(String base, String addition, String result) {
    super(CraftingBookCategory.MISC);
    this.result = ScriptorItems.ITEMS.getRegistrar().get(new ResourceLocation(result));
    this.base = ScriptorItems.ITEMS.getRegistrar().get(new ResourceLocation(base));
    this.addition = ScriptorItems.ITEMS.getRegistrar().get(new ResourceLocation(addition));

  }

  @Override
  public boolean matches(CraftingContainer container, Level level) {
    return
      container.hasAnyMatching(itemStack -> itemStack.getItem() == base)
      && container.hasAnyMatching(itemStack -> itemStack.getItem() == addition);
  }

  @Override
  public ItemStack assemble(CraftingContainer container, RegistryAccess access) {
    ItemStack craftingBase = null;
    for(var item: container.getItems())
      if(item.getItem() == base)
        craftingBase = item;

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
    return ScriptorRecipes.SPELLBOOK.get();
  }

  public static class Serializer implements RecipeSerializer<SpellbookRecipe> {

    public Serializer() {}

    public SpellbookRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
      CraftingBookCategory craftingBookCategory = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(jsonObject, "category", null), CraftingBookCategory.MISC);

      var result = ScriptorItems.ITEMS.getRegistrar().get(new ResourceLocation(jsonObject.get("result").getAsString()));
      var base = ScriptorItems.ITEMS.getRegistrar().get(new ResourceLocation(jsonObject.get("base").getAsString()));
      var addition = ScriptorItems.ITEMS.getRegistrar().get(new ResourceLocation(jsonObject.get("addition").getAsString()));

      assert result != null && base != null && addition != null;
      return new SpellbookRecipe(craftingBookCategory, base, addition, result);
    }

    @Override
    public Codec<SpellbookRecipe> codec() {
      return CODEC;
    }

    @Override
    public SpellbookRecipe fromNetwork(FriendlyByteBuf buf) {
      CraftingBookCategory craftingBookCategory = buf.readEnum(CraftingBookCategory.class);

      var nbt = buf.readNbt();
      assert nbt != null;
      var result = ScriptorItems.ITEMS.getRegistrar().get(new ResourceLocation(nbt.getString("r")));
      var base = ScriptorItems.ITEMS.getRegistrar().get(new ResourceLocation(nbt.getString("b")));
      var addition = ScriptorItems.ITEMS.getRegistrar().get(new ResourceLocation(nbt.getString("a")));

      assert result != null && base != null && addition != null;
      return new SpellbookRecipe(craftingBookCategory, base, addition, result);
    }

    public void toNetwork(FriendlyByteBuf buf, SpellbookRecipe craftingRecipe) {
      buf.writeEnum(craftingRecipe.category());

      var compound = new CompoundTag();
      compound.putString("r", Objects.requireNonNull(ScriptorItems.ITEMS.getRegistrar().getId(craftingRecipe.result)).toString());
      compound.putString("b", Objects.requireNonNull(ScriptorItems.ITEMS.getRegistrar().getId(craftingRecipe.base)).toString());
      compound.putString("a", Objects.requireNonNull(ScriptorItems.ITEMS.getRegistrar().getId(craftingRecipe.addition)).toString());

      buf.writeNbt(compound);
    }
  }
}
