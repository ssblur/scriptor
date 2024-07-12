package com.ssblur.scriptor.blockentity;

import com.ssblur.scriptor.color.interfaces.Colorable;
import com.ssblur.scriptor.data.DictionarySavedData;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.Spell;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class RuneBlockEntity extends BlockEntity implements Colorable {
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
  public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
    var tag = super.getUpdateTag(provider);
    tag.putInt("com/ssblur/scriptor/color", color);
    return tag;
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.loadAdditional(tag, provider);
    color = tag.getInt("com/ssblur/scriptor/color");

    if(tag.contains("spell"))
      spellText = tag.getString("spell");
    else
      unloadedSpell = false;

    if(tag.contains("owner"))
      ownerUUID = UUID.fromString(tag.getString("owner"));

    setChanged();
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.saveAdditional(tag, provider);

    if(level instanceof ServerLevel server && spell != null)
      tag.putString("spell", DictionarySavedData.computeIfAbsent(server).generate(spell));
    if(owner != null && owner.getUUID() != null)
      tag.putString("owner", owner.getStringUUID());
    else if(ownerUUID != null)
      tag.putString("owner", ownerUUID.toString());
    tag.putInt("com/ssblur/scriptor/color", color);
  }

  public void tick() {
    if(level == null) return;
    if(level.isClientSide) return;

    if(level.getGameTime() % 40 == 0 && owner == null)
      if(ownerUUID != null && level.getPlayerByUUID(ownerUUID) != null) {
        owner = level.getPlayerByUUID(ownerUUID);
        // If the owner is online after this is reloaded, reassign ownership.
        var spell = DictionarySavedData.computeIfAbsent((ServerLevel) level).parse(spellText);
        if(spell != null) {
          if (owner != null)
            future = spell.createFuture(new EntityTargetable(owner));
          else
            future = spell.createFuture(new Targetable(this.level, this.getBlockPos()));
        }
      }

    if(future == null || future.isDone())
      if(spell != null) {
        if(owner != null && owner.isAlive())
          future = spell.createFuture(new EntityTargetable(owner));
        else
          future = spell.createFuture(new Targetable(this.level, this.getBlockPos()));
      } else if(spellText != null) {
        spell = DictionarySavedData.computeIfAbsent((ServerLevel) level).parse(spellText);
        if(spell != null)
          if(owner == null) {
            future = spell.createFuture(new Targetable(level, worldPosition));
          } else {
            future = spell.createFuture(new EntityTargetable(owner));
          }
      }

    double xMin = worldPosition.getX() >= 0 ? 0.2 : -0.8;
    double zMin = worldPosition.getZ() >= 0 ? 0.2 : -0.8;
    double xMax = worldPosition.getX() >= 0 ? 0.6 : -0.4;
    double zMax = worldPosition.getZ() >= 0 ? 0.6 : -0.4;
    var box = AABB.of(
      BoundingBox.fromCorners(
        new Vec3i((int) (worldPosition.getX() + xMin), (int) (worldPosition.getY() + 0.0), (int) (worldPosition.getZ() + zMin)),
        new Vec3i((int) (worldPosition.getX() + xMax), (int) (worldPosition.getY() + 0.0625), (int) (worldPosition.getZ() + zMax))
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

  @Override
  public void setColor(int color) {
    this.color = color;
    setChanged();
    if(level != null)
      level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
  }
}
