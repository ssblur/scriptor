package com.ssblur.scriptor.commands

import com.google.gson.Gson
import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import com.ssblur.scriptor.ScriptorMod.LOGGER
import com.ssblur.scriptor.item.ScriptorTags
import com.ssblur.scriptor.registry.words.WordRegistry.actionRegistry
import com.ssblur.scriptor.registry.words.WordRegistry.descriptorRegistry
import com.ssblur.scriptor.registry.words.WordRegistry.subjectRegistry
import net.minecraft.client.resources.language.I18n
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player

object DebugCommand {
  @Suppress("unused_parameter")
  fun register(
    dispatcher: CommandDispatcher<CommandSourceStack>,
    registry: CommandBuildContext?,
    selection: Commands.CommandSelection?
  ) {
    dispatcher.register(
      Commands.literal("scriptor_debug")
        .requires { s: CommandSourceStack -> s.hasPermission(4) }
        .executes { execute(it) }
    )
    dispatcher.register(
      Commands.literal("scriptor_debug").then(
        Commands.literal("dump")
          .requires { s: CommandSourceStack -> s.hasPermission(4) }
          .executes { executeDump(it) }
      )
    )
  }

  private fun execute(command: CommandContext<CommandSourceStack>): Int {
    val message = StringBuilder()
    for (action in actionRegistry.keys) {
      if (!I18n.exists("action.scriptor.$action")) {
        message.append("Description not localized for action.scriptor.").append(action).append("\n")
      }
    }
    for (descriptor in descriptorRegistry.keys) {
      if (!I18n.exists("descriptor.scriptor.$descriptor")) {
        message.append("Description not localized for descriptor.scriptor.").append(descriptor).append("\n")
      }
    }
    for (subject in subjectRegistry.keys) {
      if (!I18n.exists("subject.scriptor.$subject")) {
        message.append("Description not localized for subject.scriptor.").append(subject).append("\n")
      }
    }
    if (command.source.entity is Player) {
      (command.source.entity as Player).sendSystemMessage(Component.literal(message.toString()))
    } else {
      LOGGER.info(message.toString())
    }
    return Command.SINGLE_SUCCESS
  }

  private fun executeDump(command: CommandContext<CommandSourceStack>): Int {
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
}
