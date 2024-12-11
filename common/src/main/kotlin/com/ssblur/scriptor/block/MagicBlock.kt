package com.ssblur.scriptor.block

import dev.architectury.platform.Platform
import dev.architectury.registry.client.rendering.RenderTypeRegistry
import net.fabricmc.api.EnvType
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour.StateArgumentPredicate
import net.minecraft.world.level.block.state.BlockBehaviour.StatePredicate
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument

class MagicBlock(properties: Properties) : Block(
    properties
        .instrument(NoteBlockInstrument.HAT)
        .strength(0.3f)
        .sound(SoundType.GLASS)
        .noOcclusion()
        .isValidSpawn(SPAWN_NEVER)
        .isRedstoneConductor(NEVER)
        .isSuffocating(NEVER)
        .isViewBlocking(NEVER)
) {
    init {
        if (!Platform.isForgeLike() && Platform.getEnv() == EnvType.CLIENT)
            RenderTypeRegistry.register(RenderType.translucent(), this)
    }

    companion object {
        private val SPAWN_NEVER =
            StateArgumentPredicate { a: BlockState?, b: BlockGetter?, c: BlockPos?, d: EntityType<*>? -> false }
        private val NEVER = StatePredicate { a: BlockState?, b: BlockGetter?, c: BlockPos? -> false }
    }
}
