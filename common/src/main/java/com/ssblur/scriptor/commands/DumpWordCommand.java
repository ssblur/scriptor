package com.ssblur.scriptor.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.data.DictionarySavedData;
import com.ssblur.scriptor.registry.words.WordRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class DumpWordCommand {
  private static final String[] otherWords = new String[] {
    "and"
  };

  public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection selection){
    var command = Commands.literal("dump_word").requires(s -> s.hasPermission(4)).executes(DumpWordCommand::execute);

    command = command.then(Commands
      .literal("action")
      .then(Commands.argument("word", StringArgumentType.string()).suggests(((context, builder) ->
        SharedSuggestionProvider.suggest(WordRegistry.INSTANCE.actionRegistry.keySet(), builder)
      ))
      .executes(getWord("action")))
      .requires(s -> s.hasPermission(4))
      .executes((context -> {
        if(context.getSource().getEntity() instanceof Player) {
          context.getSource().sendSystemMessage(Component.translatable("command.scriptor.dump_noword").withStyle(ChatFormatting.RED));
          context.getSource().sendSystemMessage(
            Component.translatable("command.scriptor.dump_actions", String.join(", ", WordRegistry.INSTANCE.actionRegistry.keySet()))
              .withStyle(ChatFormatting.RED));
        }
        return Command.SINGLE_SUCCESS;
      })));

    command = command.then(Commands
      .literal("descriptor")
      .then(Commands.argument("word", StringArgumentType.string()).suggests(((context, builder) ->
        SharedSuggestionProvider.suggest(WordRegistry.INSTANCE.descriptorRegistry.keySet(), builder)
      ))
      .executes(getWord("descriptor")))
      .requires(s -> s.hasPermission(4))
      .executes((context -> {
        if(context.getSource().getEntity() instanceof Player) {
          context.getSource().sendSystemMessage(Component.translatable("command.scriptor.dump_noword").withStyle(ChatFormatting.RED));
          context.getSource().sendSystemMessage(
            Component.translatable("command.scriptor.dump_descriptors", String.join(", ", WordRegistry.INSTANCE.descriptorRegistry.keySet()))
              .withStyle(ChatFormatting.RED));
        }
        return Command.SINGLE_SUCCESS;
      })));

    command = command.then(Commands
      .literal("subject")
      .then(Commands.argument("word", StringArgumentType.string()).suggests(((context, builder) ->
        SharedSuggestionProvider.suggest(WordRegistry.INSTANCE.subjectRegistry.keySet(), builder)
      ))
      .executes(getWord("subject")))
      .requires(s -> s.hasPermission(4))
      .executes((context -> {
        if(context.getSource().getEntity() instanceof Player) {
          context.getSource().sendSystemMessage(Component.translatable("command.scriptor.dump_noword").withStyle(ChatFormatting.RED));
          context.getSource().sendSystemMessage(
            Component.translatable("command.scriptor.dump_subjects", String.join(", ", WordRegistry.INSTANCE.subjectRegistry.keySet()))
              .withStyle(ChatFormatting.RED));
        }
        return Command.SINGLE_SUCCESS;
      })));

    command = command.then(Commands
      .literal("other")
      .then(Commands.argument("word", StringArgumentType.string()).suggests(((context, builder) ->
          SharedSuggestionProvider.suggest(otherWords, builder)
        ))
        .executes(getWord("other")))
      .requires(s -> s.hasPermission(4))
      .executes((context -> {
        if(context.getSource().getEntity() instanceof Player) {
          context.getSource().sendSystemMessage(Component.translatable("command.scriptor.dump_noword").withStyle(ChatFormatting.RED));
          context.getSource().sendSystemMessage(
            Component.translatable("command.scriptor.dump_others", "and")
              .withStyle(ChatFormatting.RED));
        }
        return Command.SINGLE_SUCCESS;
      })));

    dispatcher.register(command);
  }

  private static Command<CommandSourceStack> getWord(String category) {
    return (context) -> {
      var key = context.getArgument("word", String.class);
      if(context.getSource().getEntity() instanceof Player player) {
        if(ScriptorMod.COMMUNITY_MODE) {
          player.sendSystemMessage(Component.translatable("command.scriptor.community_mode"));
          return Command.SINGLE_SUCCESS;
        }
        var dict = DictionarySavedData.computeIfAbsent((ServerLevel) player.level());
        var data = dict.getWord(category + ":" + key);
        context.getSource().sendSystemMessage(Component.translatable("command.scriptor.dump_" + category, key, data));
      }
      return Command.SINGLE_SUCCESS;
    };
  }

  private static int execute(CommandContext<CommandSourceStack> command){
    if(command.getSource().getEntity() instanceof Player)
      command.getSource().sendSystemMessage(Component.translatable("command.scriptor.dump_noword").withStyle(ChatFormatting.RED));
    return Command.SINGLE_SUCCESS;
  }
}
