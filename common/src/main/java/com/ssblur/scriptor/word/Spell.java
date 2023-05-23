package com.ssblur.scriptor.word;

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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * A record used to represent a complete spell.
 * @param action The active component of a spell
 * @param subject The target of a spell
 * @param descriptors Any descriptors affecting this spell
 */
public record Spell(
  Action action,
  Subject subject,
  Descriptor... descriptors
) {
  void castOnTargets(Targetable caster, List<Targetable> targets) {
    for(var descriptor: deduplicatedDescriptors()) {
      if (descriptor instanceof TargetDescriptor cast)
        targets = cast.modifyTargets(targets);
      if (descriptor instanceof FocusDescriptor focus)
        caster = focus.modifyFocus(caster);
    }

    for(var target: targets) {
      action.apply(caster, target, deduplicatedDescriptors());
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
    for(var descriptor: deduplicatedDescriptors()) {
      if (descriptor instanceof CastDescriptor cast)
        if (cast.cannotCast(caster)) {
          if (caster instanceof EntityTargetable entityTargetable && entityTargetable.getTargetEntity() instanceof Player player)
            player.sendSystemMessage(Component.translatable("extra.scriptor.condition_not_met"));
          if(!caster.getLevel().isClientSide)
            ParticleNetwork.fizzle(caster.getLevel(), caster.getTargetBlockPos());
          return;
        }
      if (descriptor instanceof FocusDescriptor focus)
        caster = focus.modifyFocus(caster);
    }

    var targetFuture = subject.getTargets(caster, this);

    for(var descriptor: deduplicatedDescriptors())
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
    for(var descriptor: deduplicatedDescriptors())
      if (descriptor instanceof CastDescriptor cast)
        if (cast.cannotCast(caster)) {
          if (caster instanceof EntityTargetable entityTargetable && entityTargetable.getTargetEntity() instanceof Player player)
            player.sendSystemMessage(Component.translatable("extra.scriptor.condition_not_met"));
          return;
        }

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

  public Descriptor[] deduplicatedDescriptors() {
    ArrayList<Descriptor> out = new ArrayList<>();
    for(var descriptor: descriptors) {
      if(descriptor.allowsDuplicates() || !out.contains(descriptor))
        out.add(descriptor);
    }
    return out.toArray(Descriptor[]::new);
  }

  public Word[] words() {
    var descriptors = deduplicatedDescriptors();
    Word[] words = new Word[descriptors.length + 2];

    words[0] = subject;
    words[1] = action;
    System.arraycopy(descriptors, 0, words, 2, descriptors.length);
    return words;
  }
}
