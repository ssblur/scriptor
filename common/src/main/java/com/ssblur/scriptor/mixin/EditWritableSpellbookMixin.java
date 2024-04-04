package com.ssblur.scriptor.mixin;

import com.ssblur.scriptor.item.ScriptorItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.network.FilteredText;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.UnaryOperator;

@Mixin(ServerGamePacketListenerImpl.class)
public class EditWritableSpellbookMixin {

  @Inject(method = "updateBookContents", at = @At("HEAD"))
  private void updateBookContents(List<FilteredText> list, int i, CallbackInfo info) {
    var self = (ServerGamePacketListenerImpl) (Object) this;
    var invoker = (ServerGamePacketInvoker) self;
    ItemStack itemstack = self.player.getInventory().getItem(i);
    if (itemstack.is(ScriptorItems.WRITABLE_SPELLBOOK.get()))
      invoker.invokeUpdateBookPages(list, UnaryOperator.identity(), itemstack);

  }

  @Inject(method = "signBook", at = @At("HEAD"))
  private void signBook(FilteredText arg, List<FilteredText> list, int i, CallbackInfo ci) {
    var self = (ServerGamePacketListenerImpl) (Object) this;
    var invoker = (ServerGamePacketInvoker) self;
    ItemStack itemstack = self.player.getInventory().getItem(i);
    if (itemstack.is(ScriptorItems.WRITABLE_SPELLBOOK.get())) {
      ItemStack itemstack1 = new ItemStack(ScriptorItems.SPELLBOOK.get());
      CompoundTag compoundtag = itemstack.getTag();
      if (compoundtag != null)
        itemstack1.setTag(compoundtag.copy());

      itemstack1.addTagElement("author", StringTag.valueOf(self.player.getName().getString()));
      if (self.player.isTextFilteringEnabled()) {
        itemstack1.addTagElement("title", StringTag.valueOf(arg.filteredOrEmpty()));
      } else {
        itemstack1.addTagElement("filtered_title", StringTag.valueOf(arg.filteredOrEmpty()));
        itemstack1.addTagElement("title", StringTag.valueOf(arg.raw()));
      }

      invoker.invokeUpdateBookPages(list, (string) -> Component.Serializer.toJson(Component.literal(string)), itemstack1);
      self.player.getInventory().setItem(i, itemstack1);
    }

  }
}
