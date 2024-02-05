package com.ssblur.scriptor.entity.renderers.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ssblur.scriptor.entity.ColorfulSheep;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SheepFurModel;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class ColorfulSheepFurLayer extends RenderLayer<ColorfulSheep, SheepModel<ColorfulSheep>> {
  private static final ResourceLocation SHEEP_FUR_LOCATION = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
  private final SheepFurModel<ColorfulSheep> model;

  public ColorfulSheepFurLayer(RenderLayerParent<ColorfulSheep, SheepModel<ColorfulSheep>> renderLayerParent, EntityModelSet entityModelSet) {
    super(renderLayerParent);
    this.model = new SheepFurModel<>(entityModelSet.bakeLayer(ModelLayers.SHEEP_FUR));
  }

  public void render(
    PoseStack poseStack,
    MultiBufferSource multiBufferSource,
    int i,
    ColorfulSheep sheep,
    float f,
    float g,
    float h,
    float j,
    float k,
    float l
  ) {
    if (!sheep.isSheared())
      if (sheep.isInvisible()) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.shouldEntityAppearGlowing(sheep)) {
          getParentModel().copyPropertiesTo(model);
          model.prepareMobModel(sheep, f, g, h);
          model.setupAnim(sheep, f, g, j, k, l);
          VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.outline(SHEEP_FUR_LOCATION));
          model.renderToBuffer(poseStack, vertexConsumer, i, LivingEntityRenderer.getOverlayCoords(sheep, 0f), 0f, 0f, 0f, 1f);
        }
      } else {
        var color = sheep.getColorArray();
        coloredCutoutModelCopyLayerRender(
          getParentModel(),
          model,
          SHEEP_FUR_LOCATION,
          poseStack,
          multiBufferSource,
          i,
          sheep,
          f,
          g,
          j,
          k,
          l,
          h,
          ((float) color.getRed()) / 255f,
          ((float) color.getGreen()) / 255f,
          ((float) color.getBlue()) / 255f
        );
      }
  }
}
