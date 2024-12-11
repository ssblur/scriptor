package com.ssblur.scriptor.entity

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.entity.renderers.ColorfulSheepRenderer
import com.ssblur.scriptor.entity.renderers.ScriptorProjectileRenderer
import dev.architectury.platform.Platform
import dev.architectury.registry.client.level.entity.EntityRendererRegistry
import dev.architectury.registry.level.entity.EntityAttributeRegistry
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.fabricmc.api.EnvType
import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.animal.Sheep
import net.minecraft.world.level.Level

object ScriptorEntities {
    val ENTITY_TYPES: DeferredRegister<EntityType<*>> =
        DeferredRegister.create(ScriptorMod.MOD_ID, Registries.ENTITY_TYPE)
    val PROJECTILE_TYPE: RegistrySupplier<EntityType<ScriptorProjectile?>> = ENTITY_TYPES.register(
        "projectile"
    ) {
        EntityType.Builder.of(
            { entityType, level -> ScriptorProjectile(entityType, level) },
            MobCategory.MISC
        )
            .clientTrackingRange(8)
            .sized(0.25f, 0.25f)
            .build("projectile")
    }
    val COLORFUL_SHEEP_TYPE: RegistrySupplier<EntityType<ColorfulSheep?>> = ENTITY_TYPES.register(
        "colorful_sheep"
    ) {
        EntityType.Builder.of(
            { entityType: EntityType<ColorfulSheep?>?, level: Level? -> ColorfulSheep(entityType, level) },
            MobCategory.CREATURE
        )
            .clientTrackingRange(10)
            .sized(0.9f, 1.3f)
            .build("colorful_sheep")
    }

    fun registerRenderers() {
        if (Platform.getEnv() == EnvType.CLIENT) {
            EntityRendererRegistry.register(PROJECTILE_TYPE) { ScriptorProjectileRenderer(it) }
            EntityRendererRegistry.register(COLORFUL_SHEEP_TYPE) { ColorfulSheepRenderer(it) }
        }
    }

    fun register() {
        ENTITY_TYPES.register()

        EntityAttributeRegistry.register(COLORFUL_SHEEP_TYPE) { Sheep.createAttributes() }

        registerRenderers()
    }
}
