package com.ssblur.scriptor.events;

import com.ssblur.scriptor.helpers.ParticleQueue;
import dev.architectury.event.events.client.ClientTickEvent;

public class ClientLevelTickEvent implements ClientTickEvent.ClientLevel {
  @Override
  public void tick(net.minecraft.client.multiplayer.ClientLevel instance) {
    ParticleQueue.INSTANCE.process(instance);
  }
}
