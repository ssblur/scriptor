package com.ssblur.scriptor.entity.renderers

import com.mojang.blaze3d.vertex.PoseStack
import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.color.CustomColors
import com.ssblur.scriptor.color.CustomColors.splitIntoRGB
import com.ssblur.scriptor.entity.ScriptorProjectile
import com.ssblur.scriptor.particle.MagicParticleData
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import javax.annotation.ParametersAreNonnullByDefault

@ParametersAreNonnullByDefault
class ScriptorProjectileRenderer(context: EntityRendererProvider.Context) :
    EntityRenderer<ScriptorProjectile>(context) {
    override fun getTextureLocation(entity: ScriptorProjectile): ResourceLocation {
        return ScriptorMod.location("textures/item/tome.png")
    }

    override fun render(
        entity: ScriptorProjectile,
        yaw: Float,
        tickDelta: Float,
        poseStack: PoseStack,
        multiBufferSource: MultiBufferSource,
        lightLevel: Int
    ) {
        super.render(entity, yaw, tickDelta, poseStack, multiBufferSource, lightLevel)
        entity.setPos(entity.position().add(entity.deltaMovement.scale(tickDelta.toDouble())))

        val level = Minecraft.getInstance().level!!
        val (r, g, b) = CustomColors.getColor(entity.color, level.gameTime.toFloat()).splitIntoRGB()
        val d = entity.deltaMovement.scale(tickDelta.toDouble())

        level.addParticle(
            MagicParticleData.magic(r, g, b),
            entity.x + d.x,
            entity.y + d.y,
            entity.z + d.z,
            0.0,
            0.0,
            0.0
        )
    }
}
