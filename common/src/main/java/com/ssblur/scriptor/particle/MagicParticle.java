package com.ssblur.scriptor.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;

public class MagicParticle extends TextureSheetParticle {

  public MagicParticle(
    ClientLevel level,
    double d,
    double e,
    double f,
    double xd,
    double yd,
    double zd,
    float size,
    float red,
    float green,
    float blue,
    float age
  ) {
    super(level, d, e, f, 0, 0, 0);

    this.xd = xd + (Math.random() / 20 - 0.025f);
    this.yd = yd + (Math.random() / 20 - 0.025f);
    this.zd = zd + (Math.random() / 20 - 0.025f);

    this.rCol = red / 255f;
    this.gCol = green / 255f;
    this.bCol = blue / 255f;
    this.alpha = 1.0f;
    this.gravity = 0;
    this.quadSize = size;
    this.lifetime = (int) ((Math.random() * 0.4d + 0.6d) * 30 * age);
    this.xo = x;
    this.yo = y;
    this.zo = z;
    this.hasPhysics = true;

    setSize(0.01f, 0.01f);
  }

  @Override
  public void tick() {
    super.tick();

    this.alpha *= 0.9f;
  }

  @Override
  public float getQuadSize(float p_217561_1_) {
    float agescale = (float) age / 30;
    if (agescale > 1F) {
      agescale = 2 - agescale;
    }

    quadSize = agescale * 0.5F;
    return quadSize;
  }

  @Override
  protected int getLightColor(float partialTicks) {
    return 0xF000F0;
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }
}
