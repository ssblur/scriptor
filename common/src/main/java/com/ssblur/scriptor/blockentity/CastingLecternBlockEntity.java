package com.ssblur.scriptor.blockentity;

import com.ssblur.scriptor.block.CastingLecternBlock;
import com.ssblur.scriptor.data.DictionarySavedData;
import com.ssblur.scriptor.events.network.client.ParticleNetwork;
import com.ssblur.scriptor.gamerules.ScriptorGameRules;
import com.ssblur.scriptor.helpers.LimitedBookSerializer;
import com.ssblur.scriptor.helpers.targetable.LecternTargetable;
import com.ssblur.scriptor.item.casters.CasterCrystal;
import com.ssblur.scriptor.word.Spell;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CastingLecternBlockEntity extends BlockEntity {
  public static final int SPELLBOOK_SLOT = 0;
  public static final int CASTING_FOCUS_SLOT = 1;

  NonNullList<ItemStack> items;
  int focusTarget;
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

  public ItemStack getFocus() { return items.get(CASTING_FOCUS_SLOT); }
  public void setFocus(ItemStack itemStack) {
    items.set(CASTING_FOCUS_SLOT, itemStack);
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
  public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
    var tag = super.getUpdateTag(provider);
    saveAdditional(tag, provider);
    return tag;
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.loadAdditional(tag, provider);
    items = NonNullList.withSize(2, ItemStack.EMPTY);
    ContainerHelper.loadAllItems(tag, items, provider);
    setChanged();
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.saveAdditional(tag, provider);
    ContainerHelper.saveAllItems(tag, items, provider);
  }

  public void tick() {
    if(level == null || level.isClientSide) return;
    cooldown = Math.max(0, cooldown - 1);
    if(level.getDirectSignalTo(getBlockPos()) == 0 && !getSpellbook().isEmpty() && cooldown == 0) {
      var item = getSpellbook();
      var tag = item.get(DataComponents.WRITTEN_BOOK_CONTENT);
      if(tag != null && level instanceof ServerLevel server) {
        Spell spell = DictionarySavedData.computeIfAbsent(server).parse(LimitedBookSerializer.decodeText(tag));
        if(spell != null) {
          if(spell.cost() > level.getGameRules().getInt(ScriptorGameRules.CASTING_LECTERN_MAX_COST)) {
            ParticleNetwork.fizzle(level, getBlockPos());
            level.playSound(null, this.getBlockPos(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            cooldown += (int) Math.round(200.0D * ( (double) level.getGameRules().getInt(ScriptorGameRules.CASTING_LECTERN_COOLDOWN_MULTIPLIER) / (double) 100));
            return;
          }
          var state = level.getBlockState(getBlockPos());
          var direction = state.getValue(CastingLecternBlock.FACING).getOpposite();
          var blockPos = this.getBlockPos();
          var pos = blockPos.getCenter();
          var target = new LecternTargetable(this.getLevel(), pos).setFacing(direction);
          if(getFocus().getItem() instanceof CasterCrystal crystal) {
            var foci = crystal.getTargetables(getFocus(), level);
            if(!foci.isEmpty()) {
              focusTarget++;
              focusTarget %= foci.size();
              var focus = foci.get(focusTarget);
              if(focus.getTargetPos().distanceTo(target.getTargetPos()) <= 16 && focus.getLevel() == level)
                target.setFinalTargetable(focus);
            }
          }
          spell.cast(target);
          cooldown += (int) Math.round(spell.cost() * 10.0D * ( (double) level.getGameRules().getInt(ScriptorGameRules.CASTING_LECTERN_COOLDOWN_MULTIPLIER) / (double) 100));
        }
      }
    }
  }

  public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T entity) {
    if(level.isClientSide) return;
    if(entity instanceof CastingLecternBlockEntity tile) tile.tick();
  }
}
