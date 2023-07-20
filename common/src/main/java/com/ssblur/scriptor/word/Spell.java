package com.ssblur.scriptor.word;

import com.ssblur.scriptor.advancement.ScriptorAdvancements;
import com.ssblur.scriptor.effect.ScriptorEffects;
import com.ssblur.scriptor.events.messages.ParticleNetwork;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.action.Action;
import com.ssblur.scriptor.word.descriptor.AfterCastDescriptor;
import com.ssblur.scriptor.word.descriptor.CastDescriptor;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.descriptor.focus.FocusDescriptor;
import com.ssblur.scriptor.word.descriptor.target.TargetDescriptor;
import com.ssblur.scriptor.word.subject.Subject;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * A record used to represent a complete spell.
 * @param subject The target of a spell
 * @param spells Groups of descriptors and actions
 */
public record Spell(
  Subject subject,
  PartialSpell... spells
) {
  void castOnTargets(Targetable caster, List<Targetable> targets) {
    assert spells.length >= 1;
    for(var descriptor: spells[0].deduplicatedDescriptors()) {
      if (descriptor instanceof TargetDescriptor cast)
        targets = cast.modifyTargets(targets);
      if (descriptor instanceof FocusDescriptor focus)
        caster = focus.modifyFocus(caster);
    }

    for(var target: targets) {
      for(var spell: spells)
        spell.action().apply(caster, target, spells[0].deduplicatedDescriptors());
    }
  }

  public CompletableFuture<List<Targetable>> createFuture(Targetable caster) {
    var targetFuture = new CompletableFuture<List<Targetable>>();

    targetFuture.whenComplete((targets, throwable) -> {
      if(throwable != null)
        throwable.printStackTrace();
      else
        castOnTargets(caster, targets);
    });

    return targetFuture;
  }

  /**
   * Casts this spell.
   * @param caster The entity which cast this spell.
   */
  public void cast(Targetable caster) {
    if(caster instanceof EntityTargetable entity && entity.getTargetEntity() instanceof LivingEntity living)
      if(living.hasEffect(ScriptorEffects.MUTE.get())) {
        if(living instanceof Player player)
          player.sendSystemMessage(Component.translatable("extra.scriptor.mute"));
        return;
      }

    assert spells.length >= 1;
    for(var descriptor: spells[0].deduplicatedDescriptors()) {
      if (descriptor instanceof CastDescriptor cast)
        if (cast.cannotCast(caster)) {
          if (caster instanceof EntityTargetable entityTargetable && entityTargetable.getTargetEntity() instanceof Player player) {
            player.sendSystemMessage(Component.translatable("extra.scriptor.condition_not_met"));
            ScriptorAdvancements.FIZZLE.trigger((ServerPlayer) player);
          }
          if(!caster.getLevel().isClientSide)
            ParticleNetwork.fizzle(caster.getLevel(), caster.getTargetBlockPos());
          return;
        }
      if (descriptor instanceof FocusDescriptor focus)
        caster = focus.modifyFocus(caster);
    }

    var targetFuture = subject.getTargets(caster, this);

    for(var descriptor: spells[0].deduplicatedDescriptors())
      if(descriptor instanceof AfterCastDescriptor afterCastDescriptor)
        afterCastDescriptor.afterCast(caster);

    final var finalCaster = caster.getFinalTargetable();
    if(targetFuture.isDone()) {
      try {
        var targets = targetFuture.get();
        castOnTargets(finalCaster, targets);
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    } else {
      targetFuture.whenComplete((targets, throwable) -> {
        if(throwable != null)
          throwable.printStackTrace();
        else {
          castOnTargets(finalCaster, targets);
        }
      });
    }
  }

  /**
   * Casts this spell with a specified list of Targetables.
   * @param caster The entity which cast this spell.
   */
  public void cast(Targetable caster, Targetable... targetables) {
    castOnTargets(caster, Arrays.stream(targetables).toList());
  }

  /**
   * The cost for this spell, generally affects cooldowns / material cost.
   * @return A number representing cost.
   */
  public double cost() {
    double sum = 0;
    double scalar = 1;
    double discount = 0;

    for(var d: words()) {
      var cost = d.cost();
      switch (cost.type()){
        case ADDITIVE -> sum += cost.cost();
        case MULTIPLICATIVE -> scalar *= cost.cost();
        case ADDITIVE_POST -> discount += cost.cost();
      }
    }

    var out = sum * scalar;
    out += discount;
    return out;
  }



  private Word[] words() {
    int length = 1;
    int index = 1;
    for(var spell: spells)
      length += spell.deduplicatedDescriptors().length + 1;

    Word[] words = new Word[length];
    words[0] = subject;

    for(var spell: spells) {
      words[index] = spell.action();
      index++;

      var descriptors = spell.deduplicatedDescriptors();
      System.arraycopy(descriptors, 0, words, index, descriptors.length);
      index += descriptors.length;
    }
    return words;
  }

  public Descriptor[] deduplicatedDescriptorsForSubjects() {
    assert spells.length >= 1;
    return spells[0].deduplicatedDescriptors();
  }

  public Descriptor[] deduplicatedDescriptorsForAccumulation() {
    int length = 0;
    int index = 0;

    for(var spell: spells)
      length += spell.deduplicatedDescriptors().length;

    Descriptor[] descriptors = new Descriptor[length];

    for(var spell: spells) {
      System.arraycopy(spell.deduplicatedDescriptors(), 0, descriptors, index, descriptors.length);
      index += descriptors.length;
    }

    return descriptors;
  }
}
