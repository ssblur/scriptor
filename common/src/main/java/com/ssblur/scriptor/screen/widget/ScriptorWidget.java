package com.ssblur.scriptor.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public abstract class ScriptorWidget extends GuiComponent implements GuiEventListener, Widget, NarratableEntry {
  ResourceLocation background;
  int x;
  int y;
  int uvx = 0;
  int uvy = 0;
  int uvw = 0;
  int uvl = 0;
  public ScriptorWidget(int x, int y, @Nullable ResourceLocation background) {
    this.background = background;
    this.x = x;
    this.y = y;
  }

  public ScriptorWidget uv(int uvx, int uvy, int uvw, int uvl) {
    this.uvx = uvx;
    this.uvy = uvy;
    this.uvw = uvw;
    this.uvl = uvl;
    return this;
  }

  @Override
  public void render(PoseStack matrix, int i, int j, float f) {
    matrix.pushPose();

    if(background != null) {
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.setShaderTexture(0, background);

      blit(matrix, x, y, uvx, uvy, uvw, uvl);
    }

    matrix.popPose();
  }
}
