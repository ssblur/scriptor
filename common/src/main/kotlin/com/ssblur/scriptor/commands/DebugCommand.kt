package com.ssblur.scriptor.commands

import com.google.gson.Gson
import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import com.ssblur.scriptor.ScriptorMod.COMMUNITY_MODE
import com.ssblur.scriptor.ScriptorMod.LOGGER
import com.ssblur.scriptor.data.components.DictionaryData
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.data.saved_data.DictionarySavedData
import com.ssblur.scriptor.item.ScriptorItems
import com.ssblur.scriptor.item.ScriptorTags
import com.ssblur.scriptor.registry.words.WordRegistry.actionRegistry
import com.ssblur.scriptor.registry.words.WordRegistry.descriptorRegistry
import com.ssblur.scriptor.registry.words.WordRegistry.subjectRegistry
import com.ssblur.scriptor.resources.Artifacts
import com.ssblur.scriptor.resources.Engravings
import com.ssblur.scriptor.resources.Scraps
import com.ssblur.scriptor.resources.Tomes
import net.minecraft.ChatFormatting
import net.minecraft.client.resources.language.I18n
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

object DebugCommand {
  @Suppress("unused_parameter")
  fun register(
    dispatcher: CommandDispatcher<CommandSourceStack>,
    registry: CommandBuildContext?,
    selection: Commands.CommandSelection?
  ) {
    dispatcher.register(
      Commands.literal("scriptor_debug").then(
        Commands.literal("localization")
          .requires { s: CommandSourceStack -> s.hasPermission(4) }
          .executes { executeNoLocale(it) }
      )
    )
    dispatcher.register(
      Commands.literal("scriptor_debug").then(
        Commands.literal("dump")
          .requires { s: CommandSourceStack -> s.hasPermission(4) }
          .executes { executeDump(it) }
      )
    )
    dispatcher.register(
      Commands.literal("scriptor_debug").then(
        Commands.literal("missing")
          .requires { s: CommandSourceStack -> s.hasPermission(4) }
          .executes { executeMissing(it) }
      )
    )
    dispatcher.register(
      Commands.literal("scriptor_debug").then(
        Commands.literal("give_dictionary")
          .requires { s: CommandSourceStack -> s.hasPermission(4) }
          .executes { executeDictionary(it) }
      )
    )
  }

  private fun executeNoLocale(command: CommandContext<CommandSourceStack>): Int {
    if(COMMUNITY_MODE) {
      command.source.sendSystemMessage(Component.translatable("command.scriptor.community_mode").withStyle(ChatFormatting.RED))
      return Command.SINGLE_SUCCESS
    }

    val message = StringBuilder()

    for (action in actionRegistry.keys) {
      if (!I18n.exists("action.scriptor.$action")) {
        message.append("Description not localized for action.scriptor.$action\n")
      }
    }
    for (descriptor in descriptorRegistry.keys) {
      if (!I18n.exists("descriptor.scriptor.$descriptor")) {
        message.append("Description not localized for descriptor.scriptor.$descriptor\n")
      }
    }
    for (subject in subjectRegistry.keys) {
      if (!I18n.exists("subject.scriptor.$subject")) {
        message.append("Description not localized for subject.scriptor.$subject\n")
      }
    }

    val tomesNoLocale = Tomes.tomes.values.filter{ !I18n.exists(it.name) }.map { it.name }
    if(tomesNoLocale.isNotEmpty()) message.append("The following tomes have no localized name: $tomesNoLocale\n")

    if (command.source.entity is Player) {
      (command.source.entity as Player).sendSystemMessage(Component.literal(message.toString()))
    } else {
      LOGGER.info(message.toString())
    }
    return Command.SINGLE_SUCCESS
  }

  private fun executeMissing(command: CommandContext<CommandSourceStack>): Int {
    if(COMMUNITY_MODE) {
      command.source.sendSystemMessage(Component.translatable("command.scriptor.community_mode").withStyle(ChatFormatting.RED))
      return Command.SINGLE_SUCCESS
    }

    val message = StringBuilder()

    val actionsNotInTomes = actionRegistry.keys.filter { word ->
      Tomes.tomes.values.none{ it.spell?.spells?.any{ it.action == word } ?: false }
              && Artifacts.artifacts.values.none{ it.spell?.spells?.any{ it.action == word } ?: false }
              && Engravings.engravings.values.none{ it.spell?.spells?.any{ it.action == word } ?: false }
    }
    message.append("The following actions do not appear in any tomes:").append(actionsNotInTomes).append("\n")
    val actionsNotInScraps = actionsNotInTomes.filter { word ->
      Scraps.scraps.values.none { it.keys.contains("action:$word") }
    }
    message.append("Of these, the following don't appear in scraps:").append(actionsNotInScraps).append("\n")

    val subjectsNotInTomes = subjectRegistry.keys.filter { word ->
      Tomes.tomes.values.none{ it.spell?.subject == word }
              && Artifacts.artifacts.values.none{ it.spell?.subject == word }
              && Engravings.engravings.values.none{ it.spell?.subject == word }
    }
    message.append("The following subjects do not appear in any tomes:").append(subjectsNotInTomes).append("\n")
    val subjectsNotInScraps = subjectsNotInTomes.filter { word ->
      Scraps.scraps.values.none { it.keys.contains("subject:$word") }
    }
    message.append("Of these, the following don't appear in scraps:").append(subjectsNotInScraps).append("\n")

    val descriptorsNotInTomes = descriptorRegistry.keys.filter { word ->
      Tomes.tomes.values.none { it.spell?.spells?.any{ it.descriptors.contains(word) } ?: false }
              && Artifacts.artifacts.values.none { it.spell?.spells?.any{ it.descriptors.contains(word) } ?: false }
              && Engravings.engravings.values.none { it.spell?.spells?.any{ it.descriptors.contains(word) } ?: false }
    }
    message.append("The following descriptors do not appear in any tomes:").append(descriptorsNotInTomes).append("\n")
    val descriptorsNotInScraps = descriptorsNotInTomes.filter { word ->
      Scraps.scraps.values.none { it.keys.contains("descriptor:$word") }
    }
    message.append("Of these, the following don't appear in scraps:").append(descriptorsNotInScraps).append("\n")


    if (command.source.entity is Player) {
      (command.source.entity as Player).sendSystemMessage(Component.literal(message.toString()))
    } else {
      LOGGER.info(message.toString())
    }
    return Command.SINGLE_SUCCESS
  }

  private fun executeDump(command: CommandContext<CommandSourceStack>): Int {
    if(COMMUNITY_MODE) {
      command.source.sendSystemMessage(Component.translatable("command.scriptor.community_mode").withStyle(ChatFormatting.RED))
      return Command.SINGLE_SUCCESS
    }

    val message = StringBuilder("Item list dumped as JSON")
    var keys = command.source.level.registryAccess().registry(Registries.ITEM).get().entrySet().map {
      it.key.location().toString()
    }
    val gson = Gson()
    LOGGER.info(gson.toJson(keys))

    keys = command.source.level.registryAccess().registry(Registries.ITEM).get()
      .getTag(ScriptorTags.SPELLBOOKS).get().map {
        it.unwrapKey().get().location().toString()
    }
    LOGGER.info(gson.toJson(keys))
    if (command.source.entity is Player) {
      (command.source.entity as Player).sendSystemMessage(Component.literal(message.toString()))
    } else {
      LOGGER.info(message.toString())
    }
    return Command.SINGLE_SUCCESS
  }

  private fun executeDictionary(command: CommandContext<CommandSourceStack>): Int {
    if(COMMUNITY_MODE) {
      command.source.sendSystemMessage(Component.translatable("command.scriptor.community_mode").withStyle(ChatFormatting.RED))
      return Command.SINGLE_SUCCESS
    }

    val item = ItemStack(ScriptorItems.DICTIONARY.get())
    val list = mutableListOf<List<String>>()
    val data = DictionarySavedData.computeIfAbsent(command.source.level)
    actionRegistry.keys.forEach {
      list.add(listOf(data.words["action:$it"]!!, I18n.get("action.scriptor.$it")))
    }
    descriptorRegistry.keys.forEach {
      list.add(listOf(data.words["descriptor:$it"]!!, I18n.get("descriptor.scriptor.$it")))
    }
    subjectRegistry.keys.forEach {
      list.add(listOf(data.words["subject:$it"]!!, I18n.get("subject.scriptor.$it")))
    }
    item[ScriptorDataComponents.DICTIONARY_DATA] = DictionaryData(list)
    command.source.player?.addItem(item)

    return Command.SINGLE_SUCCESS
  }
}
