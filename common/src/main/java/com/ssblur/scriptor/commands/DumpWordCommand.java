package com.ssblur.scriptor.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.ssblur.scriptor.helpers.DictionarySavedData;
import com.ssblur.scriptor.registry.WordRegistry;
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
        var dict = DictionarySavedData.computeIfAbsent((ServerLevel) player.level);
        var data = dict.getWord("action:" + key);
        player.sendSystemMessage(Component.literal("The word for action \"" + key + "\" is \"" + data.word() + "\""));
        if(dict.actionArticlePosition == DictionarySavedData.ARTICLEPOSITION.AFTER)
          player.sendSystemMessage(
            Component.literal(
              "This word should be proceeded with the article \""
                + dict.actionGenderedArticles.get(data.gender())
                + "\""));
        else if(dict.actionArticlePosition == DictionarySavedData.ARTICLEPOSITION.BEFORE)
          player.sendSystemMessage(
            Component.literal(
              "This word should be preceeded with the article \""
                + dict.actionGenderedArticles.get(data.gender())
                + "\""));

      }
      return Command.SINGLE_SUCCESS;
    };
  }

  private static Command<CommandSourceStack> generateDescriptorCommand(String key) {
    return command -> {
      if(command.getSource().getEntity() instanceof Player player) {
        var dict = DictionarySavedData.computeIfAbsent((ServerLevel) player.level);
        var data = dict.getWord("descriptor:" + key);
        player.sendSystemMessage(Component.literal("The word for descriptor \"" + key + "\" is \"" + data.word() + "\""));
        if(dict.descriptorArticlePosition == DictionarySavedData.ARTICLEPOSITION.AFTER)
          player.sendSystemMessage(
            Component.literal(
              "This word should be proceeded with the article \""
                + dict.descriptorGenderedArticles.get(data.gender())
                + "\""));
        else if(dict.descriptorArticlePosition == DictionarySavedData.ARTICLEPOSITION.BEFORE)
          player.sendSystemMessage(
            Component.literal(
              "This word should be preceeded with the article \""
                + dict.descriptorGenderedArticles.get(data.gender())
                + "\""));

      }
      return Command.SINGLE_SUCCESS;
    };
  }

  private static Command<CommandSourceStack> generateSubjectCommand(String key) {
    return command -> {
      if(command.getSource().getEntity() instanceof Player player) {
        var dict = DictionarySavedData.computeIfAbsent((ServerLevel) player.level);
        var data = dict.getWord("subject:" + key);
        player.sendSystemMessage(Component.literal("The word for subject \"" + key + "\" is \"" + data.word() + "\""));
        if(dict.subjectArticlePosition == DictionarySavedData.ARTICLEPOSITION.AFTER)
          player.sendSystemMessage(
            Component.literal(
              "This word should be proceeded with the article \""
                + dict.subjectGenderedArticles.get(data.gender())
                + "\""));
        else if(dict.subjectArticlePosition == DictionarySavedData.ARTICLEPOSITION.BEFORE)
          player.sendSystemMessage(
            Component.literal(
              "This word should be preceeded with the article \""
                + dict.subjectGenderedArticles.get(data.gender())
                + "\""));

      }
      return Command.SINGLE_SUCCESS;
    };
  }

  private static int execute(CommandContext<CommandSourceStack> command){
    if(command.getSource().getEntity() instanceof Player player)
      player.sendSystemMessage(Component.literal("Please specify a word to dump."));
    return Command.SINGLE_SUCCESS;
  }
}
