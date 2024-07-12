package com.ssblur.scriptor.mixin;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.events.network.client.ReceiveConfigNetwork;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftServer.class)
public class ReloadResourcesMixin {
  @Shadow private PlayerList playerList;

  @Inject(method = "reloadResources", at = @At("HEAD"))
  private void bookChanged(
    Collection<String> collection,
    CallbackInfoReturnable<CompletableFuture<Void>> info
  ) {
    ReceiveConfigNetwork.sendCommunityMode(this.playerList.getPlayers(), ScriptorMod.COMMUNITY_MODE);
  }
}
