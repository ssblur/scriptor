package com.ssblur.scriptor.blockentity;

import com.ssblur.scriptor.helpers.DictionarySavedData;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.Spell;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class RuneBlockEntity extends BlockEntity {
  public Entity owner;
  public UUID ownerUUID;
  // Keep the Spell for Serialization and to rebuild future if reloaded.
  public Spell spell;
  public String spellText;
  public CompletableFuture<List<Targetable>> future;
  public int color;
  boolean unloadedSpell = true;

  public RuneBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(ScriptorBlockEntities.RUNE.get(), blockPos, blockState);
  }

  @Nullable
  @Override
  public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public CompoundTag getUpdateTag() {
    var tag = super.getUpdateTag();
    tag.putInt("color", color);
    return tag;
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    color = tag.getInt("color");

    if(tag.contains("spell"))
      spellText = tag.getString("spell");
    else
      unloadedSpell = false;

    if(tag.contains("owner"))
      ownerUUID = UUID.fromString(tag.getString("owner"));

    setChanged();
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);

    if(level instanceof ServerLevel server && spell != null)
      tag.putString("spell", DictionarySavedData.computeIfAbsent(server).generate(spell));
    if(owner != null && owner.getUUID() != null)
      tag.putString("owner", owner.getStringUUID());
    else if(ownerUUID != null)
      tag.putString("owner", ownerUUID.toString());
    tag.putInt("color", color);
  }

  public void tick() {
    if(level == null) return;
    if(level.isClientSide) return;

    if(level.getGameTime() % 40 == 0 && owner == null)
      if(level.getPlayerByUUID(ownerUUID) != null) {
        owner = level.getPlayerByUUID(ownerUUID);
        // If the owner is online after this is reloaded, reassign ownership.
        future = DictionarySavedData.computeIfAbsent((ServerLevel) level).parse(spellText).createFuture(owner);
      }

    if(future == null || future.isDone())
      if(spell != null)
        future = spell.createFuture(owner);
      else if(spellText != null)
        future = DictionarySavedData.computeIfAbsent((ServerLevel) level).parse(spellText).createFuture(owner);

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
      if(future != null)
        future.complete(targets);
      level.removeBlock(worldPosition, true);
    }
  }

  public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T entity) {
    if(level.isClientSide) return;
    if(entity instanceof RuneBlockEntity tile) tile.tick();
  }
}
