package com.ssblur.scriptor.entity.renderers

import com.ssblur.scriptor.entity.ColorfulSheep
import com.ssblur.scriptor.entity.renderers.layer.ColorfulSheepFurLayer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.model.SheepModel
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.resources.ResourceLocation

@Environment(EnvType.CLIENT)
class ColorfulSheepRenderer(context: EntityRendererProvider.Context):
  MobRenderer<ColorfulSheep, SheepModel<ColorfulSheep>?>(
    context, SheepModel(
      context.bakeLayer(
        ModelLayers.SHEEP
      )
    ), 0.7f
  ) {
  init {
    this.addLayer(ColorfulSheepFurLayer(this, context.modelSet))
  }

  override fun getTextureLocation(sheep: ColorfulSheep): ResourceLocation {
    return SHEEP_LOCATION
  }

  companion object {
    private val SHEEP_LOCATION: ResourceLocation = ResourceLocation.parse("textures/entity/sheep/sheep.png")
  }
}
