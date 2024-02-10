package com.ssblur.scriptor.mixin;

import net.minecraft.server.network.FilteredText;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.function.UnaryOperator;

@Mixin(ServerGamePacketListenerImpl.class)
public interface ServerGamePacketInvoker {
  @Invoker("updateBookPages")
  void invokeUpdateBookPages(List<FilteredText> list, UnaryOperator<String> unaryOperator, ItemStack itemStack);
}
