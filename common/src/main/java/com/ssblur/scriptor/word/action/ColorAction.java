package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.block.ScriptorBlocks;
import com.ssblur.scriptor.blockentity.LightBlockEntity;
import com.ssblur.scriptor.entity.ColorfulSheep;
import com.ssblur.scriptor.entity.ScriptorEntities;
import com.ssblur.scriptor.helpers.CustomColors;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.descriptor.duration.DurationDescriptor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ColorAction extends Action {
  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    int color = CustomColors.getColor(descriptors);
    if(targetable instanceof EntityTargetable entityTargetable) {
      if(entityTargetable.getTargetEntity() instanceof Sheep sheep) {
        if(sheep instanceof ColorfulSheep colorfulSheep) {
          colorfulSheep.setColor(color);
        } else {
          CompoundTag tag = new CompoundTag();
          sheep.addAdditionalSaveData(tag);
          tag.putInt("scriptor:color", color);

          var colorfulSheep = ScriptorEntities.COLORFUL_SHEEP_TYPE.get().spawn(
            (ServerLevel) sheep.level(),
            tag,
            null,
            sheep.getOnPos(),
            MobSpawnType.CONVERSION,
            true,
            true
          );
          if(colorfulSheep != null) {
            colorfulSheep.load(tag);
            colorfulSheep.setPos(sheep.position());
            colorfulSheep.setXRot(sheep.getXRot());
            colorfulSheep.setYRot(sheep.getYRot());
            sheep.level().addFreshEntity(colorfulSheep);

            sheep.remove(Entity.RemovalReason.DISCARDED);
          }
        }
      }
    }
  }

  @Override
  public Cost cost() { return new Cost(8, COSTTYPE.ADDITIVE); }

}
