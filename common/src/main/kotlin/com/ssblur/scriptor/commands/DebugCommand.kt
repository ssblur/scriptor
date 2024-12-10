package com.ssblur.scriptor.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import com.ssblur.scriptor.ScriptorMod.LOGGER
import com.ssblur.scriptor.registry.words.WordRegistry.actionRegistry
import com.ssblur.scriptor.registry.words.WordRegistry.descriptorRegistry
import com.ssblur.scriptor.registry.words.WordRegistry.subjectRegistry
import net.minecraft.client.resources.language.I18n
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player

object DebugCommand {
    fun register(
        dispatcher: CommandDispatcher<CommandSourceStack?>,
        ignoredRegistry: CommandBuildContext?,
        ignoredSelection: Commands.CommandSelection?
    ) {
        dispatcher.register(
            Commands.literal("scriptor_debug")
                .requires { s: CommandSourceStack -> s.hasPermission(4) }
                .executes { execute(it) }
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
}
