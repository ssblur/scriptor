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
    if(owner.level.isClientSide) return;

    var tag = owner.getMainHandItem().getOrCreateTagElement("scriptor");
    long lastDrained = tag.getLong("chargeLastDrained");
    if(lastDrained < owner.level.getGameTime()) {
      tag.putLong("chargeLastDrained", owner.level.getGameTime());
      chargeItem(owner.getMainHandItem(), i - 1);
    }
  }

  public static void chargeItem(ItemStack stack, int strength) {
    ListTag list = new ListTag();
    for(var item: stack.getEnchantmentTags()) {
      if(item instanceof CompoundTag tag) {
        if(!tag.getString("id").equals("scriptor:charged"))
          list.add(tag);
      }
    }

    CompoundTag tag = stack.getTag();
    if(list.size() > 0)
      stack.getOrCreateTag().put("Enchantments", list);
    else if(tag != null)
      tag.remove("Enchantments");

    if(strength > 0)
      stack.enchant(ScriptorEnchantments.CHARGED.get(), strength);
    else if(tag != null && tag.contains("scriptor") && tag.getCompound("scriptor").contains("chargeLastDrained")) {
      CompoundTag scriptor = tag.getCompound("scriptor");
      scriptor.remove("chargeLastDrained");
      if(scriptor.size() == 0)
        tag.remove("scriptor");
    }

    if(tag != null && tag.size() == 0)
      stack.setTag(null);
  }

  public boolean isTreasureOnly() {
    return true;
  }
}
