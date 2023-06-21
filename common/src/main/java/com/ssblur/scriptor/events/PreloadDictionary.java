package com.ssblur.scriptor.events;

import com.ssblur.scriptor.data.DictionarySavedData;
import dev.architectury.event.events.common.LifecycleEvent;
import net.minecraft.server.level.ServerLevel;

public class PreloadDictionary implements LifecycleEvent.ServerLevelState {
  @Override
  public void act(ServerLevel world) {
    DictionarySavedData.computeIfAbsent(world);
  }
}
