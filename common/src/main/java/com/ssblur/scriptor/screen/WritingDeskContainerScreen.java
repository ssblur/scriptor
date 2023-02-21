package com.ssblur.scriptor.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.container.WritingDeskContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;

public class WritingDeskContainerScreen extends AbstractContainerScreen<WritingDeskContainer> {
  static final ResourceLocation INVENTORY_LOCATION = new ResourceLocation(ScriptorMod.MOD_ID, "textures/gui/container/writing_desk.png");
  int imageWidth = 480 / 2;
  int imageHeight = 360 / 2;
  int tabWidth = 51 / 2;
  int tablessWidth = imageWidth - tabWidth;
  WritingDeskContainer container;

  public WritingDeskContainerScreen(WritingDeskContainer container, Inventory inventory, Component component) {
    super(container, inventory, component);

    this.container = container;
  }

  @Override
  protected void renderBg(PoseStack matrix, float f, int i, int j) {
    if(minecraft == null) return;
    matrix.pushPose();

    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, INVENTORY_LOCATION);
    int k = ((width - tablessWidth) / 2) - tabWidth;
    int l = (height - imageHeight) / 2;
    blit(matrix, k, l, 0, 0, imageWidth, imageHeight);

    k = (width - tablessWidth) / 2 + 19;
    l = (height - imageHeight) / 2 + 6;

    for(var slot: container.slots) {
      blit(matrix, k + slot.x, l + slot.y, 0, 218, 18, 18);
    }

    matrix.popPose();
  }
}
