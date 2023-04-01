package com.ssblur.scriptor.enchant;

import com.ssblur.scriptor.ScriptorMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.enchantment.Enchantment;

public class ScriptorEnchantments {
  public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ScriptorMod.MOD_ID, Registry.ENCHANTMENT_REGISTRY);

  public static final RegistrySupplier<ChargedEnchant> CHARGED = ENCHANTMENTS.register("charged", ChargedEnchant::new);

  public static void register() {
    ENCHANTMENTS.register();
  }
}
