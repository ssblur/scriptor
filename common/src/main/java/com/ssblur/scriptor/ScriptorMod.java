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
import com.ssblur.scriptor.feature.ScriptorFeatures;
import com.ssblur.scriptor.gamerules.ScriptorGameRules;
import com.ssblur.scriptor.item.ScriptorItems;
import com.ssblur.scriptor.loot.ScriptorLoot;
import com.ssblur.scriptor.particle.ScriptorParticles;
import com.ssblur.scriptor.recipe.ScriptorRecipes;
import com.ssblur.scriptor.tabs.ScriptorTabs;
import com.ssblur.scriptor.trade.ScriptorTrades;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.registry.registries.RegistrarManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class ScriptorMod {
  public static final String MOD_ID = "scriptor";
  public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
  public static final Supplier<RegistrarManager> REGISTRIES = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

  // Please don't mess with this, I'm not adding anticheat but it's no fun );
  public static boolean COMMUNITY_MODE = false;

  public static void registerCommands() {
    CommandRegistrationEvent.EVENT.register(DumpDictionaryCommand::register);
    CommandRegistrationEvent.EVENT.register(DumpWordCommand::register);
  }

  public static void init() {
    ScriptorTabs.register();
    ScriptorBlocks.register();
    ScriptorBlockEntities.register();
    ScriptorItems.register();
    ScriptorEntities.register();
    ScriptorEvents.register();
    ScriptorEffects.register();
    ScriptorEnchantments.register();
    ScriptorTrades.register();
    ScriptorRecipes.register();
    ScriptorParticles.register();
    ScriptorLoot.register();
    ScriptorGameRules.register();
    ScriptorFeatures.register();

    registerCommands();
  }
}