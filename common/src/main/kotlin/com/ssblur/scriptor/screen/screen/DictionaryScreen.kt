package com.ssblur.scriptor.screen.screen

import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.screen.menu.DictionaryMenu
import com.ssblur.unfocused.screen.UnfocusedScreen
import com.ssblur.unfocused.screen.renderable.BlackBox
import com.ssblur.unfocused.screen.renderable.SinglePageBackground
import com.ssblur.unfocused.screen.widget.MarkdownWidget
import com.ssblur.unfocused.screen.widget.TextEntryWidget
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class DictionaryScreen(abstractContainerMenu: DictionaryMenu, inventory: Inventory, component: Component) :
  UnfocusedScreen<DictionaryMenu>(abstractContainerMenu, inventory, component) {
  enum class SUBSCREENS {
    MAIN,
    ENTRIES,
    OBSERVED_SPELLS,
  }
  var subscreen = SUBSCREENS.MAIN
  var searchTerm = ""
  var bookMemory = abstractContainerMenu.dictionary.hashCode()

  override fun init() {
    val x = (this.width - 265) / 2
    val y = (this.height - 220) / 2
    add(SinglePageBackground(x, y, 265, 220))

    when(subscreen) {
      SUBSCREENS.MAIN -> {
        subscreen = SUBSCREENS.ENTRIES
      }
      SUBSCREENS.ENTRIES -> {
        add(BlackBox(x + 20, y + 20, 225, 12)) // TODO replace with actual search box bg
        val contents = add(MarkdownWidget(
          x + 20,
          y + 34,
          225,
          152,
          entryMarkdown(),
          false,
          commandsAllowed = false
        ))
        contents.setColor(0, 0, 0)

        val entry = addRenderableWidget(
          TextEntryWidget(x + 22, y + 22, 221, 12, true)
        )
        entry.color = 0xffffffffu
        entry.cursorColor = 0xffddddddu
        entry.text = searchTerm
        entry.onTextUpdate = { element, text ->
          searchTerm = text
          contents.markdownText = entryMarkdown()
          contents.scroll = 0.0
        }
      }
      SUBSCREENS.OBSERVED_SPELLS -> {

      }
    }
  }

  override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
    if(menu.dictionary.hashCode() != bookMemory) {
      bookMemory = menu.dictionary.hashCode()
      rebuildWidgets()
    }
    super.render(guiGraphics, i, j, f)
  }

  fun entryMarkdown() = (menu.dictionary[ScriptorDataComponents.DICTIONARY_DATA]?.values ?: listOf()).filter {
      it.any { w -> w.lowercase().contains(searchTerm.lowercase()) }
    }.joinToString(separator = "\n\n") {
      "**${it[0]}**: ${it[1]}"
    }.trimEnd()
}