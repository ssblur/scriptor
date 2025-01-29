package com.ssblur.scriptor.recipe

import com.ssblur.scriptor.item.ScriptorItems.SPELLBOOK_BINDER
import com.ssblur.scriptor.item.books.Spellbook
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.WrittenBookContent
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.CustomRecipe
import net.minecraft.world.level.Level

class SpellbookCloningRecipe(category: CraftingBookCategory) : CustomRecipe(category) {
    override fun matches(container: CraftingInput, level: Level): Boolean {
        var paperCount = 0
        var binderCount = 0
        var spellbookCount = 0
        for (slot in 0 until container.size()) {
            if (!container.getItem(slot).isEmpty) if (container.getItem(slot).item === Items.PAPER) paperCount++
            else if (container.getItem(slot).item is Spellbook) {
                val item = container.getItem(slot)
                val book = item.get(DataComponents.WRITTEN_BOOK_CONTENT)
                if (book != null && book.generation() < 2) spellbookCount++
            } else if (container.getItem(slot).item === SPELLBOOK_BINDER.get()) binderCount++
        }
        return spellbookCount == 1 && binderCount == 1 && paperCount == 3
    }

    override fun assemble(container: CraftingInput, access: HolderLookup.Provider): ItemStack {
        var spellbook = ItemStack.EMPTY
        for (slot in 0 until container.size())
            if (container.getItem(slot).item is Spellbook) spellbook = container.getItem(slot)
        val copy = spellbook.copy()
        val book = copy.get(DataComponents.WRITTEN_BOOK_CONTENT)
        copy.set(
            DataComponents.WRITTEN_BOOK_CONTENT,
            WrittenBookContent(book!!.title(), book.author(), book.generation(), book.pages(), book.resolved())
        )
        return copy
    }

    override fun canCraftInDimensions(i: Int, j: Int) = i * j >= 6
    override fun getSerializer() = ScriptorRecipes.SPELLBOOK_CLONING.get()

    override fun getRemainingItems(craftingContainer: CraftingInput): NonNullList<ItemStack> {
        val nonNullList = NonNullList.withSize(craftingContainer.size(), ItemStack.EMPTY)
        for (i in nonNullList.indices) {
            val itemStack = craftingContainer.getItem(i)
            if (itemStack.item.hasCraftingRemainingItem()) {
                val remainingItem = itemStack.item.craftingRemainingItem
                if (remainingItem != null) nonNullList[i] = ItemStack(remainingItem)
                continue
            }

            if (itemStack.item is Spellbook) {
                val itemStack2 = itemStack.copy()
                itemStack2.count = itemStack.count
                nonNullList[i] = itemStack2
            }
        }
        return nonNullList
    }
}
