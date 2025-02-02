package com.ssblur.scriptor.mixin;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.network.client.ScriptorNetworkS2C;
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
public class ScriptorMinecraftServerReloadResourcesMixin {
  @Shadow
  private PlayerList playerList;

  @Inject(method = "reloadResources", at = @At("HEAD"))
  private void scriptor$reloadResources(
    Collection<String> collection,
    CallbackInfoReturnable<CompletableFuture<Void>> info
  ) {
    if (this.playerList != null)
      ScriptorNetworkS2C.INSTANCE.getFlag().invoke(
        new ScriptorNetworkS2C.Flag(ScriptorNetworkS2C.FLAGS.COMMUNITY, ScriptorMod.INSTANCE.getCOMMUNITY_MODE()),
        this.playerList.getPlayers()
      );
  }
}
