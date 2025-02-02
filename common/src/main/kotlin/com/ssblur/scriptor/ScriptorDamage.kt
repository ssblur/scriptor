package com.ssblur.scriptor

import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageSources
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.Entity

@Suppress("unused")
object ScriptorDamage {
  val SACRIFICE: ResourceKey<DamageType> =
    ResourceKey.create(Registries.DAMAGE_TYPE, ScriptorMod.location("sacrifice"))
  val OVERLOAD: ResourceKey<DamageType> = ResourceKey.create(Registries.DAMAGE_TYPE, ScriptorMod.location("overload"))

  @JvmStatic
  fun sacrifice(entity: Entity): DamageSource {
    val level = entity.level()
    return level.registryAccess().registry(Registries.DAMAGE_TYPE).map { damageTypes: Registry<DamageType> ->
      DamageSource(
        damageTypes.getHolderOrThrow(
          SACRIFICE
        ), entity
      )
    }.orElseThrow()
  }

  @JvmStatic
  fun overload(entity: Entity): DamageSource? {
    val level = entity.level()
    return level.registryAccess().registry(Registries.DAMAGE_TYPE).map { damageTypes: Registry<DamageType> ->
      DamageSource(
        damageTypes.getHolderOrThrow(
          OVERLOAD
        ), entity
      )
    }
      .orElse(null)
  }

  @JvmStatic
  fun magic(entity: Entity, entity2: Entity?) =
    DamageSources(entity.level().registryAccess()).indirectMagic(entity, entity2)
}
