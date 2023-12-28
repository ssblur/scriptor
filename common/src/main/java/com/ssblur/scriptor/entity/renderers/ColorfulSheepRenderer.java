package com.ssblur.scriptor.entity.renderers;

import com.ssblur.scriptor.entity.ColorfulSheep;
import com.ssblur.scriptor.entity.renderers.layer.ColorfulSheepFurLayer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class ColorfulSheepRenderer extends MobRenderer<ColorfulSheep, SheepModel<ColorfulSheep>> {
  private static final ResourceLocation SHEEP_LOCATION = new ResourceLocation("textures/entity/sheep/sheep.png");

  public ColorfulSheepRenderer(EntityRendererProvider.Context context) {
    super(context, new SheepModel<>(context.bakeLayer(ModelLayers.SHEEP)), 0.7F);
    this.addLayer(new ColorfulSheepFurLayer(this, context.getModelSet()));
  }

  public ResourceLocation getTextureLocation(ColorfulSheep sheep) {
    return SHEEP_LOCATION;
  }
}
