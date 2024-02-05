package com.ssblur.scriptor.blockentity.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.ssblur.scriptor.block.CastingLecternBlock;
import com.ssblur.scriptor.blockentity.CastingLecternBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;

public class CastingLecternBlockEntityRenderer implements BlockEntityRenderer<CastingLecternBlockEntity> {
  ItemRenderer itemRenderer;
  public CastingLecternBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    itemRenderer = context.getItemRenderer();
  }
  @Override
  public void render(CastingLecternBlockEntity lectern, float tickDelta, PoseStack matrix, MultiBufferSource buffers, int light, int overlay) {
    var level = lectern.getLevel();
    if(lectern.isRemoved() || level == null) return;
    float time = level.getGameTime() + tickDelta;
    time %= 101;
    time /= 100;

    matrix.pushPose();

    int combined = lectern.getBlockPos().getX() + lectern.getBlockPos().getY() + lectern.getBlockPos().getZ();
    float translateY = (float) Math.sin((combined + level.getGameTime() + (double) tickDelta) / 8);
    translateY /= 64;

    int rotationY = 0;
    float translateX = 0;
    float translateZ = 0;

    var state = level.getBlockState(lectern.getBlockPos());
    switch (state.getValue(CastingLecternBlock.FACING)) {
      case NORTH -> {
        translateZ -= 0.2f;
        rotationY += 180;
      }
      case SOUTH -> translateZ += 0.2f;
      case WEST -> {
        rotationY += 270;
        translateX -= 0.2f;
      }
      default -> {
        rotationY += 90;
        translateX += 0.2f;
      }
    }

    matrix.pushPose();

    matrix.translate(0.5f, 1.3f, 0.5f);
    matrix.scale(0.5f, 0.5f, 0.5f);

    matrix.mulPose(Axis.YP.rotationDegrees(time * 360));
    itemRenderer.renderStatic(lectern.getFocus(), ItemDisplayContext.GROUND, light, overlay, matrix, buffers, level, (int) lectern.getBlockPos().asLong());

    matrix.popPose();

    matrix.translate(0.5f + translateX, 1f + translateY, 0.5f + translateZ);

    matrix.mulPose(Axis.YP.rotationDegrees(rotationY));
    matrix.mulPose(Axis.XP.rotationDegrees(112));

    itemRenderer.renderStatic(lectern.getSpellbook(), ItemDisplayContext.FIXED, light, overlay, matrix, buffers, level, (int) lectern.getBlockPos().asLong());

    matrix.popPose();
  }
}
