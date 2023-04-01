package com.ssblur.scriptor.enchant;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.List;

public class ChargedEnchant extends Enchantment {
  static EquipmentSlot[] slots = new EquipmentSlot[] {
    EquipmentSlot.MAINHAND
  };

  public ChargedEnchant() {
    super(Rarity.COMMON, EnchantmentCategory.WEAPON, slots);
  }

  @Override
  public float getDamageBonus(int i, MobType mobType) {
    return 3.0F;
  }

  @Override
  public void doPostAttack(LivingEntity owner, Entity entity, int i) {
    chargeItem(owner.getMainHandItem(), i - 1);
  }

  public static void chargeItem(ItemStack stack, int strength) {
    ListTag list = new ListTag();
    for(var item: stack.getEnchantmentTags()) {
      if(item instanceof CompoundTag tag) {
        if(!tag.getString("id").equals("scriptor:charged"))
          list.add(tag);
      }
    }
    stack.getOrCreateTag().put("Enchantments", list);

    if(strength > 0)
      stack.enchant(ScriptorEnchantments.CHARGED.get(), strength);
  }
}
