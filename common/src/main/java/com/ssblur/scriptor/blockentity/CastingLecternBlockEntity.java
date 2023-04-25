package com.ssblur.scriptor.blockentity;

import com.mojang.math.Vector3f;
import com.ssblur.scriptor.block.CastingLecternBlock;
import com.ssblur.scriptor.helpers.DictionarySavedData;
import com.ssblur.scriptor.helpers.LimitedBookSerializer;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.ItemTargetable;
import com.ssblur.scriptor.helpers.targetable.LecternTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.Spell;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CastingLecternBlockEntity extends BlockEntity {
  public static final int SPELLBOOK_SLOT = 0;
  public static final int CASTING_FOCUS_SLOT = 1;

  NonNullList<ItemStack> items;
  int cooldown;

  public CastingLecternBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(ScriptorBlockEntities.CASTING_LECTERN.get(), blockPos, blockState);
    items = NonNullList.withSize(2, ItemStack.EMPTY);
  }

  public ItemStack getSpellbook() {
    return items.get(SPELLBOOK_SLOT);
  }
  public void setSpellbook(ItemStack itemStack) {
    items.set(SPELLBOOK_SLOT, itemStack);
    if(level != null)
      level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    setChanged();
  }

  public NonNullList<ItemStack> getItems() {
    return items;
  }

  @Nullable
  @Override
  public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public CompoundTag getUpdateTag() {
    var tag = super.getUpdateTag();
    saveAdditional(tag);
    return tag;
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    items = NonNullList.withSize(2, ItemStack.EMPTY);
    ContainerHelper.loadAllItems(tag, items);
    setChanged();
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    ContainerHelper.saveAllItems(tag, items);
  }

  public void tick() {
    cooldown = Math.max(0, cooldown - 1);
    if(!getSpellbook().isEmpty() && cooldown == 0) {
      var item = getSpellbook();
      Tag tag = item.getTag();
      if(tag instanceof CompoundTag compound && level instanceof ServerLevel server) {
        var text = compound.getList("pages", Tag.TAG_STRING);
        Spell spell = DictionarySavedData.computeIfAbsent(server).parse(LimitedBookSerializer.decodeText(text));
        if(spell != null) {
          if(spell.cost() > 20) {
            System.out.println("would fizzle");
            // TODO: emit smoke when fizzling
            return;
          }
          var state = level.getBlockState(getBlockPos());
          var direction = state.getValue(CastingLecternBlock.FACING).getOpposite();
          var blockPos = this.getBlockPos();
          float offsetX = direction.getAxis() == Direction.Axis.X ? 0 : 0.5f;
          float offsetZ = direction.getAxis() == Direction.Axis.Z ? 0 : 0.5f;
          var pos = new Vector3f(blockPos.getX() + offsetX, blockPos.getY() + 0.5f, blockPos.getZ() + offsetZ);
          var target = new LecternTargetable(this.getLevel(), pos).setFacing(direction);
          spell.cast(target);
          cooldown += (int) Math.round(spell.cost() * 10);
        }
      }
    }
  }

  public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T entity) {
    if(level.isClientSide) return;
    if(entity instanceof CastingLecternBlockEntity tile) tile.tick();
  }
}
