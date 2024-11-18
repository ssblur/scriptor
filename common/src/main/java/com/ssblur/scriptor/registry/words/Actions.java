package com.ssblur.scriptor.registry.words;

import com.ssblur.scriptor.api.word.Action;
import com.ssblur.scriptor.item.ScriptorItems;
import com.ssblur.scriptor.word.action.*;
import com.ssblur.scriptor.word.action.bound.BoundSwordAction;
import com.ssblur.scriptor.word.action.bound.BoundToolAction;
import com.ssblur.scriptor.word.action.teleport.BringAction;
import com.ssblur.scriptor.word.action.teleport.GotoAction;
import com.ssblur.scriptor.word.action.teleport.SwapAction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.List;

@SuppressWarnings("unused")
public class Actions {
  private static final WordRegistry INSTANCE = WordRegistry.INSTANCE;

  public final Action INFLAME = INSTANCE.register("inflame", new InflameAction());
  public final Action LIGHT = INSTANCE.register("light", new LightAction());
  public final Action HEAL = INSTANCE.register("heal", new HealAction());
  public final Action SMITE = INSTANCE.register("smite", new SmiteAction());
  public final Action EXPLOSION = INSTANCE.register("explosion", new ExplosionAction());
  public final Action GOTO = INSTANCE.register("goto", new GotoAction());
  public final Action SWAP = INSTANCE.register("swap", new SwapAction());
  public final Action BRING = INSTANCE.register("bring", new BringAction());
  public final Action BREAK = INSTANCE.register("break", new BreakBlockAction());
  public final Action PLACE = INSTANCE.register("place", new PlaceBlockAction());
  public final Action HARM = INSTANCE.register("harm", new HarmAction());
  public final Action COLOR = INSTANCE.register("color", new ColorAction());
  public final Action TIME = INSTANCE.register("time", new AdvanceTimeAction());
  public final Action CLEAR_WEATHER = INSTANCE.register("clear_weather", new ClearWeatherAction());
  public final Action RAIN = INSTANCE.register("rain", new RainAction());

  public final Action BOUND_SWORD = INSTANCE.register("bound_sword", new BoundSwordAction());
  public final Action BOUND_AXE = INSTANCE.register("bound_axe",
    new BoundToolAction(ScriptorItems.BOUND_AXE, List.of(TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("mineable/axe")))));
  public final Action BOUND_SHOVEL = INSTANCE.register("bound_shovel",
    new BoundToolAction(ScriptorItems.BOUND_SHOVEL, List.of(TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("mineable/shovel")))));
  public final Action BOUND_PICKAXE = INSTANCE.register("bound_pickaxe",
    new BoundToolAction(ScriptorItems.BOUND_PICKAXE, List.of(TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("mineable/pickaxe")))));
}
