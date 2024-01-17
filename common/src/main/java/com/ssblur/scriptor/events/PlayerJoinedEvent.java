package com.ssblur.scriptor.events;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.advancement.ScriptorAdvancements;
import com.ssblur.scriptor.data.PlayerSpellsSavedData;
import com.ssblur.scriptor.events.network.ColorNetwork;
import com.ssblur.scriptor.events.network.ConfigNetwork;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.server.level.ServerPlayer;

public class PlayerJoinedEvent implements PlayerEvent.PlayerJoin {
  @Override
  public void join(ServerPlayer player) {
    if(ScriptorMod.COMMUNITY_MODE)
      ScriptorAdvancements.COMMUNITY.get().trigger(player);
    ConfigNetwork.sendCommunityMode(player, ScriptorMod.COMMUNITY_MODE);

    PlayerSpellsSavedData.computeIfAbsent(player);

    ColorNetwork.syncColors(player);
  }
}
