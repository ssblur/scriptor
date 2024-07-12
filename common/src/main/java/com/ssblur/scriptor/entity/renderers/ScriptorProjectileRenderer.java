package com.ssblur.scriptor.entity.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.color.CustomColors;
import com.ssblur.scriptor.entity.ScriptorProjectile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;
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

    var mc = Minecraft.getInstance();
    assert mc.level != null;
    int c = CustomColors.getColor(entity.getColor(), mc.level.getGameTime());
    int r, g, b;
    r = (c & 0xff0000) >> 16;
    g = (c & 0x00ff00) >> 8;
    b = c & 0x0000ff;

    Vec3 d = entity.getDeltaMovement();
    double xd = d.x * tickDelta;
    double yd = d.y * tickDelta;
    double zd = d.z * tickDelta;

    var level = Minecraft.getInstance().level;
    if (level != null) {
//      var particle = MagicParticleData.magic(r, g, b);
      // TODO: fix magic particles
      var particle = new DustParticleOptions(new Vector3f(((float)r)/255f, ((float)g)/255f, ((float)b)/255f), 0.5f);
      level.addParticle(
        particle,
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
