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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandAction extends Action {
  List<String> entityTargetCommand;
  List<String> blockTargetCommand;
  List<String> itemTargetCommand;
  double cost;

  public CommandAction(double cost, String[] blockTargetCommand, String[] entityTargetCommand, String[] itemTargetCommand) {
    this.cost = cost;
    this.blockTargetCommand = List.of(blockTargetCommand);
    this.entityTargetCommand = List.of(entityTargetCommand);
    this.itemTargetCommand = List.of(itemTargetCommand);
  }

  public CommandAction(double cost, String[] blockTargetCommand, String[] entityTargetCommand) {
    this(cost, blockTargetCommand, entityTargetCommand, new String[0]);
  }

  public CommandAction(double cost, String[] blockTargetCommand) {
    this(cost, blockTargetCommand, new String[0]);
  }

  public CommandAction(double cost) {
    this(cost, new String[0]);
  }

  public void addEntityTargetCommand(String entityTargetCommand) {
    this.entityTargetCommand.add(entityTargetCommand);
  }

  public void addItemTargetCommand(String itemTargetCommand) {
    this.itemTargetCommand.add(itemTargetCommand);
  }

  public void addBlockTargetCommand(String blockTargetCommand) {
    this.blockTargetCommand.add(blockTargetCommand);
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
    String casterString = "@s";
    ArmorStand tempCasterEntity = null;

    if(caster instanceof EntityTargetable entityTargetable) {
      var entity = entityTargetable.getTargetEntity();
      casterString = getTargetSelector(entity);
    } else {
      // summon an entity for targeting.
      var level = (ServerLevel) caster.getLevel();
      tempCasterEntity = EntityType.ARMOR_STAND.spawn(level, caster.getOrigin(), MobSpawnType.COMMAND);
      if(tempCasterEntity != null) {
        casterString = getTargetSelector(tempCasterEntity);
        tempCasterEntity.setCustomName(Component.translatable("block.scriptor.casting_lectern"));
      }
    }

    List<String> commandToParse = new ArrayList<>();
    var pos = targetable.getTargetPos();
    if(targetable instanceof EntityTargetable entityTargetable && entityTargetCommand != null) {
      var entity = entityTargetable.getTargetEntity();
      var uuid = entity.getUUID();
      for(var i: entityTargetCommand)
        commandToParse.add(
          String.format(
            "execute at %s as %s run %s",
            getTargetSelector(entity),
            getTargetSelector(entity),
            i.replace("@caster", casterString)
          )
        );
    } else {
      for(var i: blockTargetCommand)
        commandToParse.add(
          String.format(
            "execute positioned %f %f %f in %s run %s",
            pos.x,
            pos.y,
            pos.z,
            targetable.getLevel().dimension().location(),
            i.replace("@caster", casterString)
          )
        );
    }

    if(targetable.getLevel().getServer() == null) return;
    var commands = targetable.getLevel().getServer().getCommands();
    for(var command: commandToParse) {
      var results = commands.getDispatcher().parse(
        command,
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
      commands.performCommand(results, command);
    }

    if(tempCasterEntity != null)
      tempCasterEntity.remove(Entity.RemovalReason.DISCARDED);
  }
}
