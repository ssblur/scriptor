package com.ssblur.scriptor.entity.renderers.layer

import com.mojang.blaze3d.vertex.PoseStack
import com.ssblur.scriptor.entity.ColorfulSheep
import net.minecraft.client.Minecraft
import net.minecraft.client.model.SheepFurModel
import net.minecraft.client.model.SheepModel
import net.minecraft.client.model.geom.EntityModelSet
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.LivingEntityRenderer
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.resources.ResourceLocation

class ColorfulSheepFurLayer(
    renderLayerParent: RenderLayerParent<ColorfulSheep?, SheepModel<ColorfulSheep?>?>?,
    entityModelSet: EntityModelSet
) : RenderLayer<ColorfulSheep?, SheepModel<ColorfulSheep?>?>(renderLayerParent) {
    private val model = SheepFurModel<ColorfulSheep>(entityModelSet.bakeLayer(ModelLayers.SHEEP_FUR))

    override fun render(
        poseStack: PoseStack,
        multiBufferSource: MultiBufferSource,
        i: Int,
        sheep: ColorfulSheep?,
        f: Float,
        g: Float,
        h: Float,
        j: Float,
        k: Float,
        l: Float
    ) {
        if (sheep?.isSheared == true) {
            val minecraft = Minecraft.getInstance()
            if (minecraft.shouldEntityAppearGlowing(sheep)) {
                parentModel?.copyPropertiesTo(model)
                model.prepareMobModel(sheep, f, g, h)
                model.setupAnim(sheep, f, g, j, k, l)
                val vertexConsumer = multiBufferSource.getBuffer(RenderType.outline(SHEEP_FUR_LOCATION))
                model.renderToBuffer(
                    poseStack,
                    vertexConsumer,
                    i,
                    LivingEntityRenderer.getOverlayCoords(sheep, 0f),
                    0x00000000
                )
            }
        } else {
            val color = sheep?.colorArray
            coloredCutoutModelCopyLayerRender(
                parentModel,
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
                color?.rgb ?: 0x0
            )
        }
    }

    companion object {
        private val SHEEP_FUR_LOCATION = ResourceLocation.tryBuild("minecraft", "textures/entity/sheep/sheep_fur.png")
    }
}
