package com.ssblur.scriptor.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.ssblur.scriptor.config.ScriptorConfig
import com.ssblur.scriptor.events.SpellChat
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player

@Suppress("unused_parameter")
object CastCommand {
  fun register(
    dispatcher: CommandDispatcher<CommandSourceStack>,
    registry: CommandBuildContext?,
    selection: Commands.CommandSelection?
  ) {
    dispatcher.register(
      Commands.literal("cast")
        .requires { s: CommandSourceStack -> s.hasPermission(2) }
        .then(Commands.argument("spell", StringArgumentType.greedyString()).executes { execute(it) })
        .executes { noSpell(it) }
    )
  }

  private fun execute(command: CommandContext<CommandSourceStack>): Int {
    if(!ScriptorConfig.CAST_COMMAND_ENABLED()) {
      command.source.sendSystemMessage(Component.translatable("command.scriptor.cast_not_enabled").withStyle(ChatFormatting.RED))
      return Command.SINGLE_SUCCESS
    }

    if (command.source.entity is Player) {
      val player = command.source.entity as Player
      SpellChat.castFromChat(player, player.level() as ServerLevel, command.getArgument("spell", String::class.java))
    }
    return Command.SINGLE_SUCCESS
  }

  private fun noSpell(command: CommandContext<CommandSourceStack>): Int {
    command.source.sendSystemMessage(Component.translatable("command.scriptor.no_spell").withStyle(ChatFormatting.RED))
    return Command.SINGLE_SUCCESS
  }
}
