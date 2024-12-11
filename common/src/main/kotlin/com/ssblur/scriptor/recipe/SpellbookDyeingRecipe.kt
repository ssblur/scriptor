package com.ssblur.scriptor.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.ssblur.scriptor.item.books.ObfuscatedSpellbook
import com.ssblur.scriptor.item.books.Spellbook
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level

class SpellbookDyeingRecipe(var addition: Ingredient, var result: ItemStack) : CustomRecipe(CraftingBookCategory.MISC) {
    override fun matches(container: CraftingInput, level: Level): Boolean {
        return (container.items().stream().anyMatch { itemStack: ItemStack ->
            itemStack.item is Spellbook
                    && itemStack.item !is ObfuscatedSpellbook
        }
                && container.items().stream().anyMatch { itemStack: ItemStack? -> addition.test(itemStack) })
    }

    override fun assemble(container: CraftingInput, access: HolderLookup.Provider): ItemStack {
        var craftingBase: ItemStack? = null
        for (item in container.items()) if (item.item is Spellbook
            && item.item !is ObfuscatedSpellbook
        ) {
            if (craftingBase == null) craftingBase = item
            else return ItemStack.EMPTY
        }

        if (craftingBase == null) return ItemStack.EMPTY

        val craftingResult = result.copy()
        craftingResult.applyComponents(craftingBase.components)
        return craftingResult
    }

    override fun canCraftInDimensions(i: Int, j: Int): Boolean {
        return i * j >= 2
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return ScriptorRecipes.SPELLBOOK_DYEING.get()
    }

    class Serializer : RecipeSerializer<SpellbookDyeingRecipe> {
        override fun codec(): MapCodec<SpellbookDyeingRecipe> {
            return CODEC
        }

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, SpellbookDyeingRecipe> {
            return STREAM_CODEC
        }

        companion object {
            val CODEC: MapCodec<SpellbookDyeingRecipe> =
                RecordCodecBuilder.mapCodec { instance: RecordCodecBuilder.Instance<SpellbookDyeingRecipe> ->
                    instance.group(
                        Ingredient.CODEC_NONEMPTY.fieldOf("addition")
                            .forGetter { recipe: SpellbookDyeingRecipe -> recipe.addition },
                        ItemStack.CODEC.fieldOf("result").forGetter { recipe: SpellbookDyeingRecipe -> recipe.result }
                    )
                        .apply(instance) { addition: Ingredient, result: ItemStack ->
                            SpellbookDyeingRecipe(
                                addition,
                                result
                            )
                        }
                }
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SpellbookDyeingRecipe> = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC,
                { recipe: SpellbookDyeingRecipe -> recipe.addition },
                ItemStack.STREAM_CODEC,
                { recipe: SpellbookDyeingRecipe -> recipe.result },
                { addition: Ingredient, result: ItemStack -> SpellbookDyeingRecipe(addition, result) }
            )
        }
    }
}
