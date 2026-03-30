package com.ssblur.scriptor.neoforge;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.block.ScriptorBlocks;
import com.ssblur.scriptor.blockentity.CastingLecternBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

@Mod(ScriptorMod.MOD_ID)
public final class ScriptorModNeoForge {
  public ScriptorModNeoForge(@SuppressWarnings("unused") IEventBus bus) {
    ScriptorMod.INSTANCE.init();
    if (FMLEnvironment.dist == Dist.CLIENT) {
      ScriptorMod.INSTANCE.clientInit();
    }

    bus.addListener(this::attachEvent);
  }

  public void attachEvent(RegisterCapabilitiesEvent event) {
      event.registerBlock(Capabilities.EnergyStorage.BLOCK, new IBlockCapabilityProvider<>() {
          @Override
          public @Nullable IEnergyStorage getCapability(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, Direction object) {
              if(entity instanceof CastingLecternBlockEntity lectern) {
                  return new LecternEnergyHandler(lectern);
              }
              return null;
          }
      },
      ScriptorBlocks.INSTANCE.getCASTING_LECTERN().getFirst().get());
  }

  class LecternEnergyHandler implements IEnergyStorage {
      CastingLecternBlockEntity entity;
      public LecternEnergyHandler(CastingLecternBlockEntity e) {
          entity = e;
      }

      @Override
      public int receiveEnergy(int i, boolean bl) {
          var diff = entity.getCooldown() - i;
          var ret = 0;
          if(diff < 0) {
              ret = diff * -1;
              diff = 0;
          }
          entity.setCooldown(diff);
          return ret;
      }
      @Override
      public int extractEnergy(int i, boolean bl) {
          return 0;
      }
      @Override
      public int getEnergyStored() {
          return 0;
      }
      @Override
      public int getMaxEnergyStored() {
          return 0;
      }
      @Override
      public boolean canExtract() {
          return false;
      }
      @Override
      public boolean canReceive() {
          return true;
      }
  }
}
