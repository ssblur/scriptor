package com.ssblur.scriptor.particle

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.particle.TextureSheetParticle


class MagicParticle(
    data: MagicParticleData,
    level: ClientLevel,
    d: Double,
    e: Double,
    f: Double,
    xd: Double,
    yd: Double,
    zd: Double,
    spriteSet: SpriteSet
) : TextureSheetParticle(level, d, e, f, 0.0, 0.0, 0.0) {
    init {
        this.xd = xd + (Math.random() / 20 - 0.025f)
        this.yd = yd + (Math.random() / 20 - 0.025f)
        this.zd = zd + (Math.random() / 20 - 0.025f)
        this.rCol = data.r / 255f
        this.gCol = data.g / 255f
        this.bCol = data.b / 255f
        this.alpha = 1.0f
        this.gravity = 0f
        this.quadSize = 1.0f
        this.lifetime = ((Math.random() * 0.4 + 0.6) * 20).toInt()
        this.xo = x
        this.yo = y
        this.zo = z
        this.hasPhysics = true
        setSize(0.05f, 0.05f)
        setSpriteFromAge(spriteSet)
    }

    override fun tick() {
        super.tick()

        this.alpha *= 0.9f
    }

    override fun getQuadSize(p_217561_1_: Float): Float {
        var agescale = age.toFloat() / 30
        if (agescale > 1f) {
            agescale = 2 - agescale
        }

        quadSize = agescale * 0.5f
        return quadSize
    }

    override fun getLightColor(partialTicks: Float): Int {
        return 0xF000F0
    }

    override fun getRenderType(): ParticleRenderType {
        return ParticleRenderType.PARTICLE_SHEET_LIT
    }

    class Provider(val spriteSet: SpriteSet): ParticleProvider.Sprite<MagicParticleData> {
        override fun createParticle(
            data: MagicParticleData,
            level: ClientLevel,
            d: Double,
            e: Double,
            f: Double,
            g: Double,
            h: Double,
            i: Double
        ): TextureSheetParticle = MagicParticle(data, level, d, e, f, g, h, i, spriteSet)
    }
}
