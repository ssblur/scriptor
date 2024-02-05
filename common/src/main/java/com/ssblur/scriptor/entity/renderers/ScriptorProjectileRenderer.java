package com.ssblur.scriptor.entity.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.entity.ScriptorProjectile;
import com.ssblur.scriptor.particle.MagicParticleData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
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
    entity.setPos(entity.position().add(entity.getDeltaMovement().scale(tickDelta)));

    int c = entity.getColor();
    int r, g, b;
    if(c == -1) {
      var mc = Minecraft.getInstance();
      assert mc.level != null;
      float time = mc.level.getGameTime() + mc.getDeltaFrameTime();
      time %= 40;
      time /= 40;
      var color = Color.getHSBColor(time, 1f, 0.5f);
      r = color.getRed();
      g = color.getGreen();
      b = color.getBlue();
    } else {
      r = (c & 0xff0000) >> 16;
      g = (c & 0x00ff00) >> 8;
      b = c & 0x0000ff;
    }

    Vec3 d = entity.getDeltaMovement();
    double xd = d.x * tickDelta;
    double yd = d.y * tickDelta;
    double zd = d.z * tickDelta;

    var level = Minecraft.getInstance().level;
    if (level != null)
      level.addParticle(
        MagicParticleData.magic(r, g, b),
        entity.getX() + xd,
        entity.getY() + yd,
        entity.getZ() + zd,
        0,
        0,
        0
      );
  }
}
