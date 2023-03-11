package com.ssblur.scriptor.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.container.WritingDeskContainer;
import com.ssblur.scriptor.screen.widget.WritingDeskCraftWidget;
import net.minecraft.client.gui.screens.Screen;
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
  static final int IMAGE_WIDTH = 480 / 2;
  static final int IMAGE_HEIGHT = 360 / 2;
  static final int TAB_WIDTH = 51 / 2;
  static final int TAB_HEIGHT = 63 / 2;
  static final int TABLESS_WIDTH = IMAGE_WIDTH - TAB_WIDTH;
  static final int TAB_Y = 373 / 2;
  static final int TAB_Y_OFFSET = 13 / 2;
  static final int TAB_Y_SPACING = 12 / 2;
  int activeTab = 0;
  WritingDeskContainer container;

  public WritingDeskContainerScreen(WritingDeskContainer container, Inventory inventory, Component component) {
    super(container, inventory, component);

    this.container = container;
  }

  @Override
  protected void init() {
    super.init();

    int k = width / 2;
    int l = height / 2;
    addRenderableWidget(new WritingDeskCraftWidget(k + 25, l - 45, button -> {

    }));
  }

  @Override
  public void render(PoseStack poseStack, int i, int j, float f) {
    super.render(poseStack, i, j, f);
  }

  @Override
  protected void renderBg(PoseStack matrix, float f, int i, int j) {
    if(minecraft == null) return;
    matrix.pushPose();

    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, INVENTORY_LOCATION);
    int k = ((width - TABLESS_WIDTH) / 2) - TAB_WIDTH;
    int l = (height - IMAGE_HEIGHT) / 2;
    blit(matrix, k, l, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

    k += 4;
    l += TAB_Y_OFFSET;
    l += (TAB_Y_SPACING + TAB_HEIGHT) * activeTab;
    blit(matrix, k, l , TAB_WIDTH * activeTab, TAB_Y, TAB_WIDTH, TAB_HEIGHT);

    k = (width - TABLESS_WIDTH) / 2 + 19;
    l = (height - IMAGE_HEIGHT) / 2 + 6;

    for(int s = 0; s < container.slots.size(); s++) {
      var slot = container.slots.get(s);
      blit(matrix, k + slot.x, l + slot.y, 18 * Math.max(0, s - 35), 218, 18, 18);
    }
    
    

    matrix.popPose();
  }
}
