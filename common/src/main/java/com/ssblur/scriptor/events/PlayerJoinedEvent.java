package com.ssblur.scriptor.events;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.advancement.ScriptorAdvancements;
import com.ssblur.scriptor.data.PlayerSpellsSavedData;
import com.ssblur.scriptor.events.network.client.ReceiveColorNetwork;
import com.ssblur.scriptor.events.network.client.ReceiveConfigNetwork;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.server.level.ServerPlayer;

public class PlayerJoinedEvent implements PlayerEvent.PlayerJoin {
  @Override
  public void join(ServerPlayer player) {
    if(ScriptorMod.INSTANCE.getCOMMUNITY_MODE())
      ScriptorAdvancements.COMMUNITY.get().trigger(player);
    ReceiveConfigNetwork.sendCommunityMode(player, ScriptorMod.INSTANCE.getCOMMUNITY_MODE());

    PlayerSpellsSavedData.computeIfAbsent(player);
    ReceiveColorNetwork.syncColors(player);
  }
}
