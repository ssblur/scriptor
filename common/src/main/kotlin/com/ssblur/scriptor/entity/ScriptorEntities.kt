package com.ssblur.scriptor.entity

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.unfocused.entity.EntityAttributes.registerEntityAttributes
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.animal.Sheep

object ScriptorEntities {
  val PROJECTILE_TYPE = ScriptorMod.registerEntity(
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
  val COLORFUL_SHEEP_TYPE = ScriptorMod.registerEntity(
    "colorful_sheep"
  ) {
    EntityType.Builder.of(
      { entityType, level -> ColorfulSheep(entityType, level) },
      MobCategory.CREATURE
    )
      .clientTrackingRange(10)
      .sized(0.9f, 1.3f)
      .build("colorful_sheep")
  }

  fun register() {
    ScriptorMod.registerEntityAttributes(COLORFUL_SHEEP_TYPE) { Sheep.createAttributes() }
  }
}
