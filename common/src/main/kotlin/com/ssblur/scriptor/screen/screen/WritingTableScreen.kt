package com.ssblur.scriptor.screen.screen

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.helpers.LimitedBookSerializer
import com.ssblur.scriptor.item.ScriptorItems
import com.ssblur.scriptor.item.ScriptorTags
import com.ssblur.scriptor.network.server.WritingTableNetwork
import com.ssblur.scriptor.screen.menu.WritingTableMenu
import com.ssblur.scriptor.screen.widget.TextField
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.resources.language.I18n
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.ItemStack
import kotlin.math.sign

class WritingTableScreen(menu: WritingTableMenu, val inventory: Inventory, component: Component):
  AbstractContainerScreen<WritingTableMenu>(
    menu,
    inventory,
    component
  ) {
    val textField: TextField
    init {
      imageWidth = 256
      imageHeight = 182
      textField = addRenderableWidget(TextField(leftPos + 15, topPos + 8, 140, 86, true))
    }

  companion object {
    private val TEXTURE = ScriptorMod.location("textures/gui/container/writing_desk.png")
  }

  var words = mutableListOf<List<String>>()
  private var lastBook: ItemStack = ItemStack.EMPTY
  private var lastDict: ItemStack = ItemStack.EMPTY
  private var click = false
  private var rightClick = false
  private var entriesOffset = 0
  private var dictionaryMode = false

  override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
    super.render(guiGraphics, i, j, f)
    renderTooltip(guiGraphics, i, j)

    if(menu.book != lastBook) {
      lastBook = menu.book
      var book = false
      lastBook[DataComponents.WRITTEN_BOOK_CONTENT]?.let { text ->
        textField.text = LimitedBookSerializer.decodeText(text)
        textField.editable = false
        book = true
      }
      lastBook[DataComponents.WRITABLE_BOOK_CONTENT]?.let { text ->
        textField.text = LimitedBookSerializer.decodeText(text)
        textField.editable = true
        book = true
      }
      if (!book) {
        textField.text = ""
        textField.editable = false
      }
    }
    if(menu.dictionary != lastDict) {
      lastDict = menu.dictionary
      entriesOffset = 0
      words = lastDict[ScriptorDataComponents.DICTIONARY_DATA]?.values?.toMutableList() ?: mutableListOf()
    }
    if(i > textField.x && j > textField.y && i < textField.x + textField.w && j < textField.y + textField.h) {
      val cursorPos = textField.getIndexAtPosition(font, i.toDouble() - textField.x + 9, j.toDouble() - textField.y).i
      val text = textField.text
      if (cursorPos < text.length && cursorPos > 0 && text[cursorPos] != ' ') {
        val components = mutableListOf<Component>()
        var wordStart = cursorPos
        while(wordStart > 0 && !text[wordStart].isWhitespace()) wordStart--
        var wordEnd = cursorPos
        while(wordEnd < text.length && !text[wordEnd].isWhitespace()) wordEnd++
        val word = text.substring(wordStart, wordEnd).trim(' ', '\n', '\t', '\r')
        components.add(Component.literal(word))
        val definition = words.firstOrNull { it.first() == word }?.get(1)
        if(menu.dictionary.`is`(ScriptorItems.DICTIONARY.get())) {
          components.add(definition?.let { Component.literal(it) } ?:
          Component.translatable("lore.scriptor.no_definition").withStyle(ChatFormatting.GRAY))
          if(definition == null && !textField.editable) {
            components.add(Component.translatable("lore.scriptor.click_to_add").withStyle(ChatFormatting.AQUA))
            if (click) {
              WritingTableNetwork.writeDictionaryEntry(WritingTableNetwork.DictionaryMessage(word, I18n.get("lore.scriptor.default_definition")))
              words.add(listOf(word, I18n.get("lore.scriptor.default_definition")))
            }
          }
        } else {
          components.add(Component.translatable("lore.scriptor.no_dictionary"))
        }
        guiGraphics.renderTooltip(font, components, lastBook.tooltipImage, i, j + (if(textField.editable) -20 else 0))
      }
    }
    textField.x = leftPos + 15
    textField.y = topPos + 8
    textField.font = font
    textField.render(guiGraphics, i, j, f)

    var y = topPos + 10
    val x = leftPos + 177
    val h = topPos + 96
    for(entry in words.slice(entriesOffset..<words.size)) {
      if(y + (2*font.lineHeight) > h) {
        guiGraphics.drawString(font, Component.translatable("extra.scriptor.scroll"), x, y, 0x777777)
        break
      }
      if(menu.dictionary.`is`(ScriptorItems.DICTIONARY.get())) {
        if(overEntries(i, j) && j - y in 1..font.lineHeight) {
          guiGraphics.drawString(font, entry[0], x, y, 0xffff99)
          val components = mutableListOf<Component>()
          components.add(Component.literal(entry[0]))
          components.add(Component.literal(entry[1]))
          if(lastBook.`is`(ScriptorTags.WRITABLE_SPELLBOOKS)) {
            components.add(Component.translatable("lore.scriptor.click_to_insert").withStyle(ChatFormatting.GRAY))
            if(click) {
              if(textField.cursor.i == 0)
                textField.text = entry[0] + " " + textField.text.trimStart()
              else if(textField.cursor.i >= textField.text.length - 1)
                textField.text = textField.text.trimEnd() + " " + entry[0]
              else {
                val c = textField.cursor.i
                textField.text = textField.text.substring(0, c).trimEnd() + " " + entry[0] + " " + textField.text.substring(c).trimStart()
              }
              WritingTableNetwork.write(textField.text, menu.pos, container = true)
            }
          }
          components.add(Component.translatable("lore.scriptor.right_click_to_edit").withStyle(ChatFormatting.GRAY))
          guiGraphics.renderTooltip(font, components, lastDict.tooltipImage, i, j + (if(textField.editable) -20 else 0))
        } else {
          guiGraphics.drawString(font, entry[0], x, y, 0xeeeeee)
        }
      }
      y += font.lineHeight
    }

    click = false
    rightClick = false
  }

  override fun mouseClicked(d: Double, e: Double, i: Int): Boolean {
    if(overText(d, e)) {
      textField.isFocused = true
      textField.mouseClicked(d, e, i)
      return true
    }
    textField.isFocused = false
    if(i == 0) click = true
    if(i == 1) rightClick = true
    return super.mouseClicked(d, e, i)
  }

  override fun mouseScrolled(d: Double, e: Double, f: Double, g: Double): Boolean {
    if(overText(d, e))
      textField.mouseScrolled(d, e, f, g)

    if(overEntries(d, e)) {
      entriesOffset -= g.sign.toInt()
      if(words.size >= 2)
        entriesOffset = entriesOffset.coerceIn(0, words.size - 1)
      else
        entriesOffset = 0
    }
    return super.mouseScrolled(d, e, f, g)
  }

  fun overEntries(x: Number, y: Number): Boolean {
    val offsetX = x.toInt() - leftPos
    val offsetY = y.toInt() - topPos
    return offsetX in 176..248 && offsetY in 9..97
  }

  fun overText(x: Number, y: Number): Boolean {
    return x.toInt() in textField.x..(textField.x + textField.w) && y.toInt() in textField.y..(textField.y + textField.h)
  }

  override fun renderBg(guiGraphics: GuiGraphics, f: Float, i: Int, j: Int) {
    val k = this.leftPos
    val l = this.topPos
    guiGraphics.blit(TEXTURE, k, l, 0, 0, this.imageWidth, this.imageHeight)
  }

  override fun renderLabels(guiGraphics: GuiGraphics, i: Int, j: Int) {}

  override fun onClose() {
    if(lastBook.`is`(ScriptorTags.WRITABLE_SPELLBOOKS)) {
      WritingTableNetwork.write(textField.text, menu.pos, container = true)
    }
    super.onClose()
  }

  override fun keyPressed(i: Int, j: Int, k: Int): Boolean {
    if(textField.isFocused)
      if(textField.keyPressed(i, j, k)) return true
    return super.keyPressed(i, j, k)
  }

  override fun keyReleased(i: Int, j: Int, k: Int): Boolean {
    if(textField.isFocused)
      if(textField.keyReleased(i, j, k)) return true
    return super.keyReleased(i, j, k)
  }

  override fun charTyped(c: Char, i: Int): Boolean {
    if(textField.isFocused)
      if(textField.charTyped(c, i)) return true
    return super.charTyped(c, i)
  }
}