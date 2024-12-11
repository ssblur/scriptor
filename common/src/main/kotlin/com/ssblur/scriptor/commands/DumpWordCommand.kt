package com.ssblur.scriptor.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.ssblur.scriptor.ScriptorMod.COMMUNITY_MODE
import com.ssblur.scriptor.data.saved_data.DictionarySavedData
import com.ssblur.scriptor.registry.words.WordRegistry.actionRegistry
import com.ssblur.scriptor.registry.words.WordRegistry.descriptorRegistry
import com.ssblur.scriptor.registry.words.WordRegistry.subjectRegistry
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player

@Suppress("unused")
object DumpWordCommand {
    private val otherWords = arrayOf(
        "and"
    )

    fun register(
        dispatcher: CommandDispatcher<CommandSourceStack?>,
        registry: CommandBuildContext?,
        selection: Commands.CommandSelection?
    ) {
        var command = Commands.literal("dump_word").requires { s: CommandSourceStack -> s.hasPermission(4) }
            .executes { execute(it) }

        command = command.then(Commands
            .literal("action")
            .then(
                Commands.argument("word", StringArgumentType.string())
                    .suggests((SuggestionProvider { context: CommandContext<CommandSourceStack?>?, builder: SuggestionsBuilder ->
                        SharedSuggestionProvider.suggest(
                            actionRegistry.keys, builder
                        )
                    }
                            ))
                    .executes(getWord("action")))
            .requires { s: CommandSourceStack -> s.hasPermission(4) }
            .executes((Command { context: CommandContext<CommandSourceStack> ->
                if (context.source.entity is Player) {
                    context.source.sendSystemMessage(
                        Component.translatable("command.scriptor.dump_noword").withStyle(
                            ChatFormatting.RED
                        )
                    )
                    context.source.sendSystemMessage(
                        Component.translatable(
                            "command.scriptor.dump_actions",
                            java.lang.String.join(", ", actionRegistry.keys)
                        )
                            .withStyle(ChatFormatting.RED)
                    )
                }
                Command.SINGLE_SUCCESS
            }))
        )

        command = command.then(Commands
            .literal("descriptor")
            .then(
                Commands.argument("word", StringArgumentType.string())
                    .suggests { _: CommandContext<CommandSourceStack?>?, builder: SuggestionsBuilder ->
                        SharedSuggestionProvider.suggest(
                            descriptorRegistry.keys, builder
                        )
                    }.executes(getWord("descriptor"))
            ).requires { it.hasPermission(4) }
            .executes { context ->
                context.source.sendSystemMessage(
                    Component.translatable("command.scriptor.dump_noword").withStyle(
                        ChatFormatting.RED
                    )
                )
                context.source.sendSystemMessage(
                    Component.translatable(
                        "command.scriptor.dump_descriptors",
                        java.lang.String.join(", ", descriptorRegistry.keys)
                    )
                        .withStyle(ChatFormatting.RED)
                )
                Command.SINGLE_SUCCESS
            }
        )

        command = command.then(Commands
            .literal("subject")
            .then(
                Commands.argument("word", StringArgumentType.string())
                    .suggests { _: CommandContext<CommandSourceStack?>?, builder: SuggestionsBuilder ->
                        SharedSuggestionProvider.suggest(
                            subjectRegistry.keys, builder
                        )
                    }.executes(getWord("subject")))
            .requires { it.hasPermission(4) }
            .executes { context ->
                context.source.sendSystemMessage(
                    Component.translatable("command.scriptor.dump_noword").withStyle(
                        ChatFormatting.RED
                    )
                )
                context.source.sendSystemMessage(
                    Component.translatable(
                        "command.scriptor.dump_subjects",
                        java.lang.String.join(", ", subjectRegistry.keys)
                    )
                        .withStyle(ChatFormatting.RED)
                )
                Command.SINGLE_SUCCESS
            }
        )

        command = command.then(Commands
            .literal("other")
            .then(
                Commands.argument("word", StringArgumentType.string())
                .suggests { _, builder ->
                    SharedSuggestionProvider.suggest(otherWords, builder)
                }.executes(getWord("other"))
            ).requires { it.hasPermission(4) }
            .executes { context: CommandContext<CommandSourceStack> ->
                context.source.sendSystemMessage(
                    Component.translatable("command.scriptor.dump_noword").withStyle(
                        ChatFormatting.RED
                    )
                )
                context.source.sendSystemMessage(
                    Component.translatable("command.scriptor.dump_others", "and")
                        .withStyle(ChatFormatting.RED)
                )
                Command.SINGLE_SUCCESS
            }
        )

        dispatcher.register(command)
    }

    private fun getWord(category: String): Command<CommandSourceStack> {
        return Command { context: CommandContext<CommandSourceStack> ->
            val key = context.getArgument("word", String::class.java)
            if (context.source.entity is Player) {
                val player = context.source.entity as Player
                if (COMMUNITY_MODE) {
                    player.sendSystemMessage(Component.translatable("command.scriptor.community_mode"))
                    return@Command Command.SINGLE_SUCCESS
                }
                val dict = DictionarySavedData.computeIfAbsent(player.level() as ServerLevel)
                val data = dict.getWord("$category:$key")
                context.source.sendSystemMessage(Component.translatable("command.scriptor.dump_$category", key, data))
            }
            Command.SINGLE_SUCCESS
        }
    }

    private fun execute(command: CommandContext<CommandSourceStack>): Int {
        if (command.source.entity is Player) command.source.sendSystemMessage(
            Component.translatable("command.scriptor.dump_noword").withStyle(ChatFormatting.RED)
        )
        return Command.SINGLE_SUCCESS
    }
}
