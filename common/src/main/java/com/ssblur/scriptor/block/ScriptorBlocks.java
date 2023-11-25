package com.ssblur.scriptor.block;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.item.Spellbook;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ScriptorBlocks {
  public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ScriptorMod.MOD_ID, Registries.BLOCK);

  public static final RegistrySupplier<Block> RUNE = BLOCKS.register("rune", RuneBlock::new);
  public static final RegistrySupplier<Block> LIGHT = BLOCKS.register("light", LightBlock::new);
  public static final RegistrySupplier<Block> CHALK = BLOCKS.register("chalk", ChalkBlock::new);
  public static final RegistrySupplier<Block> CASTING_LECTERN = BLOCKS.register("casting_lectern", CastingLecternBlock::new);

  public static final RegistrySupplier<Block> WHITE_MAGIC_BLOCK = BLOCKS.register("white_magic_block",
    () -> new MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE)));
  public static final RegistrySupplier<Block> LIGHT_GRAY_MAGIC_BLOCK = BLOCKS.register("light_gray_magic_block",
    () -> new MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.LIGHT_GRAY)));
  public static final RegistrySupplier<Block> GRAY_MAGIC_BLOCK = BLOCKS.register("gray_magic_block",
    () -> new MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.GRAY)));
  public static final RegistrySupplier<Block> BLACK_MAGIC_BLOCK = BLOCKS.register("black_magic_block",
    () -> new MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.BLACK)));
  public static final RegistrySupplier<Block> BROWN_MAGIC_BLOCK = BLOCKS.register("brown_magic_block",
    () -> new MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.BROWN)));
  public static final RegistrySupplier<Block> RED_MAGIC_BLOCK = BLOCKS.register("red_magic_block",
    () -> new MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.RED)));
  public static final RegistrySupplier<Block> ORANGE_MAGIC_BLOCK = BLOCKS.register("orange_magic_block",
    () -> new MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.ORANGE)));
  public static final RegistrySupplier<Block> YELLOW_MAGIC_BLOCK = BLOCKS.register("yellow_magic_block",
    () -> new MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.YELLOW)));
  public static final RegistrySupplier<Block> LIME_MAGIC_BLOCK = BLOCKS.register("lime_magic_block",
    () -> new MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.LIME)));
  public static final RegistrySupplier<Block> GREEN_MAGIC_BLOCK = BLOCKS.register("green_magic_block",
    () -> new MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.GREEN)));
  public static final RegistrySupplier<Block> CYAN_MAGIC_BLOCK = BLOCKS.register("cyan_magic_block",
    () -> new MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.CYAN)));
  public static final RegistrySupplier<Block> LIGHT_BLUE_MAGIC_BLOCK = BLOCKS.register("light_blue_magic_block",
    () -> new MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.LIGHT_BLUE)));
  public static final RegistrySupplier<Block> BLUE_MAGIC_BLOCK = BLOCKS.register("blue_magic_block",
    () -> new MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.BLUE)));
  public static final RegistrySupplier<Block> PURPLE_MAGIC_BLOCK = BLOCKS.register("purple_magic_block",
    () -> new MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.PURPLE)));
  public static final RegistrySupplier<Block> MAGENTA_MAGIC_BLOCK = BLOCKS.register("magenta_magic_block",
    () -> new MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.MAGENTA)));
  public static final RegistrySupplier<Block> PINK_MAGIC_BLOCK = BLOCKS.register("pink_magic_block",
    () -> new MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.PINK)));

  public static void register() {
    BLOCKS.register();
  }
}
