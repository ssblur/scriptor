package com.ssblur.scriptor;

import com.google.common.base.Suppliers;
import com.ssblur.scriptor.block.ScriptorBlocks;
import com.ssblur.scriptor.blockentity.ScriptorBlockEntities;
import com.ssblur.scriptor.commands.DumpDictionaryCommand;
import com.ssblur.scriptor.commands.DumpWordCommand;
import com.ssblur.scriptor.effect.ScriptorEffects;
import com.ssblur.scriptor.enchant.ScriptorEnchantments;
import com.ssblur.scriptor.entity.ScriptorEntities;
import com.ssblur.scriptor.events.ScriptorEvents;
import com.ssblur.scriptor.helpers.ConfigHelper;
import com.ssblur.scriptor.item.ScriptorItems;
import com.ssblur.scriptor.recipe.ScriptorRecipes;
import com.ssblur.scriptor.trade.ScriptorTrades;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.RegistrarManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class ScriptorMod {
  public static final String MOD_ID = "scriptor";
  public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
  public static final Supplier<RegistrarManager> REGISTRIES = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));
  public static final CreativeTabRegistry.TabSupplier SCRIPTOR_TAB = CreativeTabRegistry.create(new ResourceLocation(MOD_ID, "scriptor_tab"), () ->
    new ItemStack(ScriptorItems.TOME_TIER4.get()));

  public static void registerCommands() {
    CommandRegistrationEvent.EVENT.register(DumpDictionaryCommand::register);
    CommandRegistrationEvent.EVENT.register(DumpWordCommand::register);
  }

  public static void init() {
    ScriptorBlocks.register();
    ScriptorBlockEntities.register();
    ScriptorItems.register();
    ScriptorEntities.register();
    ScriptorEvents.register();
    ScriptorEffects.register();
    ScriptorEnchantments.register();
    ScriptorTrades.register();
    ScriptorRecipes.register();

    ConfigHelper.getConfig();

    registerCommands();
  }
}