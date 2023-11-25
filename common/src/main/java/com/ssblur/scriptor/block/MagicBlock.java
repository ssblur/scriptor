package com.ssblur.scriptor.block;

import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

public class MagicBlock extends Block {
  private static final StateArgumentPredicate<EntityType<?>> SPAWN_NEVER = (a, b, c, d) -> false;
  private static final StatePredicate NEVER = (a, b, c) -> false;

  public MagicBlock(Properties properties) {
    super(
      properties
        .instrument(NoteBlockInstrument.HAT)
        .strength(0.3f)
        .sound(SoundType.GLASS)
        .noOcclusion()
        .isValidSpawn(SPAWN_NEVER)
        .isRedstoneConductor(NEVER)
        .isSuffocating(NEVER)
        .isViewBlocking(NEVER)
    );

    if(!Platform.isForge())
      RenderTypeRegistry.register(RenderType.translucent(), this);
  }
}
