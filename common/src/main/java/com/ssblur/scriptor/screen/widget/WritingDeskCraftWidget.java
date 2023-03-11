package com.ssblur.scriptor.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.ssblur.scriptor.ScriptorMod;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class WritingDeskCraftWidget extends ImageButton {
  static final ResourceLocation BUTTON_LOCATION = new ResourceLocation(ScriptorMod.MOD_ID, "textures/gui/container/writing_desk.png");
  static final int BUTTON_WIDTH = 52;
  static final int BUTTON_HEIGHT = 32;
  static final int BUTTON_X = 186;
  static final int BUTTON_Y = 360;
  static final int ACTUAL_SIZE = 512;
  static final int SIZE_DENOMINATOR = 2;
  static OnTooltip TOOLTIP = (button, poseStack, i, j) -> {

  };

  boolean highlighted = false;



  public WritingDeskCraftWidget(int x, int y, Button.OnPress onPress) {
    super(
      x,
      y,
      BUTTON_WIDTH / SIZE_DENOMINATOR,
      BUTTON_HEIGHT / SIZE_DENOMINATOR,
      BUTTON_X / SIZE_DENOMINATOR,
      BUTTON_Y / SIZE_DENOMINATOR,
      BUTTON_HEIGHT / SIZE_DENOMINATOR,
      BUTTON_LOCATION,
      ACTUAL_SIZE / SIZE_DENOMINATOR,
      ACTUAL_SIZE / SIZE_DENOMINATOR,
      onPress,
      TOOLTIP,
      Component.translatable("scriptor.gui.widget.writing_desk_craft")
    );
  }
}
