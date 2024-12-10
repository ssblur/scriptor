package com.ssblur.scriptor.particle

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.particle.TextureSheetParticle

class MagicParticle : TextureSheetParticle {
    constructor(
        level: ClientLevel?,
        d: Double,
        e: Double,
        f: Double,
        xd: Double,
        yd: Double,
        zd: Double,
        size: Float,
        red: Float,
        green: Float,
        blue: Float,
        age: Float
    ) : super(level, d, e, f, 0.0, 0.0, 0.0) {
        this.xd = xd + (Math.random() / 20 - 0.025f)
        this.yd = yd + (Math.random() / 20 - 0.025f)
        this.zd = zd + (Math.random() / 20 - 0.025f)

        this.rCol = red / 255f
        this.gCol = green / 255f
        this.bCol = blue / 255f
        this.alpha = 1.0f
        this.gravity = 0f
        this.quadSize = size
        this.lifetime = ((Math.random() * 0.4 + 0.6) * 30 * age).toInt()
        this.xo = x
        this.yo = y
        this.zo = z
        this.hasPhysics = true

        setSize(0.01f, 0.01f)
    }

    constructor(
        data: MagicParticleData,
        level: ClientLevel?,
        d: Double,
        e: Double,
        f: Double,
        xd: Double,
        yd: Double,
        zd: Double
    ) : super(level, d, e, f, 0.0, 0.0, 0.0) {
        this.xd = xd + (Math.random() / 20 - 0.025f)
        this.yd = yd + (Math.random() / 20 - 0.025f)
        this.zd = zd + (Math.random() / 20 - 0.025f)

        this.rCol = data.r / 255f
        this.gCol = data.g / 255f
        this.bCol = data.b / 255f
        this.alpha = 1.0f
        this.gravity = 0f
        this.quadSize = 1.0f
        this.lifetime = ((Math.random() * 0.4 + 0.6) * 30 * age).toInt()
        this.xo = x
        this.yo = y
        this.zo = z
        this.hasPhysics = true

        setSize(0.01f, 0.01f)
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
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT
    }
}
