package com.ssblur.scriptor.mixin;

import org.spongepowered.asm.mixin.gen.Accessor;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.client.gui.Gui.class)
public interface GuiAccessor {
    @Accessor
    int getToolHighlightTimer();
}
