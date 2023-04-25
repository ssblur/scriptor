package com.ssblur.scriptor.blockentity.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.ssblur.scriptor.block.CastingLecternBlock;
import com.ssblur.scriptor.blockentity.CastingLecternBlockEntity;
import com.ssblur.scriptor.item.ScriptorItems;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemStack;

public class CastingLecternBlockEntityRenderer implements BlockEntityRenderer<CastingLecternBlockEntity> {
  ItemRenderer itemRenderer;
  public CastingLecternBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    itemRenderer = context.getItemRenderer();
  }
  @Override
  public void render(CastingLecternBlockEntity lectern, float tickDelta, PoseStack matrix, MultiBufferSource buffers, int light, int overlay) {
    var level = lectern.getLevel();
    if(lectern.isRemoved() || level == null) return;

    matrix.pushPose();

    float translateY = (float) Math.sin((lectern.getBlockPos().hashCode() + level.getGameTime() + (double) tickDelta) / 8);
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

    matrix.translate(0.5f + translateX, 1f + translateY, 0.5f + translateZ);
    matrix.mulPose(Vector3f.YP.rotationDegrees(rotationY));
    matrix.mulPose(Vector3f.XP.rotationDegrees(112));

    itemRenderer.renderStatic(lectern.getSpellbook(), ItemTransforms.TransformType.FIXED, light, overlay, matrix, buffers, (int) lectern.getBlockPos().asLong());

    matrix.popPose();
  }
}
