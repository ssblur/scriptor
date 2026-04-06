package com.ssblur.scriptor.screen.screen

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.helpers.ScriptionaryHelper
import com.ssblur.scriptor.screen.menu.DictionaryMenu
import com.ssblur.unfocused.helper.LocalizedMarkdownReader
import com.ssblur.unfocused.screen.UnfocusedBookScreen
import com.ssblur.unfocused.screen.UnfocusedScreen
import com.ssblur.unfocused.screen.renderable.BlackBox
import com.ssblur.unfocused.screen.renderable.SinglePageBackground
import com.ssblur.unfocused.screen.widget.ButtonWidget
import com.ssblur.unfocused.screen.widget.MarkdownWidget
import com.ssblur.unfocused.screen.widget.PlainTextWidget
import com.ssblur.unfocused.screen.widget.TextEntryWidget
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import java.io.FileNotFoundException

class DictionaryScreen(abstractContainerMenu: DictionaryMenu, inventory: Inventory, component: Component) :
  UnfocusedScreen<DictionaryMenu>(abstractContainerMenu, inventory, component) {
  enum class SUBSCREENS {
    MAIN,
    ENTRIES,
    OBSERVED_SPELLS,
    GUIDE,
  }

  var subscreen = SUBSCREENS.MAIN
//  var guideCategory: String? = null
  var searchTerm = ""
  var bookMemory = abstractContainerMenu.dictionary.hashCode()

  override fun init() {
    UnfocusedBookScreen.backAction = {
      Minecraft.getInstance().screen = this@DictionaryScreen
    }

    imageWidth = 265
    imageHeight = 220
    leftPos = (this.width - 265) / 2
    topPos = (this.height - 220) / 2
    add(SinglePageBackground(leftPos, topPos, imageWidth, imageHeight))

    when (subscreen) {
      SUBSCREENS.MAIN -> {
        var wy = topPos + 20
        add(
          PlainTextWidget(
            Component.translatable("extra.scriptor.scriptionary_title").withStyle(ChatFormatting.BOLD),
            leftPos + 20,
            wy,
            225,
            12
          )
        )

        wy += 12
        add(
          PlainTextWidget(
            Component.translatable("extra.scriptor.scriptionary_version").withStyle(ChatFormatting.ITALIC),
            leftPos + 20,
            wy,
            225,
            12
          )
        )

        wy += 12
        add(
          PlainTextWidget(
            Component.translatable("extra.scriptor.scriptionary_blurb").withStyle(ChatFormatting.ITALIC),
            leftPos + 20,
            wy,
            225,
            48
          )
        )

        wy += 64
        add(
          ButtonWidget(leftPos + 20, wy, 225, 24, Component.translatable("extra.scriptor.entries")) {
            subscreen = SUBSCREENS.ENTRIES
            rebuildWidgets()
          }
        )

        wy += 28
        add(
          ButtonWidget(leftPos + 20, wy, 225, 24, Component.translatable("extra.scriptor.guide")) {
            subscreen = SUBSCREENS.GUIDE
            rebuildWidgets()
          }
        )

        wy += 28
        add(
          ButtonWidget(leftPos + 20, wy, 225, 24, Component.translatable("extra.scriptor.observed_spells")) {
            subscreen = SUBSCREENS.OBSERVED_SPELLS
            rebuildWidgets()
          }
        )
      }

      SUBSCREENS.ENTRIES -> {
        add(BlackBox(leftPos + 20, topPos + 20, 225, 12))
        val contents = add(
          MarkdownWidget(
            leftPos + 20,
            topPos + 34,
            225,
            128,
            entryMarkdown(),
            false,
            commandsAllowed = false
          )
        )
        contents.setColor(0, 0, 0)

        val entry = add(TextEntryWidget(leftPos + 22, topPos + 22, 221, 12, true))
        entry.color = 0xffffffffu
        entry.cursorColor = 0xffddddddu
        entry.placeholderColor = 0xffaaaaaau
        entry.placeholder = "Search..."
        entry.text = searchTerm
        entry.onTextUpdate = { _, text ->
          searchTerm = text
          contents.markdownText = entryMarkdown()
          contents.scroll = 0.0
        }

        add(
          ButtonWidget(leftPos + 20, topPos + 170, 225, 24, Component.translatable("extra.scriptor.back")) {
            subscreen = SUBSCREENS.MAIN
            rebuildWidgets()
          }
        )
      }

      SUBSCREENS.OBSERVED_SPELLS -> {
        val contents = add(
          PlainTextWidget(
            observationsComponent(),
            leftPos + 20,
            topPos + 20,
            225,
            138,
            true,
          )
        )
        contents.color = 0xff000000u

        add(
          ButtonWidget(leftPos + 20, topPos + 170, 225, 24, Component.translatable("extra.scriptor.back")) {
            subscreen = SUBSCREENS.MAIN
            rebuildWidgets()
          }
        )
      }

      SUBSCREENS.GUIDE -> {
        addEntriesWidget()
        add(
          ButtonWidget(leftPos + 20, topPos + 170, 225, 24, Component.translatable("extra.scriptor.back")) {
            subscreen = SUBSCREENS.MAIN
            rebuildWidgets()
          }
        )
      }
    }
  }

  override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
    if (menu.dictionary.hashCode() != bookMemory) {
      bookMemory = menu.dictionary.hashCode()
      rebuildWidgets()
    }
    super.render(guiGraphics, i, j, f)
  }

  fun entryMarkdown() = try {
      (menu.dictionary[ScriptorDataComponents.DICTIONARY_DATA]?.values ?: listOf()).filter {
        it.any { w -> w.lowercase().contains(searchTerm.lowercase()) }
      }.joinToString(separator = "\n\n") {
        "**${it[0]}**: ${it[1]}"
      }.trimEnd()
    } catch (_: FileNotFoundException) {
      "Unable to load dictionary entry. Are you using a resource pack that overwrites scriptor files?"
    }

  fun observationsComponent(): Component = ScriptionaryHelper.PLAYER_OBSERVATIONS.map {
    Component.literal("<").append(it.second).append("> ").append(it.first).append("\n\n")
  }.let {
    val component = Component.empty()
    it.forEach { c -> component.append(c) }
    if(component.string.trim().isEmpty())
      Component.translatable("extra.scriptor.empty")
        .withStyle(ChatFormatting.GRAY)
        .withStyle(ChatFormatting.ITALIC)
    else component
  }

  fun addEntriesWidget() {
    val categories = mutableMapOf<String, MutableList<String>>()
    (ScriptionaryHelper.PLAYER_NOTES + DEFAULT_ENTRIES).distinct().forEach {
      val cat = if(it.contains("/")) it.split("/")[0] else ""
      categories[cat] = categories[cat] ?: mutableListOf()
      categories[cat]!!.add(it)
    }

    var categoriesMarkdown = ""
    categories.forEach { (name, list) ->
      val location = ScriptorMod.location(name)
      val entry = I18n.get(location.toLanguageKey("entry")) ?: name
      categoriesMarkdown += "### $entry\n"
      list.forEach {
        val location = ScriptorMod.location(it)
        val entry = I18n.get(location.toLanguageKey("entry")) ?: it
        categoriesMarkdown += " - [$entry](${location})\n"
      }
      categoriesMarkdown += "\n"
    }

    add(MarkdownWidget(
      leftPos + 20,
      topPos + 20,
      225,
      142,
      LocalizedMarkdownReader.read(ScriptorMod.location("dictionary")) + categoriesMarkdown,
      false,
      commandsAllowed = true
    )).setColor(0, 0, 0)
  }

//  fun addEntriesWidget() {
//    if(guideCategory != null) {
//      add(MarkdownWidget(
//        leftPos + 20,
//        topPos + 20,
//        225,
//        142,
//        "# " + I18n.get(ScriptorMod.location(guideCategory!!).toLanguageKey("entry")) +
//          "\n\n" +
//          (ScriptionaryHelper.PLAYER_NOTES + DEFAULT_ENTRIES).filter {
//            it.startsWith(guideCategory ?: "")
//          }.joinToString("\n\n") {
//            val location = ScriptorMod.location(it)
//            val entry = I18n.get(location.toLanguageKey("entry")) ?: it
//            "[$entry](${location})"
//          }
//      )).setColor(0, 0, 0)
//    }
//
//
//    val entries = mutableListOf<String>()
//    (ScriptionaryHelper.PLAYER_NOTES + DEFAULT_ENTRIES).forEach {
//      if(it.contains("/")) {
//        val location = ScriptorMod.location(it.split("/")[0])
//        val entry = I18n.get(location.toLanguageKey("entry")) ?: it
////        val cmd = "[$entry](cmd://scriptor page $location)" // TODO
//        val cmd = "[$entry](cmd://execute as @e[limit=1] run say Unimplemented, but you would've gone to the index for $location)"
//        if(cmd !in entries) entries.add(cmd)
//      } else {
//        val location = ScriptorMod.location(it)
//        val entry = I18n.get(location.toLanguageKey("entry")) ?: it
//        entries.add("[$entry](${location})")
//      }
//    }
//
//    add(MarkdownWidget(
//      leftPos + 20,
//      topPos + 20,
//      225,
//      142,
//      LocalizedMarkdownReader.read(ScriptorMod.location("dictionary")) +
//          entries.joinToString("\n\n"),
//      false,
//      commandsAllowed = true
//    )).setColor(0, 0, 0)
//  }

  companion object {
    val DEFAULT_ENTRIES: List<String> = listOf(
      "scriptor:casting/verbal_casting",
      "scriptor:casting/tome_casting",
      "scriptor:casting/writing_tomes",
    )
  }
}