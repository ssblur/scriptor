package com.ssblur.scriptor.events;

import com.ssblur.scriptor.item.books.BookOfBooks;
import com.ssblur.scriptor.network.server.ScriptorNetworkC2S;
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
        ScriptorNetworkC2S.INSTANCE.getScroll().invoke(new ScriptorNetworkC2S.Scroll(InteractionHand.MAIN_HAND, amountY));
        return EventResult.interruptFalse();
      } else if (player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof BookOfBooks) {
        ScriptorNetworkC2S.INSTANCE.getScroll().invoke(new ScriptorNetworkC2S.Scroll(InteractionHand.OFF_HAND, amountY));
        return EventResult.interruptFalse();
      }
    }
    return EventResult.pass();
  }
}
