package com.ssblur.scriptor.mixin;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.ssblur.scriptor.entity.goals.HealVillagerGoal;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.VillagerGoalPackages;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerGoalPackages.class)
public class VillagerGoalPackagesMixin {
    @Inject(method = "getWorkPackage", at = @At("RETURN"), cancellable = true)
    private static void scriptor$getWorkPackage(VillagerProfession villagerProfession, float f, CallbackInfoReturnable<ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>>> cir) {
        cir.setReturnValue(HealVillagerGoal.Companion.injectBehavior(villagerProfession, cir.getReturnValue()));
    }
}
