package com.ssblur.scriptor.entity

import com.ssblur.scriptor.color.CustomColors
import com.ssblur.scriptor.color.interfaces.Colorable
import com.ssblur.scriptor.mixin.SheepAccessor
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.tags.ItemTags
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.entity.SpawnGroupData
import net.minecraft.world.entity.ai.goal.*
import net.minecraft.world.entity.animal.Sheep
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor
import net.minecraft.world.phys.Vec3
import java.awt.Color
import java.util.*

class ColorfulSheep(entityType: EntityType<out ColorfulSheep>, level: Level): Sheep(entityType, level),
  Colorable {
  override fun defineSynchedData(builder: SynchedEntityData.Builder) {
    super.defineSynchedData(builder)
    builder.define(DATA_COLOR, 0xFFFFFF)
  }

  override fun setColor(color: Int) {
    entityData.set(DATA_COLOR, color)
  }

  override fun getColor(): DyeColor {
    return CustomColors.getDyeColor(entityData.get(DATA_COLOR), level().gameTime.toFloat())
  }

  val colorArray: Color
    get() = Color(rGBA)

  val rGBA: Int
    get() = CustomColors.getColor(entityData.get(DATA_COLOR), level().gameTime.toFloat())

  override fun setColor(dyeColor: DyeColor) {
    entityData.set(DATA_COLOR, dyeColor.fireworkColor)
  }

  override fun shear(soundSource: SoundSource) {
    level().playSound(null, this, SoundEvents.SHEEP_SHEAR, soundSource, 1.0f, 1.0f)
    this.isSheared = true
    val i = 1 + random.nextInt(3)

    for (j in 0 until i) {
      val itemEntity = WOOL[color]?.let { this.spawnAtLocation(it, 1) }
      if (itemEntity != null) itemEntity.deltaMovement = Vec3(
        ((random.nextFloat() - random.nextFloat()) * 0.05f).toDouble(),
        (random.nextFloat() * 0.05f).toDouble(),
        (random.nextFloat() - random.nextFloat() * 0.05f).toDouble()
      )
    }
  }

  override fun addAdditionalSaveData(compoundTag: CompoundTag) {
    super.addAdditionalSaveData(compoundTag)
    compoundTag.putInt("scriptor:color", entityData.get(DATA_COLOR))
  }

  override fun readAdditionalSaveData(compoundTag: CompoundTag) {
    super.readAdditionalSaveData(compoundTag)
    this.setColor(compoundTag.getInt("scriptor:color"))
  }

  override fun finalizeSpawn(
    serverLevelAccessor: ServerLevelAccessor,
    difficultyInstance: DifficultyInstance,
    mobSpawnType: MobSpawnType,
    spawnGroupData: SpawnGroupData?
  ): SpawnGroupData? {
    this.setColor(0xFFFFFF)
    return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData)
  }

  override fun registerGoals() {
    val eatBlockGoal = EatBlockGoal(this)
    @Suppress("CAST_NEVER_SUCCEEDS")
    (this as SheepAccessor).setEatBlockGoal(eatBlockGoal)
    goalSelector.addGoal(0, FloatGoal(this))
    goalSelector.addGoal(1, PanicGoal(this, 1.25))
    goalSelector.addGoal(2, BreedGoal(this, 1.0, Sheep::class.java))
    goalSelector.addGoal(2, BreedGoal(this, 1.0))
    goalSelector.addGoal(
      3,
      TemptGoal(this, 1.1, { itemStack: ItemStack -> itemStack.`is`(ItemTags.SHEEP_FOOD) }, false)
    )
    goalSelector.addGoal(4, FollowParentGoal(this, 1.1))
    goalSelector.addGoal(5, eatBlockGoal)
    goalSelector.addGoal(6, WaterAvoidingRandomStrollGoal(this, 1.0))
    goalSelector.addGoal(7, LookAtPlayerGoal(this, Player::class.java, 6.0f))
    goalSelector.addGoal(8, RandomLookAroundGoal(this))
  }

  companion object {
    var WOOL: EnumMap<DyeColor, Item> = EnumMap(DyeColor::class.javaObjectType)

    init {
      WOOL[DyeColor.BLACK] = Items.BLACK_WOOL
      WOOL[DyeColor.BLUE] = Items.BLUE_WOOL
      WOOL[DyeColor.BROWN] = Items.BROWN_WOOL
      WOOL[DyeColor.CYAN] = Items.CYAN_WOOL
      WOOL[DyeColor.GRAY] = Items.GRAY_WOOL
      WOOL[DyeColor.GREEN] = Items.GREEN_WOOL
      WOOL[DyeColor.LIGHT_BLUE] = Items.LIGHT_BLUE_WOOL
      WOOL[DyeColor.LIGHT_GRAY] = Items.LIGHT_GRAY_WOOL
      WOOL[DyeColor.LIME] = Items.LIME_WOOL
      WOOL[DyeColor.MAGENTA] = Items.MAGENTA_WOOL
      WOOL[DyeColor.ORANGE] = Items.ORANGE_WOOL
      WOOL[DyeColor.PINK] = Items.PINK_WOOL
      WOOL[DyeColor.PURPLE] = Items.PURPLE_WOOL
      WOOL[DyeColor.RED] = Items.RED_WOOL
      WOOL[DyeColor.WHITE] = Items.WHITE_WOOL
      WOOL[DyeColor.YELLOW] = Items.YELLOW_WOOL
    }

    private val DATA_COLOR: EntityDataAccessor<Int> = SynchedEntityData.defineId(
      ColorfulSheep::class.java, EntityDataSerializers.INT
    )
  }
}
