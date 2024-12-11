package com.ssblur.scriptor.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level

class SpellbookRecipe(var base: Ingredient, var addition: Ingredient, var result: ItemStack) : CustomRecipe(
    CraftingBookCategory.MISC
) {
    override fun matches(container: CraftingInput, level: Level): Boolean {
        return (container.items().stream().anyMatch { itemStack: ItemStack? -> base.test(itemStack) }
                && container.items().stream().anyMatch { itemStack: ItemStack? -> addition.test(itemStack) })
    }

    override fun assemble(container: CraftingInput, access: HolderLookup.Provider): ItemStack {
        var craftingBase: ItemStack? = null
        for (item in container.items()) if (base.test(item)) craftingBase = item

        if (craftingBase == null) return ItemStack.EMPTY

        val craftingResult = result.copy()
        craftingResult.applyComponents(craftingBase.components)
        return craftingResult
    }

    override fun canCraftInDimensions(i: Int, j: Int): Boolean {
        return i * j >= 2
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return ScriptorRecipes.SPELLBOOK.get()
    }

    class Serializer : RecipeSerializer<SpellbookRecipe> {
        override fun codec(): MapCodec<SpellbookRecipe> {
            return CODEC
        }

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, SpellbookRecipe> {
            return STREAM_CODEC
        }

        companion object {
            val CODEC: MapCodec<SpellbookRecipe> =
                RecordCodecBuilder.mapCodec { instance: RecordCodecBuilder.Instance<SpellbookRecipe> ->
                    instance.group(
                        Ingredient.CODEC_NONEMPTY.fieldOf("base").forGetter { recipe: SpellbookRecipe -> recipe.base },
                        Ingredient.CODEC_NONEMPTY.fieldOf("addition")
                            .forGetter { recipe: SpellbookRecipe -> recipe.addition },
                        ItemStack.CODEC.fieldOf("result").forGetter { recipe: SpellbookRecipe -> recipe.result }
                    )
                        .apply(instance) { base: Ingredient, addition: Ingredient, result: ItemStack ->
                            SpellbookRecipe(
                                base,
                                addition,
                                result
                            )
                        }
                }
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SpellbookRecipe> = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC,
                { recipe: SpellbookRecipe -> recipe.base },
                Ingredient.CONTENTS_STREAM_CODEC,
                { recipe: SpellbookRecipe -> recipe.addition },
                ItemStack.STREAM_CODEC,
                { recipe: SpellbookRecipe -> recipe.result },
                { base: Ingredient, addition: Ingredient, result: ItemStack -> SpellbookRecipe(base, addition, result) }
            )
        }
    }
}
