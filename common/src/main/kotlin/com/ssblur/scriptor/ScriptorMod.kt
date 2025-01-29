package com.ssblur.scriptor

import com.ssblur.scriptor.advancement.ScriptorAdvancements
import com.ssblur.scriptor.block.ScriptorBlocks
import com.ssblur.scriptor.blockentity.ScriptorBlockEntities
import com.ssblur.scriptor.commands.DebugCommand
import com.ssblur.scriptor.commands.DumpDictionaryCommand
import com.ssblur.scriptor.commands.DumpWordCommand
import com.ssblur.scriptor.config.ScriptorConfig
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.effect.ScriptorEffects
import com.ssblur.scriptor.entity.ScriptorEntities
import com.ssblur.scriptor.events.ScriptorEvents
import com.ssblur.scriptor.feature.ScriptorFeatures
import com.ssblur.scriptor.item.ScriptorItems
import com.ssblur.scriptor.item.ScriptorLoot
import com.ssblur.scriptor.item.ScriptorTabs
import com.ssblur.scriptor.network.client.ScriptorNetworkS2C
import com.ssblur.scriptor.network.server.ScriptorNetworkC2S
import com.ssblur.scriptor.particle.ScriptorParticles
import com.ssblur.scriptor.recipe.ScriptorRecipes
import com.ssblur.scriptor.resources.ScriptorResources
import com.ssblur.scriptor.trade.ScriptorTrades
import com.ssblur.unfocused.ModInitializer
import com.ssblur.unfocused.command.CommandRegistration.registerCommand
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import org.apache.logging.log4j.LogManager

@Suppress("unused")
object ScriptorMod: ModInitializer("scriptor") {
    const val MOD_ID = "scriptor"
    val LOGGER = LogManager.getLogger(MOD_ID)!!

    // Please don't mess with this, I'm not adding anticheat but it's no fun );
    var COMMUNITY_MODE = false

    fun registerCommands() {
        registerCommand { dispatcher, registry, selection ->
            DumpDictionaryCommand.register(
                dispatcher,
                registry,
                selection
            )
        }
        registerCommand  { dispatcher, registry, selection ->
            DumpWordCommand.register(
                dispatcher,
                registry,
                selection
            )
        }
        registerCommand  { dispatcher, registry, selection ->
            DebugCommand.register(
                dispatcher,
                registry,
                selection
            )
        }
    }

    fun init() {
        ScriptorTabs.register()
        ScriptorAdvancements.register()
        ScriptorBlocks.register()
        ScriptorBlockEntities.register()
        ScriptorItems.register()
        ScriptorEntities.register()
        ScriptorEvents.register()
        ScriptorEffects.register()
        ScriptorTrades.register()
        ScriptorRecipes.register()
        ScriptorParticles.register()
        ScriptorLoot.register()
        ScriptorConfig.register()
        ScriptorFeatures.register()
        ScriptorDataComponents.register()
        ScriptorNetworkC2S.register()
        ScriptorNetworkS2C.register()
        ScriptorResources.register()

        registerCommands()
    }

    @Environment(EnvType.CLIENT)
    fun clientInit() {
        ScriptorEntities.registerRenderers()
        ScriptorBlockEntities.registerRenderers()
    }
}