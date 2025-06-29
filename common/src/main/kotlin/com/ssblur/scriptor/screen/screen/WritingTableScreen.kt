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
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.resources.language.I18n
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Component.literal
import net.minecraft.network.chat.Component.translatable
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
    val searchField: TextField
    val dictionaryWordField: TextField
    val dictionaryDefinitionField: TextField
    var dictionarySaveButton: Button
    var dictionaryDeleteButton: Button
    var signButton: Button
    var finishSigningButton: Button
    var signingNameField: TextField
    init {
      imageWidth = 256
      imageHeight = 182
      textField = addRenderableWidget(TextField(leftPos + 15, topPos + 8, 140, 86, true))
      searchField = addRenderableWidget(
        TextField(leftPos + 178, topPos + 11, 72, 12, true, multiline = false, color = 0xffffff)
      )
      dictionaryWordField = TextField(leftPos, topPos, 90, 14, true, multiline = false)
      dictionaryDefinitionField = TextField(leftPos, topPos, 141, 26, true)
      dictionarySaveButton = Button.Builder(translatable("extra.scriptor.save")) {}.build()
      dictionaryDeleteButton = Button.Builder(translatable("extra.scriptor.delete")) {}.build()
      signButton = Button.Builder(translatable("extra.scriptor.sign")) {}.build()
      finishSigningButton = Button.Builder(translatable("extra.scriptor.sign")) {}.build()
      signingNameField = TextField(leftPos, topPos, 136, 14, true, multiline = false)
    }

  override fun init() {
    super.init()

    addRenderableWidget(searchField)
    searchField.x = leftPos + 178
    searchField.y = topPos + 11
    searchField.font = font

    if(editMode) {
      addRenderableWidget(textField)
      textField.x = leftPos + 15
      textField.y = topPos + 8
      textField.font = font
    } else if(dictionaryMode) {
      addRenderableWidget(dictionaryWordField)
      dictionaryWordField.x = leftPos + 17
      dictionaryWordField.y = topPos + 20
      dictionaryWordField.font = font

      addRenderableWidget(dictionaryDefinitionField)
      dictionaryDefinitionField.x = leftPos + 16
      dictionaryDefinitionField.y = topPos + 41
      dictionaryDefinitionField.font = font

      dictionarySaveButton = addRenderableWidget(
        Button.Builder(translatable("extra.scriptor.save")) { switchToEditMode() }
          .size(50, 17)
          .pos(leftPos + 110, topPos + 17)
          .build()
      )

      dictionaryDeleteButton = addRenderableWidget(
        Button.Builder(translatable("extra.scriptor.delete")) { switchToEditModeAndDelete() }
          .size(70, 17)
          .pos(leftPos + 88, topPos + 76)
          .build()
      )
    } else if(signMode) {
      finishSigningButton = addRenderableWidget(
        Button.Builder(translatable("extra.scriptor.sign")) {
          switchToEditMode()
          signBook()
        }
          .size(141, 19)
          .pos(leftPos + 17, topPos + 52)
          .build()
      )

      addRenderableWidget(signingNameField)
      signingNameField.x = leftPos + 21
      signingNameField.y = topPos + 33
      signingNameField.font = font
    }
    if(hasWritableBook())
      if(signMode)
        signButton = addRenderableWidget(Button.Builder(translatable("extra.scriptor.back")) {
          switchToEditMode()
        }.pos(leftPos + 200, topPos + 128).size(48, 18).build())
      else
        signButton = addRenderableWidget(Button.Builder(translatable("extra.scriptor.sign")) {
          switchToSignMode()
        }.pos(leftPos + 200, topPos + 128).size(48, 18).build())
  }

  companion object {
    private val TEXTURE = ScriptorMod.location("textures/gui/container/writing_desk.png")
    private val DICTIONARY_MODE_TEXTURE = ScriptorMod.location("textures/gui/container/writing_desk_dictionary.png")
    private val SIGN_MODE_TEXTURE = ScriptorMod.location("textures/gui/container/writing_desk_signing.png")
  }

  var words = mutableListOf<List<String>>()

  private var lastBook: ItemStack = ItemStack.EMPTY
  private var lastDict: ItemStack = ItemStack.EMPTY
  private var click = false
  private var rightClick = false
  private var entriesOffset = 0
  private var textLength = 0

  private var editMode = true
  private var dictionaryMode = false
  private var signMode = false

  override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
    super.render(guiGraphics, i, j, f)
    renderTooltip(guiGraphics, i, j)

    if(menu.book != lastBook) {
      if(menu.book.`is`(ScriptorItems.SCRAP.get())) {
        val word = menu.book[DataComponents.ITEM_NAME]!!.string
        if(words.none { it.get(0) == word }) {
          val parts = menu.book[ScriptorDataComponents.SPELL]!!.split(":".toRegex(), limit = 2).toTypedArray()
          val entry = parts[0] + ".scriptor." + parts[1]
          switchToDictionaryMode(word, I18n.get(entry))
        }
      } else {
        if (signMode) switchToEditMode()
        rebuildWidgets()
        lastBook = menu.book
        load()
      }
    } else if(textField.text.length != textLength && !overText(i, j)) {
      save()
    }

    if(menu.dictionary != lastDict) {
      lastDict = menu.dictionary
      entriesOffset = 0
      words = lastDict[ScriptorDataComponents.DICTIONARY_DATA]?.values?.toMutableList() ?: mutableListOf()
    }

    if(overText(i, j)) {
      val cursorPos = textField.getIndexAtPosition(font, i.toDouble() - textField.x, j.toDouble() - textField.y).i
      val text = textField.text
      if (cursorPos < text.length && cursorPos > 0 && text[cursorPos] != ' ') {
        val components = mutableListOf<Component>()

        val word = textField.currentWord(cursorPos)
        components.add(literal(word))

        val definition = words.firstOrNull { it.first() == word }?.get(1)
        if(hasDictionary()) {
          components.add(
            definition?.let { literal(it) } ?:
            translatable("lore.scriptor.no_definition").withStyle(ChatFormatting.GRAY)
          )
          if(definition == null && !textField.editable) {
            components.add(translatable("lore.scriptor.click_to_add").withStyle(ChatFormatting.AQUA))
            if (click) {
              switchToDictionaryMode(word, I18n.get("lore.scriptor.default_definition"))
              WritingTableNetwork.writeDictionaryEntry(WritingTableNetwork.DictionaryMessage(word, I18n.get("lore.scriptor.default_definition")))
              words.add(listOf(word, I18n.get("lore.scriptor.default_definition")))
            }
          }
        } else components.add(translatable("lore.scriptor.no_dictionary"))
        guiGraphics.renderTooltip(font, components, lastBook.tooltipImage, i, j + (if(textField.editable) -20 else 0))
      }
    }

    var y = topPos + 28
    val x = leftPos + 177
    val h = topPos + 96
    for(entry in words.slice(entriesOffset..<words.size)) {
      val search = searchField.text.lowercase()
      if(!entry[0].lowercase().contains(search) && !entry[1].lowercase().contains(search)) continue

      if(y + (2*font.lineHeight) > h) {
        guiGraphics.drawString(font, translatable("extra.scriptor.scroll"), x, y, 0x777777)
        break
      }
      if(menu.dictionary.`is`(ScriptorItems.DICTIONARY.get())) {
        if(overEntries(i, j) && j - y in 1..font.lineHeight) {
          guiGraphics.drawString(font, entry[0], x, y, 0xffff99)
          val components = mutableListOf<Component>()
          components.add(literal(entry[0]))
          components.add(literal(entry[1]))
          if(hasWritableBook() && editMode) {
            components.add(translatable("lore.scriptor.click_to_insert").withStyle(ChatFormatting.GRAY))
            if(click) {
              if(textField.cursor.i == 0)
                textField.text = entry[0] + " " + textField.text.trimStart()
              else if(textField.cursor.i >= textField.text.length - 1)
                textField.text = textField.text.trimEnd() + " " + entry[0]
              else {
                val c = textField.cursor.i
                textField.text = textField.text.substring(0, c).trimEnd() + " " + entry[0] + " " + textField.text.substring(c).trimStart()
              }
              textField.moveCursorToIndex(textField.cursor.i + 1 + entry[0].length)
              save()
            }
          }
          components.add(translatable("lore.scriptor.right_click_to_edit").withStyle(ChatFormatting.GRAY))
          if(rightClick) switchToDictionaryMode(entry[0], entry[1])
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

  private fun save() {
    WritingTableNetwork.write(textField.text, menu.pos, container = true)
    textLength = textField.text.length
  }

  private fun load(): Boolean {
    if(hasReadableBook())
      lastBook[DataComponents.WRITTEN_BOOK_CONTENT]?.let { text ->
        textField.text = LimitedBookSerializer.decodeText(text)
        textField.editable = false
        return true
      }
    if(hasWritableBook())
      lastBook[DataComponents.WRITABLE_BOOK_CONTENT]?.let { text ->
        textField.text = LimitedBookSerializer.decodeText(text)
        textField.editable = true
        return true
      }
    textField.text = ""
    textField.editable = false
    return false
  }

  private fun hasWritableBook() = menu.book.`is`(ScriptorTags.WRITABLE_SPELLBOOKS)
  private fun hasReadableBook() = menu.book.`is`(ScriptorTags.READABLE_SPELLBOOKS)
  private fun hasDictionary() = menu.dictionary.`is`(ScriptorItems.DICTIONARY.get())

  override fun mouseClicked(d: Double, e: Double, i: Int): Boolean {
    if(i == 0) click = true
    if(i == 1) rightClick = true
    if(!overText(d, e) && focused == textField) focused = null
    return super.mouseClicked(d, e, i)
  }

  override fun mouseScrolled(d: Double, e: Double, f: Double, g: Double): Boolean {
    if(overText(d, e))
      textField.mouseScrolled(d, e, f, g)

    if(overSearch(d, e))
      searchField.mouseScrolled(d, e, f, g)

    if(overEntries(d, e)) {
      entriesOffset -= g.sign.toInt()
      if(words.size >= 2)
        entriesOffset = entriesOffset.coerceIn(0, words.size - 1)
      else
        entriesOffset = 0
      return true
    }
    return super.mouseScrolled(d, e, f, g)
  }

  fun overEntries(x: Number, y: Number): Boolean {
    val offsetX = x.toInt() - leftPos
    val offsetY = y.toInt() - topPos
    return offsetX in 176..248 && offsetY in 28..97
  }

  fun overText(x: Number, y: Number): Boolean {
    if(!editMode) return false
    return x.toInt() in textField.x..(textField.x + textField.w) && y.toInt() in textField.y..(textField.y + textField.h)
  }

  fun overSearch(x: Number, y: Number): Boolean {
    return x.toInt() in searchField.x..(searchField.x + searchField.w) && y.toInt() in searchField.y..(searchField.y + searchField.h)
  }

  override fun renderBg(guiGraphics: GuiGraphics, f: Float, i: Int, j: Int) {
    val k = this.leftPos
    val l = this.topPos
    if(dictionaryMode)
      guiGraphics.blit(DICTIONARY_MODE_TEXTURE, k, l, 0, 0, this.imageWidth, this.imageHeight)
    else if(editMode)
      guiGraphics.blit(TEXTURE, k, l, 0, 0, this.imageWidth, this.imageHeight)
    else if(signMode)
      guiGraphics.blit(SIGN_MODE_TEXTURE, k, l, 0, 0, this.imageWidth, this.imageHeight)
  }

  override fun renderLabels(guiGraphics: GuiGraphics, i: Int, j: Int) {}

  override fun onClose() {
    if(hasWritableBook()) WritingTableNetwork.write(textField.text, menu.pos, container = true)
    super.onClose()
  }

  private fun switchToDictionaryMode(word: String, definition: String) {
    switchMode()
    dictionaryMode = true
    dictionaryWordField.text = word
    dictionaryDefinitionField.text = definition
    rebuildWidgets()
  }

  private fun switchMode() {
    if(dictionaryMode) {
      dictionaryMode = false
      WritingTableNetwork.writeDictionaryEntry(WritingTableNetwork.DictionaryMessage(dictionaryWordField.text, dictionaryDefinitionField.text))
    }
    if(editMode) {
      editMode = false
      save()
    }
    if(signMode) {
      signMode = false
    }
  }

  private fun switchToEditMode() {
    switchMode()
    editMode = true
    rebuildWidgets()
  }

  private fun switchToEditModeAndDelete() {
    dictionaryMode = false
    WritingTableNetwork.deleteDictionaryEntry(WritingTableNetwork.DictionaryDeleteMessage(dictionaryWordField.text.trim()))
    words = words.filter { it[0] != dictionaryWordField.text.trim() }.toMutableList()
    switchToEditMode()
  }

  private fun switchToSignMode() {
    switchMode()
    signingNameField.text = ""
    signMode = true
    rebuildWidgets()
  }

  private fun signBook() {
    WritingTableNetwork.signBook(WritingTableNetwork.SignBookMessage(if(signingNameField.text.isEmpty()) "Spell" else signingNameField.text))
  }
}