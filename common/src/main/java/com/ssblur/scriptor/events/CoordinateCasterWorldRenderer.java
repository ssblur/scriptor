package com.ssblur.scriptor.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Vector3f;
import com.ssblur.scriptor.item.ScriptorItems;
import com.ssblur.scriptor.item.casters.CoordinateCasterCrystal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class CoordinateCasterWorldRenderer {
  static final int BOX_COLOR = 0xffff5064;


  public static void render(PoseStack matrix) {
    var player = Minecraft.getInstance().player;
    ItemStack item;
    if (player != null) {
      if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == ScriptorItems.COORDINATE_CASTING_CRYSTAL.get())
        render(matrix, player.getItemInHand(InteractionHand.MAIN_HAND));
      if (player.getItemInHand(InteractionHand.OFF_HAND).getItem() == ScriptorItems.COORDINATE_CASTING_CRYSTAL.get())
        render(matrix, player.getItemInHand(InteractionHand.OFF_HAND));
    }
  }

  public static void render(PoseStack matrix, ItemStack item) {
    var tesselator = Tesselator.getInstance();
    var builder = tesselator.getBuilder();
    var camera = Minecraft.getInstance().gameRenderer.getMainCamera();
    var pose = matrix.last().pose();

    matrix.pushPose();
    RenderSystem.disableTexture();
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
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        builder.vertex(
          pose,
          x,
          y,
          z
        ).color(BOX_COLOR).endVertex();
        builder.vertex(
          pose,
          xo,
          y,
          z
        ).color(BOX_COLOR).endVertex();
        builder.vertex(
          pose,
          xo,
          yo,
          z
        ).color(BOX_COLOR).endVertex();
        builder.vertex(
          pose,
          x,
          yo,
          z
        ).color(BOX_COLOR).endVertex();

        tesselator.end();
      } else {
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        builder.vertex(
          pose,
          x,
          y,
          z
        ).color(BOX_COLOR).endVertex();
        builder.vertex(
          pose,
          x,
          y,
          zo
        ).color(BOX_COLOR).endVertex();
        builder.vertex(
          pose,
          xo,
          yo,
          zo
        ).color(BOX_COLOR).endVertex();
        builder.vertex(
          pose,
          xo,
          yo,
          z
        ).color(BOX_COLOR).endVertex();

        tesselator.end();
      }
    }

    matrix.popPose();
  }
}
