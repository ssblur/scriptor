package com.ssblur.scriptor.word;

import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.action.Action;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.subject.Subject;
import net.minecraft.world.entity.Entity;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
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
  void castOnTargets(Entity caster, List<Targetable> targets) {
    for(var target: targets) {
      action.apply(caster, target, deduplicatedDescriptors());
    }
  }

  public CompletableFuture<List<Targetable>> createFuture(Entity caster) {
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
  public void cast(Entity caster) {
    var targetFuture = subject.getTargets(caster, this);
    if(targetFuture.isDone()) {
      try {
        castOnTargets(caster, targetFuture.get());
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    } else {
      targetFuture.whenComplete((targets, throwable) -> {
        if(throwable != null)
          throwable.printStackTrace();
        else
          castOnTargets(caster, targets);
      });
    }
  }

  /**
   * The cost for this spell, generally affects cooldowns / material cost.
   * @return A number representing cost.
   */
  public double cost() {
    double sum = 0;
    double scalar = 1;
    ArrayList<Double> discounts = new ArrayList<>();

    for(var d: deduplicatedDescriptors()) {
      var cost = d.cost();
      switch (cost.type()){
        case ADDITIVE -> sum += cost.cost();
        case MULTIPLICATIVE -> scalar += cost.cost();
        case MULTIPLICATIVE_POST -> discounts.add(cost.cost());
      }
    }

    var cost = action.cost();
    switch (cost.type()){
      case ADDITIVE -> sum += cost.cost();
      case MULTIPLICATIVE -> scalar += cost.cost();
      case MULTIPLICATIVE_POST -> discounts.add(cost.cost());
    }

    cost = subject.cost();
    switch (cost.type()){
      case ADDITIVE -> sum += cost.cost();
      case MULTIPLICATIVE -> scalar += cost.cost();
      case MULTIPLICATIVE_POST -> discounts.add(cost.cost());
    }

    var out = sum * scalar;
    for(double discount: discounts)
      out *= discount;
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
}
