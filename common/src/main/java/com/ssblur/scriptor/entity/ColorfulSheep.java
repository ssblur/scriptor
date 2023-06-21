package com.ssblur.scriptor.entity;

import com.ssblur.scriptor.helpers.CustomColors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.HashMap;

public class ColorfulSheep extends Sheep {
  static HashMap<DyeColor, Item> WOOL = new HashMap<>();
  static {
    WOOL.put(DyeColor.BLACK, Items.BLACK_WOOL);
    WOOL.put(DyeColor.BLUE, Items.BLUE_WOOL);
    WOOL.put(DyeColor.BROWN, Items.BROWN_WOOL);
    WOOL.put(DyeColor.CYAN, Items.CYAN_WOOL);
    WOOL.put(DyeColor.GRAY, Items.GRAY_WOOL);
    WOOL.put(DyeColor.GREEN, Items.GREEN_WOOL);
    WOOL.put(DyeColor.LIGHT_BLUE, Items.LIGHT_BLUE_WOOL);
    WOOL.put(DyeColor.LIGHT_GRAY, Items.LIGHT_GRAY_WOOL);
    WOOL.put(DyeColor.LIME, Items.LIME_WOOL);
    WOOL.put(DyeColor.MAGENTA, Items.MAGENTA_WOOL);
    WOOL.put(DyeColor.ORANGE, Items.ORANGE_WOOL);
    WOOL.put(DyeColor.PINK, Items.PINK_WOOL);
    WOOL.put(DyeColor.PURPLE, Items.PURPLE_WOOL);
    WOOL.put(DyeColor.RED, Items.RED_WOOL);
    WOOL.put(DyeColor.WHITE, Items.WHITE_WOOL);
    WOOL.put(DyeColor.YELLOW, Items.YELLOW_WOOL);
  }

  private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(ColorfulSheep.class, EntityDataSerializers.INT);

  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(DATA_COLOR, 0xFFFFFF);
  }

  public ColorfulSheep(EntityType<? extends ColorfulSheep> entityType, Level level) {
    super(entityType, level);
  }

  public void setColor(int color) {
    this.entityData.set(DATA_COLOR, color);
  }

  public DyeColor getColor() {
    return CustomColors.getDyeColor(this.entityData.get(DATA_COLOR), level().getGameTime());
  }

  public Color getColorArray() {
    return new Color(getRGBA());
  }

  public int getRGBA() {
    return CustomColors.getColor(this.entityData.get(DATA_COLOR), level().getGameTime());
  }

  public void setColor(DyeColor dyeColor) {
    this.entityData.set(DATA_COLOR, dyeColor.getFireworkColor());
  }

  public void shear(SoundSource soundSource) {
    this.level().playSound(null, this, SoundEvents.SHEEP_SHEAR, soundSource, 1.0F, 1.0F);
    this.setSheared(true);
    int i = 1 + this.random.nextInt(3);

    for(int j = 0; j < i; ++j) {
      ItemEntity itemEntity = this.spawnAtLocation(WOOL.get(getColor()), 1);
      if (itemEntity != null)
        itemEntity.setDeltaMovement(
          new Vec3(
            (this.random.nextFloat() - this.random.nextFloat()) * 0.05F,
            this.random.nextFloat() * 0.05F,
            this.random.nextFloat() - this.random.nextFloat() * 0.05F
          )
        );
    }
  }

  public void addAdditionalSaveData(CompoundTag compoundTag) {
    super.addAdditionalSaveData(compoundTag);
    compoundTag.putInt("scriptor:color", this.entityData.get(DATA_COLOR));
  }

  public void readAdditionalSaveData(CompoundTag compoundTag) {
    super.readAdditionalSaveData(compoundTag);
    this.setColor(compoundTag.getInt("scriptor:color"));
  }

  @Nullable
  @Override
  public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
    this.setColor(0xFFFFFF);
    return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
  }
}
