package com.ssblur.scriptor

import com.ssblur.scriptor.block.ScriptorBlocks
import com.ssblur.scriptor.blockentity.ScriptorBlockEntities.CASTING_LECTERN
import com.ssblur.scriptor.blockentity.ScriptorBlockEntities.CHALK
import com.ssblur.scriptor.blockentity.ScriptorBlockEntities.ENGRAVING
import com.ssblur.scriptor.blockentity.ScriptorBlockEntities.LIGHT
import com.ssblur.scriptor.blockentity.ScriptorBlockEntities.PHASED_BLOCK
import com.ssblur.scriptor.blockentity.ScriptorBlockEntities.RUNE
import com.ssblur.scriptor.blockentity.renderers.*
import com.ssblur.scriptor.color.CustomColors.getColor
import com.ssblur.scriptor.entity.ScriptorEntities.COLORFUL_SHEEP_TYPE
import com.ssblur.scriptor.entity.ScriptorEntities.PROJECTILE_TYPE
import com.ssblur.scriptor.entity.renderers.ColorfulSheepRenderer
import com.ssblur.scriptor.entity.renderers.ScriptorProjectileRenderer
import com.ssblur.scriptor.events.ScriptorClientEvents
import com.ssblur.scriptor.item.ScriptorItems
import com.ssblur.scriptor.particle.MagicParticle
import com.ssblur.scriptor.particle.ScriptorParticles.MAGIC
import com.ssblur.scriptor.screen.screen.ScriptorScreens
import com.ssblur.unfocused.extension.BlockExtension.renderType
import com.ssblur.unfocused.helper.ColorHelper.registerColor
import com.ssblur.unfocused.rendering.BlockEntityRendering.registerBlockEntityRenderer
import com.ssblur.unfocused.rendering.EntityRendering.registerEntityRenderer
import com.ssblur.unfocused.rendering.ParticleFactories.registerFactory
import net.minecraft.client.renderer.RenderType
import net.minecraft.world.item.ItemStack

object ScriptorClient {
  fun clientInit() {
    ScriptorItems.BOUND_SWORD.then {
      it.registerColor { itemStack: ItemStack?, t: Int ->
        if (t == 1) getColor(
          itemStack!!
        ) else -0x1
      }
    }
    ScriptorItems.BOUND_AXE.then {
      it.registerColor { itemStack: ItemStack?, t: Int ->
        if (t == 1) getColor(
          itemStack!!
        ) else -0x1
      }
    }
    ScriptorItems.BOUND_SHOVEL.then {
      it.registerColor { itemStack: ItemStack?, t: Int ->
        if (t == 1) getColor(
          itemStack!!
        ) else -0x1
      }
    }
    ScriptorItems.BOUND_PICKAXE.then {
      it.registerColor { itemStack: ItemStack?, t: Int ->
        if (t == 1) getColor(
          itemStack!!
        ) else -0x1
      }
    }
    ScriptorItems.ETHEREAL_FIG.then {
      it.registerColor { itemStack: ItemStack?, t: Int ->
        if (t == 1) getColor(
          itemStack!!
        ) else -0x1
      }
    }

    ScriptorMod.registerEntityRenderer(PROJECTILE_TYPE) { ScriptorProjectileRenderer(it) }
    ScriptorMod.registerEntityRenderer(COLORFUL_SHEEP_TYPE) { ColorfulSheepRenderer(it) }

    MAGIC.registerFactory(MagicParticle::Provider)

    ScriptorBlocks.MAGIC_BLOCKS.forEach { block -> block.then { it.renderType(RenderType.translucent()) } }
    ScriptorBlocks.HIGHLIGHT_MODEL.then { it.renderType(RenderType.translucent()) }

    ScriptorMod.registerBlockEntityRenderer(RUNE) { RuneBlockEntityRenderer(it) }
    ScriptorMod.registerBlockEntityRenderer(CHALK) { ChalkBlockEntityRenderer(it) }
    ScriptorMod.registerBlockEntityRenderer(ENGRAVING) { EngravingBlockEntityRenderer(it) }
    ScriptorMod.registerBlockEntityRenderer(CASTING_LECTERN) { CastingLecternBlockEntityRenderer(it) }
    ScriptorMod.registerBlockEntityRenderer(LIGHT) { LightBlockEntityRenderer(it) }
    ScriptorMod.registerBlockEntityRenderer(PHASED_BLOCK) { PhasedBlockBlockEntityRenderer(it) }

    ScriptorClientEvents.init()
    ScriptorScreens.register()
  }
}