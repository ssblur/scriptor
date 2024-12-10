package com.ssblur.scriptor.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.ssblur.scriptor.item.ScriptorItems;
import com.ssblur.scriptor.item.casters.CoordinateCasterCrystal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CoordinateCasterWorldRenderer {
  static final int BOX_COLOR = 0xffff5064;


  public static void render(@Nullable PoseStack matrix) {
    var player = Minecraft.getInstance().player;

    if (player != null && matrix != null) {
      if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == ScriptorItems.INSTANCE.getCOORDINATE_CASTING_CRYSTAL().get())
        render(matrix, player.getItemInHand(InteractionHand.MAIN_HAND));
      if (player.getItemInHand(InteractionHand.OFF_HAND).getItem() == ScriptorItems.INSTANCE.getCOORDINATE_CASTING_CRYSTAL().get())
        render(matrix, player.getItemInHand(InteractionHand.OFF_HAND));
    }
  }

  public static void render(PoseStack matrix, ItemStack item) {
    var tesselator = Tesselator.getInstance();
    BufferBuilder builder;
    MeshData mesh;
    var camera = Minecraft.getInstance().gameRenderer.getMainCamera();
    var last = matrix.last();
    var pose = last.pose();

    matrix.pushPose();
    RenderSystem.disableDepthTest();
    RenderSystem.enableBlend();
    RenderSystem.setShader(GameRenderer::getPositionColorShader);
    RenderSystem.disableCull();
    for(var pair: CoordinateCasterCrystal.getCoordinates(item)) {
      BlockPos pos = pair.getLeft();
      Direction direction = pair.getRight();

      float x = (float) (pos.getX() - camera.getPosition().x);
      float y = (float) (pos.getY() - camera.getPosition().y + 0.01f);
      float z = (float) (pos.getZ() - camera.getPosition().z);
      float xo = x + 1;
      float yo = y + 1;
      float zo = z + 1;

      switch(direction) {
        case DOWN -> y += 1;
        case UP -> yo -= 1;
        case NORTH -> z += 1;
        case WEST -> x += 1;
        case EAST -> xo -= 1;
      }

      if(direction.getAxis() == Direction.Axis.Z) {
        builder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        builder.addVertex(
          pose,
          x,
          y,
          z
        ).setColor(BOX_COLOR);
        builder.addVertex(
          pose,
          xo,
          y,
          z
        ).setColor(BOX_COLOR);
        builder.addVertex(
          pose,
          xo,
          yo,
          z
        ).setColor(BOX_COLOR);
        builder.addVertex(
          pose,
          x,
          yo,
          z
        ).setColor(BOX_COLOR);

        mesh = builder.build();
      } else {
        builder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        builder.addVertex(
          pose,
          x,
          y,
          z
        ).setColor(BOX_COLOR);
        builder.addVertex(
          pose,
          x,
          y,
          zo
        ).setColor(BOX_COLOR);
        builder.addVertex(
          pose,
          xo,
          yo,
          zo
        ).setColor(BOX_COLOR);
        builder.addVertex(
          pose,
          xo,
          yo,
          z
        ).setColor(BOX_COLOR);

        mesh = builder.build();
      }

      if(mesh != null) {
        BufferUploader.drawWithShader(mesh);
      }
    }

    matrix.popPose();
  }
}
