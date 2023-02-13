package com.ssblur.scriptor.block;

import com.ssblur.scriptor.ScriptorMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class ScriptorBlocks {
  public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ScriptorMod.MOD_ID, Registry.BLOCK_REGISTRY);

  public static final RegistrySupplier<Block> RUNE = BLOCKS.register("rune", RuneBlock::new);
  public static final RegistrySupplier<Block> WRITING_DESK = BLOCKS.register("writing_desk", WritingDeskBlock::new);

  public static void register() {
    BLOCKS.register();
  }

  public enum HorizontalFacing implements StringRepresentable {
    NORTH(Direction.NORTH),
    SOUTH(Direction.SOUTH),
    EAST(Direction.EAST),
    WEST(Direction.WEST);

    public final Direction direction;
    HorizontalFacing(Direction direction) {
      this.direction = direction;
    }

    public static HorizontalFacing of(Direction direction) {
      for(var value: HorizontalFacing.values())
        if(value.direction == direction)
          return value;
      return NORTH;
    }

    @Override
    public String getSerializedName() {
      return this.name().toLowerCase();
    }
  }
  public static EnumProperty<HorizontalFacing> HorizontalProperty = EnumProperty.create("facing", HorizontalFacing.class);
}
