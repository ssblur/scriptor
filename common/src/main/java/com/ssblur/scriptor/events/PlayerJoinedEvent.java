package com.ssblur.scriptor.events;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.advancement.ScriptorAdvancements;
import com.ssblur.scriptor.data.saved_data.PlayerSpellsSavedData;
import com.ssblur.scriptor.network.client.ScriptorNetworkS2C;
import com.ssblur.scriptor.resources.Colors;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class PlayerJoinedEvent implements PlayerEvent.PlayerJoin {
  @Override
  public void join(ServerPlayer player) {
    if(ScriptorMod.INSTANCE.getCOMMUNITY_MODE())
      ScriptorAdvancements.INSTANCE.getCOMMUNITY().get().trigger(player);

    ScriptorNetworkS2C.INSTANCE.getFlag().invoke(
      new ScriptorNetworkS2C.Flag(ScriptorNetworkS2C.FLAGS.COMMUNITY, ScriptorMod.INSTANCE.getCOMMUNITY_MODE()),
      List.of(player)
    );

    PlayerSpellsSavedData.computeIfAbsent(player);
    for(var item: Colors.INSTANCE.getCache())
      ScriptorNetworkS2C.INSTANCE.getColor().invoke(
        new ScriptorNetworkS2C.Color(item.getB(), item.getA(), item.getC()[0], item.getC()[1], item.getC()[2]),
        List.of(player)
      );
  }
}
