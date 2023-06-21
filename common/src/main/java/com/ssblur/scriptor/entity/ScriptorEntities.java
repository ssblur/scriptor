package com.ssblur.scriptor.entity;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.entity.renderers.ColorfulSheepRenderer;
import com.ssblur.scriptor.entity.renderers.ScriptorProjectileRenderer;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.EnvType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Sheep;

public class ScriptorEntities {
  public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ScriptorMod.MOD_ID, Registries.ENTITY_TYPE);
  public static final RegistrySupplier<EntityType<ScriptorProjectile>> PROJECTILE_TYPE =
    ENTITY_TYPES.register(
    "projectile",
      () -> EntityType.Builder.of(
        ScriptorProjectile::new,
        MobCategory.MISC
      )
      .clientTrackingRange(8)
      .sized(0.25F, 0.25F)
      .build("projectile")
    );
  public static final RegistrySupplier<EntityType<ColorfulSheep>> COLORFUL_SHEEP_TYPE =
    ENTITY_TYPES.register(
    "colorful_sheep",
      () -> EntityType.Builder.of(
        ColorfulSheep::new,
        MobCategory.CREATURE
      )
      .clientTrackingRange(10)
      .sized(0.9F, 1.3F)
      .build("colorful_sheep")
    );

  public static void registerRenderers() {
    if(Platform.getEnv() == EnvType.CLIENT) {
      EntityRendererRegistry.register(PROJECTILE_TYPE, ScriptorProjectileRenderer::new);
      EntityRendererRegistry.register(COLORFUL_SHEEP_TYPE, ColorfulSheepRenderer::new);
    }
  }

  public static void register() {
    ENTITY_TYPES.register();

    EntityAttributeRegistry.register(COLORFUL_SHEEP_TYPE, Sheep::createAttributes);

    registerRenderers();
  }
}
