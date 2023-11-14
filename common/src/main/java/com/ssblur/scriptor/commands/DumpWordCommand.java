package com.ssblur.scriptor.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.ssblur.scriptor.data.DictionarySavedData;
import com.ssblur.scriptor.registry.words.WordRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class DumpWordCommand {
  public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection selection){
    var command = Commands.literal("dump_word").requires(s -> s.hasPermission(4)).executes(DumpWordCommand::execute);

    for(String key: WordRegistry.INSTANCE.actionRegistry.keySet())
      command = command.then(Commands
        .literal("action:" + key)
        .requires(s -> s.hasPermission(4))
        .executes(generateActionCommand(key)));
    for(String key: WordRegistry.INSTANCE.descriptorRegistry.keySet())
      command = command.then(Commands
        .literal("descriptor:" + key)
        .requires(s -> s.hasPermission(4))
        .executes(generateDescriptorCommand(key)));
    for(String key: WordRegistry.INSTANCE.subjectRegistry.keySet())
      command = command.then(Commands
        .literal("subject:" + key)
        .requires(s -> s.hasPermission(4))
        .executes(generateSubjectCommand(key)));

    dispatcher.register(command);
  }

  private static Command<CommandSourceStack> generateActionCommand(String key) {
    return command -> {
      if(command.getSource().getEntity() instanceof Player player) {
        var dict = DictionarySavedData.computeIfAbsent((ServerLevel) player.level());
        var data = dict.getWord("action:" + key);
        command.getSource().sendSystemMessage(Component.translatable("command.scriptor.dump_action", key, data));
      }
      return Command.SINGLE_SUCCESS;
    };
  }

  private static Command<CommandSourceStack> generateDescriptorCommand(String key) {
    return command -> {
      if(command.getSource().getEntity() instanceof Player player) {
        var dict = DictionarySavedData.computeIfAbsent((ServerLevel) player.level());
        var data = dict.getWord("descriptor:" + key);
        command.getSource().sendSystemMessage(Component.translatable("command.scriptor.dump_descriptor", key, data));
      }
      return Command.SINGLE_SUCCESS;
    };
  }

  private static Command<CommandSourceStack> generateSubjectCommand(String key) {
    return command -> {
      if(command.getSource().getEntity() instanceof Player player) {
        var dict = DictionarySavedData.computeIfAbsent((ServerLevel) player.level());
        var data = dict.getWord("subject:" + key);
        command.getSource().sendSystemMessage(Component.translatable("command.scriptor.dump_subject", key, data));
      }
      return Command.SINGLE_SUCCESS;
    };
  }

  private static int execute(CommandContext<CommandSourceStack> command){
    if(command.getSource().getEntity() instanceof Player player)
      command.getSource().sendSystemMessage(Component.translatable("command.scriptor.dump_noword").withStyle(ChatFormatting.RED));
    return Command.SINGLE_SUCCESS;
  }
}
