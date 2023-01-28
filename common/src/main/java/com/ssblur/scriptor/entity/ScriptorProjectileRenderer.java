package com.ssblur.scriptor.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.ssblur.scriptor.ScriptorMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;
@ParametersAreNonnullByDefault
public class ScriptorProjectileRenderer extends EntityRenderer<ScriptorProjectile> {
  public ScriptorProjectileRenderer(EntityRendererProvider.Context context) {
    super(context);
  }

  @Override
  public ResourceLocation getTextureLocation(ScriptorProjectile entity) {
    return new ResourceLocation(ScriptorMod.MOD_ID, "textures/item/tome.png");
  }

  @Override
  public void render(ScriptorProjectile entity, float yaw, float tickDelta, PoseStack poseStack, MultiBufferSource multiBufferSource, int lightLevel) {
    super.render(entity, yaw, tickDelta, poseStack, multiBufferSource, lightLevel);

    int c = entity.getColor();
    float r = ((float) ((c & 0xff0000) >> 16)) / 255;
    float g = ((float) ((c & 0x00ff00) >> 8)) / 255;
    float b = ((float) (c & 0x0000ff)) / 255;
    Vector3f color = new Vector3f(r, g, b);

    Vec3 d = entity.getDeltaMovement();
    double xd = d.x * tickDelta;
    double yd = d.y * tickDelta;
    double zd = d.z * tickDelta;

    if(entity.tickCount > 3) {
      var level = Minecraft.getInstance().level;
      if (level != null)
        level.addParticle(
          new DustParticleOptions(color, 1.0f),
          entity.getX() + xd,
          entity.getY() + yd,
          entity.getZ() + zd,
          0,
          0,
          0
        );
    }
  }
}
