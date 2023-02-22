package com.ssblur.scriptor.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.ssblur.scriptor.ScriptorMod;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class WritingDeskCraftWidget extends ScriptorWidget {
  static final ResourceLocation BUTTON_LOCATION = new ResourceLocation(ScriptorMod.MOD_ID, "textures/gui/container/writing_desk.png");
  static final int BUTTON_WIDTH = 51;
  static final int BUTTON_HEIGHT = 18;
  static final int BUTTON_X = 0;
  static final int BUTTON_Y = 492;

  boolean highlighted = false;

  public WritingDeskCraftWidget(int x, int y) {
    super(x, y, BUTTON_LOCATION);
    this.uv(BUTTON_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
  }

  @Override
  public void render(PoseStack matrix, int i, int j, float f) {
    super.render(matrix, i, j, f);

    highlighted = isMouseOver(i, j);
    if(highlighted)
      System.out.println(1);
  }

  @Override
  public NarrationPriority narrationPriority() {
    return NarrationPriority.HOVERED;
  }

  @Override
  public boolean isActive() {
    return super.isActive();
  }

  @Override
  public void updateNarration(NarrationElementOutput narrationElementOutput) {
    narrationElementOutput.add(NarratedElementType.USAGE, Component.translatable("scriptor.gui.widget.writing_desk_craft"));
  }

  @Override
  public boolean mouseClicked(double d, double e, int i) {
    return false;
  }

  @Override
  public boolean isMouseOver(double d, double e) {
    return (x < d && d < (x + BUTTON_WIDTH)) && (y < e && e < (y + BUTTON_HEIGHT));
  }
}
