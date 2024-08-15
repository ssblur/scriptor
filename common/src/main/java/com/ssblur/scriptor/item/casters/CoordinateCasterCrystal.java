package com.ssblur.scriptor.item.casters;

import com.ssblur.scriptor.data_components.ScriptorDataComponents;
import com.ssblur.scriptor.events.network.server.ServerTraceNetwork;
import com.ssblur.scriptor.helpers.ComponentHelper;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class CoordinateCasterCrystal extends CasterCrystal {
  public CoordinateCasterCrystal(Properties properties) {
    super(properties);
  }

  public static class BlockPosDirection extends Pair<BlockPos, Direction> {
    BlockPos left;
    Direction right;
    public BlockPosDirection(BlockPos left, Direction right) {
      this.left = left;
      this.right = right;
    }

    @Override
    public BlockPos getLeft() {
      return left;
    }

    @Override
    public Direction getRight() {
      return right;
    }

    @Override
    public Direction setValue(Direction value) {
      return right = value;
    }
  }

  @Override
  public List<Targetable> getTargetables(ItemStack itemStack, Level level) {
    ArrayList<Targetable> list = new ArrayList<>();
    var coordinates = itemStack.get(ScriptorDataComponents.COORDINATES);
    if(coordinates != null)
      for(var pos: coordinates) {
        var targetable = new Targetable(level, new BlockPos(pos.get(0), pos.get(1), pos.get(2)));
        targetable.setFacing(Direction.values()[pos.get(3)]);
        list.add(targetable);
      }
    return list;
  }

  @Override
  public void appendHoverText(ItemStack itemStack, TooltipContext level, List<Component> list, TooltipFlag tooltipFlag) {
    super.appendHoverText(itemStack, level, list, tooltipFlag);
    var coordinates = getCoordinates(itemStack);
    for(var pair: coordinates) {
      var coordinate = pair.getLeft();
      ComponentHelper.updateTooltipWith(ChatFormatting.GRAY, list, "lore.scriptor.coordinate_crystal_3", coordinate.getX(), coordinate.getY(), coordinate.getZ());
    }

    if(coordinates.isEmpty())
      list.add(Component.translatable("lore.scriptor.coordinate_crystal_1").withStyle(ChatFormatting.GRAY));
    else {
      if(coordinates.size() < 4)
        list.add(Component.translatable("lore.scriptor.coordinate_crystal_2").withStyle(ChatFormatting.GRAY));
      list.add(Component.translatable("lore.scriptor.crystal_reset").withStyle(ChatFormatting.GRAY));
    }
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
    var result = super.use(level, player, interactionHand);

    if(!level.isClientSide) {
      ItemStack itemStack = player.getItemInHand(interactionHand);
      ServerTraceNetwork.requestTraceData(player, target -> addCoordinate(itemStack, target.getTargetBlockPos(), target.getFacing()));
    }

    return result;
  }

  public static void addCoordinate(ItemStack itemStack, BlockPos pos, Direction direction) {
    var list = itemStack.get(ScriptorDataComponents.COORDINATES);
    if(list == null)
      list = new ArrayList<>();
    if(list.size() < 4) {
      list = new ArrayList<>(list);
      list.add(List.of(pos.getX(), pos.getY(), pos.getZ(), direction.ordinal()));
    }
    itemStack.set(ScriptorDataComponents.COORDINATES, list);
  }

  public static List<BlockPosDirection> getCoordinates(ItemStack itemStack) {
    ArrayList<BlockPosDirection> list = new ArrayList<>();
    var coordinates = itemStack.get(ScriptorDataComponents.COORDINATES);
    if(coordinates != null)
      for(var pos: coordinates)
        list.add(new BlockPosDirection(new BlockPos(pos.get(0), pos.get(1), pos.get(2)), Direction.values()[pos.get(3)]));
    return list;
  }
}
