package com.ssblur.scriptor.mixin;

import com.ssblur.scriptor.color.interfaces.Colorable;
import com.ssblur.scriptor.entity.ScriptorEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Sheep;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Sheep.class)
public class SheepColorableInject implements Colorable {
  @Override
  public void setColor(int color) {
    var self = (Sheep) (Object) this;
    CompoundTag tag = new CompoundTag();
    self.addAdditionalSaveData(tag);
    tag.putInt("scriptor:color", color);

    var colorfulSheep = ScriptorEntities.COLORFUL_SHEEP_TYPE.get().spawn(
      (ServerLevel) self.level(),
      tag,
      null,
      self.getOnPos(),
      MobSpawnType.CONVERSION,
      true,
      true
    );
    if(colorfulSheep != null) {
      colorfulSheep.load(tag);
      colorfulSheep.setPos(self.position());
      colorfulSheep.setXRot(self.getXRot());
      colorfulSheep.setYRot(self.getYRot());
      self.level().addFreshEntity(colorfulSheep);

      self.remove(Entity.RemovalReason.DISCARDED);
    }
  }
}
