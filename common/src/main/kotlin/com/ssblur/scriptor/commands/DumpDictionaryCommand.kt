package com.ssblur.scriptor.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import com.ssblur.scriptor.ScriptorMod.COMMUNITY_MODE
import com.ssblur.scriptor.data.saved_data.DictionarySavedData
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player

@Suppress("unused_parameter")
object DumpDictionaryCommand {
  fun register(
    dispatcher: CommandDispatcher<CommandSourceStack>,
    registry: CommandBuildContext?,
    selection: Commands.CommandSelection?
  ) {
    dispatcher.register(
      Commands.literal("dump_dictionary").requires { it.hasPermission(4) }.executes { execute(it) }
    )
  }

  private fun execute(command: CommandContext<CommandSourceStack>): Int {
    if (command.source.entity is Player) {
      val player = command.source.entity as Player
      if (COMMUNITY_MODE) player.sendSystemMessage(Component.translatable("command.scriptor.community_mode"))
      else player.sendSystemMessage(
        Component.literal(DictionarySavedData.computeIfAbsent(player.level() as ServerLevel).toString())
      )
    }
    return Command.SINGLE_SUCCESS
  }
}
