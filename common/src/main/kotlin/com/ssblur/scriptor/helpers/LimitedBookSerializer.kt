package com.ssblur.scriptor.helpers

import com.ssblur.scriptor.ScriptorMod.COMMUNITY_MODE
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.item.ScriptorItems.SPELLBOOK
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.network.Filterable
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.WritableBookContent
import net.minecraft.world.item.component.WrittenBookContent

object LimitedBookSerializer {
  fun decodeText(text: WrittenBookContent): String {
    val pages = text.getPages(true)
    val builder = StringBuilder()
    for (component in pages) {
      builder.append(component.string.trimEnd())
      builder.append(" ")
    }
    return builder.toString().trimEnd()
  }

  fun decodeText(text: WritableBookContent): String {
    val pages = text.getPages(true)
    val builder = StringBuilder()
    for (component in pages) {
      builder.append(component.trimEnd())
      builder.append(" ")
    }
    return builder.toString().trimEnd()
  }

  /**
   * A helper for encoding a String as a book-compatible JSON list.
   * @param text The text to encode.
   * @return An encoded JSON string
   */
  fun encodeText(text: String): List<Filterable<Component>> {
    val tokens = text.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    val list: MutableList<Filterable<Component>> = ArrayList()

    var pageLength = 0
    var page = StringBuilder()
    for (token in tokens) {
      if (token.length >= 96) {
        pageLength = 0
        list.add(filterable(page.toString()))
        list.add(filterable(token))
        page = StringBuilder()
      } else if ((token.length + page.length) >= 96) {
        pageLength = token.length
        list.add(filterable(page.toString()))
        page = StringBuilder()
        page.append(token)
        page.append(" ")
      } else {
        pageLength += token.length
        page.append(token)
        page.append(" ")
      }
    }
    if (!page.isEmpty()) list.add(filterable(page.toString()))

    return list
  }

  /**
   * A helper for encoding a String as a writable book-compatible JSON list.
   * @param text The text to encode.
   * @return An encoded JSON string
   */
  fun encodeTextWritable(text: String): List<Filterable<String>> {
    val tokens = text.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    val list: MutableList<Filterable<String>> = arrayListOf()

    var pageLength = 0
    var page = StringBuilder()
    for (token in tokens) {
      if (token.length >= 96) {
        pageLength = 0
        list.add(Filterable.passThrough(page.toString()))
        list.add(Filterable.passThrough(token))
        page = StringBuilder()
      } else if ((token.length + page.length) >= 96) {
        pageLength = token.length
        list.add(Filterable.passThrough(page.toString()))
        page = StringBuilder()
        page.append(token)
        page.append(" ")
      } else {
        pageLength += token.length
        page.append(token)
        page.append(" ")
      }
    }
    if (!page.isEmpty()) list.add(Filterable.passThrough(page.toString()))

    return list
  }

  fun createSpellbook(author: String?, title: String?, text: String, item: String?): ItemStack {
    val itemStack = if (item != null) ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse(item)))
    else ItemStack(SPELLBOOK.get())
    itemStack.count = 1
    itemStack.set(
      DataComponents.WRITTEN_BOOK_CONTENT,  // Supply "Spellbook" as title because some tome names surpass the written book title cap.
      WrittenBookContent(Filterable.passThrough("Spellbook"), author ?: "", 0, encodeText(text), false)
    )
    itemStack.set(ScriptorDataComponents.TOME_NAME, title)
    if (COMMUNITY_MODE) itemStack.set(ScriptorDataComponents.COMMUNITY_MODE, true)

    return itemStack
  }

  fun filterable(page: String): Filterable<Component> = Filterable.passThrough(Component.literal(page))
}
