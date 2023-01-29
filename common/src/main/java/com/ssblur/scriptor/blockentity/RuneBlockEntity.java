package com.ssblur.scriptor.blockentity;

import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.Spell;
import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RuneBlockEntity extends BlockEntity {
  public Entity owner;
  // Keep the Spell for Serialization and to rebuild future if reloaded.
  public Spell spell;
  public CompletableFuture<List<Targetable>> future;

  public RuneBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(ScriptorBlockEntities.RUNE.get(), blockPos, blockState);
  }

  public void tick() {
    if(level == null) return;
    if(future == null || future.isDone())
      if(spell == null) {
        level.removeBlock(worldPosition, true);
      } else {
        future = spell.createFuture(owner);
      }

    var box = AABB.of(
      BoundingBox.fromCorners(
        new Vec3i(worldPosition.getX() + 0.2, worldPosition.getY() + 0.2, worldPosition.getZ() + 0.2),
        new Vec3i(worldPosition.getX() + 0.6, worldPosition.getY() + 0.0625, worldPosition.getZ() + 0.6)
      )
    );
    var entities = level.getEntities(null, box);
    if(entities.size() > 0) {
      List<Targetable> targets = new ArrayList<>();
      for(var entity: entities)
        targets.add(new EntityTargetable(entity));
      future.complete(targets);
      level.removeBlock(worldPosition, true);
    }
  }

  public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T entity) {
    if(level == null || level.isClientSide) return;
    if(entity instanceof RuneBlockEntity tile) tile.tick();
  }
}
