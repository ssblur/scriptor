package com.ssblur.scriptor.events;

import com.ssblur.scriptor.events.network.ScrollNetwork;
import com.ssblur.scriptor.item.BookOfBooks;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientRawInputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;

public class ScrollEvent implements ClientRawInputEvent.MouseScrolled {
  @Override
  public EventResult mouseScrolled(Minecraft client, double amountX, double amountY) {
    var player = client.player;
    if(player != null && player.isShiftKeyDown()) {
      if(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof BookOfBooks) {
        ScrollNetwork.sendScroll(InteractionHand.MAIN_HAND, amountY);
        return EventResult.interruptFalse();
      } else if (player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof BookOfBooks) {
        ScrollNetwork.sendScroll(InteractionHand.OFF_HAND, amountY);
        return EventResult.interruptFalse();
      }
    }
    return EventResult.pass();
  }
}
