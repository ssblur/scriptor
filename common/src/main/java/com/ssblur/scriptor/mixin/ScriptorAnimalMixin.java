package com.ssblur.scriptor.mixin;

import com.ssblur.scriptor.entity.ColorfulSheep;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Animal.class)
public class ScriptorAnimalMixin {
  @Unique
  List<Class<?>> scriptor$SHEEP = List.of(Sheep.class, ColorfulSheep.class);

  @Inject(method = "canMate", at = @At("RETURN"), cancellable = true)
  private void scriptor$canMate(Animal animal, CallbackInfoReturnable<Boolean> info) {
    var self = (Animal) (Object) this;
    if(scriptor$SHEEP.contains(self.getClass()) && scriptor$SHEEP.contains(animal.getClass()))
      info.setReturnValue(true);
  }
}
