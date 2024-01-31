package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.helpers.ItemTargetableHelper;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.InventoryTargetable;
import com.ssblur.scriptor.helpers.targetable.ItemTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;

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

  public static String getTargetSelector(Entity entity) {
    var uuid = entity.getUUID();
    long l = uuid.getLeastSignificantBits();
    long m = uuid.getMostSignificantBits();
    return String.format("@e[nbt={UUID:[I;%d,%d,%d,%d]}]", (int) (m >> 32), (int) m, (int) (l >> 32), (int) l);
  }

  public static String substituteTargetStrings(String string, String casterString, String targetString) {
    return string
      .replace("@caster[", casterString.substring(0, casterString.length()-1) + ",")
      .replace("@target[", targetString.substring(0, targetString.length()-1) + ",")
      .replace("@caster", casterString)
      .replace("@target", targetString);
  }

  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    // @caster is a fake selector that's replaced at runtime.
    // It will target the entity or block position that cast this spell.
    // @target is a fake selector that is also replaced at runtime.
    // It will target the target of this spell, which is useful in item targeted commands.
    // This is run using execute.
    // By default, this is run as the target, with @s targeting them.
    String casterString = "@s";
    ArmorStand tempCasterEntity = null;
    ArmorStand tempItemEntity = null;

    if(caster instanceof EntityTargetable entityTargetable) {
      var entity = entityTargetable.getTargetEntity();
      casterString = getTargetSelector(entity);
    } else {
      var level = (ServerLevel) caster.getLevel();
      tempCasterEntity = EntityType.ARMOR_STAND.spawn(level, caster.getOrigin(), MobSpawnType.COMMAND);
      if(tempCasterEntity != null) {
        casterString = getTargetSelector(tempCasterEntity);
        tempCasterEntity.setCustomName(Component.translatable("block.scriptor.casting_lectern"));
      }
    }

    List<String> commandToParse = new ArrayList<>();
    var pos = targetable.getTargetPos();
    if(!ItemTargetableHelper.getTargetItemStack(targetable).isEmpty() && !itemTargetCommand.isEmpty()) {
      var targetString = "@s";
      if(targetable instanceof  EntityTargetable entityTargetable)
        targetString = getTargetSelector(entityTargetable.getTargetEntity());
      if(caster.getLevel() instanceof ServerLevel level) {
        tempItemEntity = EntityType.ARMOR_STAND.spawn(level, targetable.getTargetBlockPos(), MobSpawnType.COMMAND);
        if (tempItemEntity != null) {
          tempItemEntity.setItemSlot(EquipmentSlot.MAINHAND, ItemTargetableHelper.getTargetItemStack(targetable).copy());
          tempItemEntity.setCustomName(Component.translatable("command.scriptor.magic_name"));
          for (var i : itemTargetCommand)
            commandToParse.add(
              String.format(
                "execute at %s as %s run %s",
                getTargetSelector(tempItemEntity),
                getTargetSelector(tempItemEntity),
                substituteTargetStrings(i, casterString, targetString)
              )
            );
        }
      } else {
        if(caster instanceof EntityTargetable entityTargetable && entityTargetable.getTargetEntity() instanceof Player player)
          player.sendSystemMessage(Component.translatable("command.scriptor.no_item_commands_creative"));
      }
    } else if(targetable instanceof EntityTargetable entityTargetable && !entityTargetCommand.isEmpty()) {
      var entity = entityTargetable.getTargetEntity();
      var targetString = getTargetSelector(entity);
      var uuid = entity.getUUID();
      for(var i: entityTargetCommand)
        commandToParse.add(
          String.format(
            "execute at %s as %s run %s",
            targetString,
            targetString,
            substituteTargetStrings(i, casterString, targetString)
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
            substituteTargetStrings(i, casterString, "@e[distance=..1]")
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
    if(tempItemEntity != null) {
      if(targetable instanceof InventoryTargetable inventoryTargetable) {
        if (inventoryTargetable.getContainer() != null)
          inventoryTargetable.getContainer().setItem(inventoryTargetable.getTargetedSlot(), tempItemEntity.getItemBySlot(EquipmentSlot.MAINHAND));
      } else if(targetable instanceof ItemTargetable itemTargetable) {
        itemTargetable.getTargetItem().setCount(0);
        ItemTargetableHelper.depositItemStack(itemTargetable, tempItemEntity.getItemBySlot(EquipmentSlot.MAINHAND));
      }
      tempItemEntity.remove(Entity.RemovalReason.DISCARDED);
    }
  }
}
