package com.ssblur.scriptor.blockentity.renderers;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ssblur.scriptor.blockentity.CastingLecternBlockEntity;
import com.ssblur.scriptor.blockentity.PhasedBlockBlockEntity;
import com.ssblur.scriptor.mixin.BlockRenderDispatcherAccessor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.DisplayRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.RenderShape;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class PhasedBlockBlockEntityRenderer  implements BlockEntityRenderer<PhasedBlockBlockEntity> {
  BlockEntityRendererProvider.Context context;
  public PhasedBlockBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    this.context = context;
  }
  @Override
  public void render(PhasedBlockBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
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
        putBulkData(buffer, pose, bakedQuad, ra, ga, ba, a, i, j);
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
      putBulkData(buffer, pose, bakedQuad, ra, ga, ba, a, i, j);
    }
  }

  public void putBulkData(VertexConsumer consumer, PoseStack.Pose pose, BakedQuad bakedQuad, float f, float g, float h, float a, int i, int j) {
    putBulkData(consumer, pose, bakedQuad, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, f, g, h, a, new int[]{i, i, i, i}, j, false);
  }

  public void putBulkData(VertexConsumer consumer, PoseStack.Pose pose, BakedQuad bakedQuad, float[] fs, float f, float g, float h, float a, int[] is, int i, boolean bl) {
    float[] gs = new float[]{fs[0], fs[1], fs[2], fs[3]};
    int[] js = new int[]{is[0], is[1], is[2], is[3]};
    int[] ks = bakedQuad.getVertices();
    Vec3i vec3i = bakedQuad.getDirection().getNormal();
    Matrix4f matrix4f = pose.pose();
    Vector3f vector3f = pose.normal().transform(new Vector3f(vec3i.getX(), vec3i.getY(), vec3i.getZ()));
    int j = 8;
    int k = ks.length / 8;
    try (MemoryStack memoryStack = MemoryStack.stackPush()) {
      ByteBuffer byteBuffer = memoryStack.malloc(DefaultVertexFormat.BLOCK.getVertexSize());
      IntBuffer intBuffer = byteBuffer.asIntBuffer();
      for (int l = 0; l < k; ++l) {
        float u;
        float t;
        float s;
        float r;
        float q;
        intBuffer.clear();
        intBuffer.put(ks, l * 8, 8);
        float m = byteBuffer.getFloat(0);
        float n = byteBuffer.getFloat(4);
        float o = byteBuffer.getFloat(8);
        if (bl) {
          float p = (float) (byteBuffer.get(12) & 0xFF) / 255.0f;
          q = (float) (byteBuffer.get(13) & 0xFF) / 255.0f;
          r = (float) (byteBuffer.get(14) & 0xFF) / 255.0f;
          s = p * gs[l] * f;
          t = q * gs[l] * g;
          u = r * gs[l] * h;
        } else {
          s = gs[l] * f;
          t = gs[l] * g;
          u = gs[l] * h;
        }
        int v = js[l];
        q = byteBuffer.getFloat(16);
        r = byteBuffer.getFloat(20);
        Vector4f vector4f = matrix4f.transform(new Vector4f(m, n, o, 1.0f));
        consumer.vertex(vector4f.x(), vector4f.y(), vector4f.z(), s, t, u, a, q, r, i, v, vector3f.x(), vector3f.y(), vector3f.z());
      }
    }
  }
}
