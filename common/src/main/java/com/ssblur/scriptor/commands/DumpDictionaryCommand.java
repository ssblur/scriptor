package com.ssblur.scriptor.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.data.DictionarySavedData;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class  DumpDictionaryCommand {
  public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection selection){
    dispatcher.register(Commands.literal("dump_dictionary")
      .requires(s -> s.hasPermission(4))
      .executes(DumpDictionaryCommand::execute));
  }
  private static int execute(CommandContext<CommandSourceStack> command){
    if(command.getSource().getEntity() instanceof Player player) {
      if(ScriptorMod.COMMUNITY_MODE)
        player.sendSystemMessage(Component.translatable("command.scriptor.community_mode"));
      else
        player.sendSystemMessage(Component.literal(DictionarySavedData.computeIfAbsent((ServerLevel) player.level()).toString()));
    }
    return Command.SINGLE_SUCCESS;
  }
}
