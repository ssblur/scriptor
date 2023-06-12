package com.ssblur.scriptor.damage;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.item.Spellbook;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

import java.io.IOException;

public class ScriptorDamage {
  public static final DeferredRegister<DamageType> DAMAGE_TYPES = DeferredRegister.create(ScriptorMod.MOD_ID, Registries.DAMAGE_TYPE);

  public static final ResourceKey<DamageType> SACRIFICE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ScriptorMod.MOD_ID, "sacrifice"));
  public static final ResourceKey<DamageType> OVERLOAD = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ScriptorMod.MOD_ID, "overload"));

  public static DamageSource sacrifice(Entity entity) {
    try(var level = entity.level()) {
      return level.registryAccess().registry(Registries.DAMAGE_TYPE).map(
        damageTypes -> new DamageSource(damageTypes.getHolderOrThrow(SACRIFICE), entity)
      ).orElse(null);
    } catch (IOException e) {
      return null;
    }
  }

  public static DamageSource overload(Entity entity) {
    try(var level = entity.level()) {
      return level.registryAccess().registry(Registries.DAMAGE_TYPE).map(
        damageTypes -> new DamageSource(damageTypes.getHolderOrThrow(OVERLOAD), entity)
      ).orElse(null);
    } catch (IOException e) {
      return null;
    }
  }

  public static DamageSource magic(Entity entity, Entity entity2) {
    try(var level = entity.level()) {
      return new DamageSources(level.registryAccess()).indirectMagic(entity, entity2);
    } catch (IOException e) {
      return null;
    }
  }
}
