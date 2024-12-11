package com.ssblur.scriptor.block

import com.ssblur.scriptor.ScriptorMod
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour

object ScriptorBlocks {
    val BLOCKS: DeferredRegister<Block> = DeferredRegister.create(ScriptorMod.MOD_ID, Registries.BLOCK)

    @JvmField
    val RUNE: RegistrySupplier<Block> = BLOCKS.register("rune") { RuneBlock() }
    @JvmField
    val LIGHT: RegistrySupplier<Block> = BLOCKS.register("light") { LightBlock() }
    @JvmField
    val CHALK: RegistrySupplier<Block> = BLOCKS.register("chalk") { ChalkBlock() }
    @JvmField
    val ENGRAVING: RegistrySupplier<Block> = BLOCKS.register("engraving") { EngravingBlock() }
    @JvmField
    val CASTING_LECTERN: RegistrySupplier<Block> = BLOCKS.register("casting_lectern") { CastingLecternBlock() }
    @JvmField
    val PHASED_BLOCK: RegistrySupplier<Block> = BLOCKS.register("phased_block") { PhasedBlock() }
    @JvmField
    val GENERATE: RegistrySupplier<Block> = BLOCKS.register("generate") { GenerateBlock() }

    val WHITE_MAGIC_BLOCK: RegistrySupplier<Block> = BLOCKS.register(
        "white_magic_block"
    ) { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE)) }
    val LIGHT_GRAY_MAGIC_BLOCK: RegistrySupplier<Block> = BLOCKS.register(
        "light_gray_magic_block"
    ) { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.LIGHT_GRAY)) }
    val GRAY_MAGIC_BLOCK: RegistrySupplier<Block> = BLOCKS.register(
        "gray_magic_block"
    ) { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.GRAY)) }
    val BLACK_MAGIC_BLOCK: RegistrySupplier<Block> = BLOCKS.register(
        "black_magic_block"
    ) { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.BLACK)) }
    val BROWN_MAGIC_BLOCK: RegistrySupplier<Block> = BLOCKS.register(
        "brown_magic_block"
    ) { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.BROWN)) }
    val RED_MAGIC_BLOCK: RegistrySupplier<Block> = BLOCKS.register(
        "red_magic_block"
    ) { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.RED)) }
    val ORANGE_MAGIC_BLOCK: RegistrySupplier<Block> = BLOCKS.register(
        "orange_magic_block"
    ) { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.ORANGE)) }
    val YELLOW_MAGIC_BLOCK: RegistrySupplier<Block> = BLOCKS.register(
        "yellow_magic_block"
    ) { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.YELLOW)) }
    val LIME_MAGIC_BLOCK: RegistrySupplier<Block> = BLOCKS.register(
        "lime_magic_block"
    ) { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.LIME)) }
    val GREEN_MAGIC_BLOCK: RegistrySupplier<Block> = BLOCKS.register(
        "green_magic_block"
    ) { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.GREEN)) }
    val CYAN_MAGIC_BLOCK: RegistrySupplier<Block> = BLOCKS.register(
        "cyan_magic_block"
    ) { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.CYAN)) }
    val LIGHT_BLUE_MAGIC_BLOCK: RegistrySupplier<Block> = BLOCKS.register(
        "light_blue_magic_block"
    ) { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.LIGHT_BLUE)) }
    val BLUE_MAGIC_BLOCK: RegistrySupplier<Block> = BLOCKS.register(
        "blue_magic_block"
    ) { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.BLUE)) }
    val PURPLE_MAGIC_BLOCK: RegistrySupplier<Block> = BLOCKS.register(
        "purple_magic_block"
    ) { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.PURPLE)) }
    val MAGENTA_MAGIC_BLOCK: RegistrySupplier<Block> = BLOCKS.register(
        "magenta_magic_block"
    ) { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.MAGENTA)) }
    val PINK_MAGIC_BLOCK: RegistrySupplier<Block> = BLOCKS.register(
        "pink_magic_block"
    ) { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.PINK)) }

    fun register() {
        BLOCKS.register()
    }
}
