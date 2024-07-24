package com.ssblur.scriptor.blockentity.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ssblur.scriptor.blockentity.PhasedBlockBlockEntity;
import com.ssblur.scriptor.mixin.BlockRenderDispatcherAccessor;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class PhasedBlockBlockEntityRenderer  implements BlockEntityRenderer<PhasedBlockBlockEntity> {
  BlockEntityRendererProvider.Context context;
  public PhasedBlockBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    this.context = context;
  }
  @Override
  public void render(PhasedBlockBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
    if(poseStack == null) return;
    var blockState = blockEntity.getPhasedBlockState();
    var level = blockEntity.getLevel();

    if(blockState == null || level == null) return;

    var dispatcher = context.getBlockRenderDispatcher();
    var buffer = multiBufferSource.getBuffer(Sheets.translucentCullBlockSheet());
    var model = dispatcher.getBlockModel(blockState);
    var randomSource = RandomSource.create();
    var pose = poseStack.last();


    int color = ((BlockRenderDispatcherAccessor) dispatcher).getBlockColors().getColor(blockState, null, null, 0);
    float r = (color >> 16 & 0xFF) / 255.0f;
    float g = (color >> 8 & 0xFF) / 255.0f;
    float b = (color & 0xFF) / 255.0f;
    float a = 1.0f - (0.8f * blockEntity.getAnim());
    float ra, ga, ba;

    for(var direction: Direction.values()) {
      randomSource.setSeed(42L);
      for (BakedQuad bakedQuad : model.getQuads(blockState, direction, randomSource)) {
        if (bakedQuad.isTinted()) {
          ra = Mth.clamp(r, 0.0f, 1.0f);
          ga = Mth.clamp(g, 0.0f, 1.0f);
          ba = Mth.clamp(b, 0.0f, 1.0f);
        } else {
          ra = 1.0f;
          ga = 1.0f;
          ba = 1.0f;
        }
        buffer.putBulkData(pose, bakedQuad, ra, ga, ba, a, i, j);
      }
    }

    randomSource.setSeed(42L);
    for (BakedQuad bakedQuad : model.getQuads(blockState, null, randomSource)) {
      if (bakedQuad.isTinted()) {
        ra = Mth.clamp(r, 0.0f, 1.0f);
        ga = Mth.clamp(g, 0.0f, 1.0f);
        ba = Mth.clamp(b, 0.0f, 1.0f);
      } else {
        ra = 1.0f;
        ga = 1.0f;
        ba = 1.0f;
      }
      buffer.putBulkData(pose, bakedQuad, ra, ga, ba, a, i, j);
    }
  }
}
