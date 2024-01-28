package com.ssblur.scriptor.word.action;

import com.mojang.brigadier.CommandDispatcher;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.ItemTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class CommandAction extends Action {
  String entityTargetCommand;
  String blockTargetCommand;
  String itemTargetCommand;
  double cost;

  public CommandAction(double cost, String blockTargetCommand, String entityTargetCommand, String itemTargetCommand) {
    this.cost = cost;
    this.blockTargetCommand = blockTargetCommand;
    this.entityTargetCommand = entityTargetCommand;
    this.itemTargetCommand = itemTargetCommand;
  }

  public CommandAction(double cost, String blockTargetCommand, String entityTargetCommand) {
    this(cost, blockTargetCommand, entityTargetCommand, null);
  }

  public CommandAction(double cost, String blockTargetCommand) {
    this(cost, blockTargetCommand, null);
  }

  public void setEntityTargetCommand(String entityTargetCommand) {
    this.entityTargetCommand = entityTargetCommand;
  }

  public void setItemTargetCommand(String itemTargetCommand) {
    this.itemTargetCommand = itemTargetCommand;
  }

  public void setBlockTargetCommand(String blockTargetCommand) {
    this.blockTargetCommand = blockTargetCommand;
  }

  @Override
  public Cost cost() {
    return Cost.add(cost);
  }

  public boolean applyToItem(Targetable caster, ItemTargetable targetable, Descriptor[] descriptors) {
    // TODO
    return false;
  }

  public static String getTargetSelector(Entity entity) {
    var uuid = entity.getUUID();
    long l = uuid.getLeastSignificantBits();
    long m = uuid.getMostSignificantBits();
    return String.format("@e[nbt={UUID:[I;%d,%d,%d,%d]}]", (int) (m >> 32), (int) m, (int) (l >> 32), (int) l);
  }

  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    if(targetable instanceof ItemTargetable itemTargetable && itemTargetCommand != null)
      if(applyToItem(caster, itemTargetable, descriptors))
        return;

    // @caster is a fake selector that's replaced at runtime.
    // It will target the entity or block position that cast this spell.
    // This is run using execute.
    // By default, this is run as the target, with @s targeting them.
    String casterString;

    if(caster instanceof EntityTargetable entityTargetable) {
      var entity = entityTargetable.getTargetEntity();
      casterString = getTargetSelector(entity);
    } else {
      // summon an entity for targeting.
      casterString = "todo";
    }

    String commandToParse;
    var pos = targetable.getTargetPos();
    if(targetable instanceof EntityTargetable entityTargetable && entityTargetCommand != null) {
      var entity = entityTargetable.getTargetEntity();
      var uuid = entity.getUUID();
      commandToParse = String.format(
        "execute at %s as %s run %s",
        getTargetSelector(entity),
        getTargetSelector(entity),
        entityTargetCommand.replace("@caster", casterString)
      );
    } else {
      commandToParse = String.format(
        "execute positioned %f %f %f in %s run %s",
        pos.x,
        pos.y,
        pos.z,
        targetable.getLevel().dimension().location(),
        blockTargetCommand.replace("@caster", casterString)
      );
    }

    if(targetable.getLevel().getServer() == null) return;
    var commands = targetable.getLevel().getServer().getCommands();
    var results = commands.getDispatcher().parse(
      commandToParse,
      new CommandSourceStack(
        CommandSource.NULL,
        targetable.getTargetPos(),
        new Vec2(0, 0),
        (ServerLevel) targetable.getLevel(),
        4,
        "Magic",
        Component.translatable("command.scriptor.magic_name"),
        targetable.getLevel().getServer(),
        null
      )
    );
    commands.performCommand(results, commandToParse);
  }
}
