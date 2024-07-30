package com.ssblur.scriptor.mixin;

import com.ssblur.scriptor.data_components.ScriptorDataComponents;
import com.ssblur.scriptor.item.ScriptorItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.Filterable;
import net.minecraft.server.network.FilteredText;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WrittenBookContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
  @Shadow public ServerPlayer player;

  @Shadow protected abstract Filterable<String> filterableFromOutgoing(FilteredText filteredText);

  @Inject(method = "signBook", at = @At("HEAD"), cancellable = true)
  private void signBook(FilteredText filteredText, List<FilteredText> list, int i, CallbackInfo info) {
    ItemStack itemStack = this.player.getInventory().getItem(i);
    if (itemStack.is(ScriptorItems.WRITABLE_SPELLBOOK.get())) {
      ItemStack itemStack2 = itemStack.transmuteCopy(ScriptorItems.SPELLBOOK.get());
      itemStack2.remove(DataComponents.WRITABLE_BOOK_CONTENT);
      List<Filterable<Component>> list2 = list.stream().map(
        (filteredTextx) -> this.filterableFromOutgoing(filteredTextx).map(x -> (Component) Component.literal(x))
      ).toList();
      itemStack2.set(DataComponents.WRITTEN_BOOK_CONTENT, new WrittenBookContent(this.filterableFromOutgoing(filteredText), this.player.getName().getString(), 0, list2, true));
      itemStack2.set(ScriptorDataComponents.TOME_NAME, filteredText.filtered());
      this.player.getInventory().setItem(i, itemStack2);
      info.cancel();
    }
  }
}
