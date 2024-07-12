package com.ssblur.scriptor.enchant;

import net.minecraft.nbt.ListTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import javax.annotation.Nullable;
import java.util.Optional;

public class ChargedEnchant extends Enchantment {
  static EquipmentSlot[] slots = new EquipmentSlot[] {
    EquipmentSlot.MAINHAND
  };

  public ChargedEnchant() {
    super(new EnchantmentDefinition(
      ItemTags.WEAPON_ENCHANTABLE,
      Optional.empty(),
      0,
      0,
      new Cost(0, 0),
      new Cost(0, 0),
      0,
      FeatureFlagSet.of(),
      new EquipmentSlot[] {EquipmentSlot.MAINHAND}
    ));
  }

  @Override
  public float getDamageBonus(int i, @Nullable EntityType<?> mobType) {
    return 3.0F;
  }

  @Override
  public void doPostAttack(LivingEntity owner, Entity entity, int i) {
    var level = owner.level();
    if (level.isClientSide) return;

    if(owner instanceof Player player) {
      if(!player.getCooldowns().isOnCooldown(owner.getMainHandItem().getItem()))
        chargeItem(owner.getMainHandItem(), i - 1);
      player.getCooldowns().addCooldown(owner.getMainHandItem().getItem(), 10);
    }
  }

  public static void chargeItem(ItemStack stack, int strength) {
    ListTag list = new ListTag();
    stack.enchant(ScriptorEnchantments.CHARGED.get(), strength);
  }

  @Override
  public boolean isTreasureOnly() {
    return true;
  }
}
