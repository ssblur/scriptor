package com.ssblur.scriptor.mixin;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.item.AncientScrap;
import com.ssblur.scriptor.item.AncientSpellbook;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.GiveCommand;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(GiveCommand.class)
public class GiveCommandCommunityMixin {
  @Inject(method = "giveItem", at = @At("HEAD"), cancellable = true)
  private static void bookChanged(
    CommandSourceStack commandSourceStack,
    ItemInput itemInput,
    Collection<ServerPlayer> collection,
    int i,
    CallbackInfoReturnable<Integer> info
  ) {
    if(ScriptorMod.COMMUNITY_MODE && itemInput.getItem() instanceof AncientSpellbook) {
      collection.forEach(player -> player.sendSystemMessage(Component.translatable("command.scriptor.community_mode_give_tome")));
      info.setReturnValue(0);
    } else if(ScriptorMod.COMMUNITY_MODE && itemInput.getItem() instanceof AncientScrap) {
      collection.forEach(player -> player.sendSystemMessage(Component.translatable("command.scriptor.community_mode_give_scrap")));
      info.setReturnValue(0);
    }
  }
}
