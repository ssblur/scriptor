package com.ssblur.scriptor.events;

import com.ssblur.scriptor.data.PlayerSpellsSavedData;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.server.level.ServerPlayer;

public class PlayerJoinedEvent implements PlayerEvent.PlayerJoin {
  @Override
  public void join(ServerPlayer player) {
    PlayerSpellsSavedData.computeIfAbsent(player);
  }
}
