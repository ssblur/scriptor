package com.ssblur.scriptor.entity.renderers

import com.mojang.blaze3d.vertex.PoseStack
import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.color.CustomColors
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

        val mc = Minecraft.getInstance()
        checkNotNull(mc.level)
        val c = CustomColors.getColor(entity.color, mc.level!!.gameTime.toFloat())
        val r = c and 0xff0000 shr 16
        val g = c and 0x00ff00 shr 8
        val b = c and 0x0000ff

        val d = entity.deltaMovement
        val xd = d.x * tickDelta
        val yd = d.y * tickDelta
        val zd = d.z * tickDelta

        val level = Minecraft.getInstance().level
        if (level != null) {
        val particle = MagicParticleData.magic(r, g, b)
            level.addParticle(
                particle,
                entity.x + xd,
                entity.y + yd,
                entity.z + zd,
                0.0,
                0.0,
                0.0
            )
        }
    }
}
