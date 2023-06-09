package com.ssblur.scriptor.entity.renderers;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.vertex.PoseStack;
import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.entity.ScriptorProjectile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
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
    entity.setPos(entity.position().add(entity.getDeltaMovement().scale(tickDelta)));

    int c = entity.getColor();
    float r, g, b;
    if(c == -1) {
      var mc = Minecraft.getInstance();
      assert mc.level != null;
      float time = mc.level.getGameTime() + mc.getDeltaFrameTime();
      time %= 40;
      time /= 40;
      var color = Color.getHSBColor(time, 1f, 0.5f);
      r = ((float) color.getRed()) / 255;
      g = ((float) color.getGreen()) / 255;
      b = ((float) color.getBlue()) / 255;
    } else {
      r = ((float) ((c & 0xff0000) >> 16)) / 255;
      g = ((float) ((c & 0x00ff00) >> 8)) / 255;
      b = ((float) (c & 0x0000ff)) / 255;
    }
    Vector3f color = new Vector3f(r, g, b);

    Vec3 d = entity.getDeltaMovement();
    double xd = d.x * tickDelta;
    double yd = d.y * tickDelta;
    double zd = d.z * tickDelta;

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
