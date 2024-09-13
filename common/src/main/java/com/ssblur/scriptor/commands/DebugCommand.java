package com.ssblur.scriptor.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.registry.words.WordRegistry;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class DebugCommand {
  public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext ignoredRegistry, Commands.CommandSelection ignoredSelection){
    dispatcher.register(Commands.literal("scriptor_debug")
      .requires(s -> s.hasPermission(4))
      .executes(DebugCommand::execute));
  }
  private static int execute(CommandContext<CommandSourceStack> command){
    StringBuilder message = new StringBuilder();
    for(var action: WordRegistry.INSTANCE.actionRegistry.keySet()) {
      if(!I18n.exists("action.scriptor." + action)) {
        message.append("Description not localized for action.scriptor.").append(action).append("\n");
      }
    }
    for(var descriptor: WordRegistry.INSTANCE.descriptorRegistry.keySet()){
      if(!I18n.exists("descriptor.scriptor." + descriptor)) {
        message.append("Description not localized for descriptor.scriptor.").append(descriptor).append("\n");
      }
    }
    for(var subject: WordRegistry.INSTANCE.subjectRegistry.keySet()) {
      if(!I18n.exists("subject.scriptor." + subject)) {
        message.append("Description not localized for subject.scriptor.").append(subject).append("\n");
      }
    }
    if(command.getSource().getEntity() instanceof Player player) {
      player.sendSystemMessage(Component.literal(message.toString()));
    } else {
      ScriptorMod.LOGGER.info(message.toString());
    }
    return Command.SINGLE_SUCCESS;
  }
}
