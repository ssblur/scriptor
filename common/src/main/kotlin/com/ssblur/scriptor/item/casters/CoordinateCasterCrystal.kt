package com.ssblur.scriptor.item.casters

import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.events.network.server.ServerTraceNetwork
import com.ssblur.scriptor.helpers.ComponentHelper
import com.ssblur.scriptor.helpers.targetable.Targetable
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import org.apache.commons.lang3.tuple.Pair

class CoordinateCasterCrystal(properties: Properties) : CasterCrystal(properties) {
    class BlockPosDirection(var blockPos: BlockPos, var direction: Direction) : Pair<BlockPos, Direction>() {
        override fun getLeft(): BlockPos {
            return blockPos
        }

        override fun getRight(): Direction {
            return direction
        }

        override fun setValue(newValue: Direction): Direction {
            return newValue.also { direction = it }
        }

        fun setValue(newValue: BlockPos): BlockPos {
            return newValue.also { blockPos = it }
        }
    }

    override fun getTargetables(itemStack: ItemStack?, level: Level?): List<Targetable?> {
        val list = ArrayList<Targetable?>()
        val coordinates = itemStack!!.get(ScriptorDataComponents.COORDINATES)
        if (coordinates != null) for (pos in coordinates) {
            val targetable = Targetable(
                level!!,
                BlockPos(pos[0], pos[1], pos[2])
            )
            targetable.setFacing(Direction.entries[pos[3]])
            list.add(targetable)
        }
        return list
    }

    override fun appendHoverText(
        itemStack: ItemStack,
        level: TooltipContext,
        list: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(itemStack, level, list, tooltipFlag)
        val coordinates = getCoordinates(itemStack)
        for (pair in coordinates) {
            val coordinate = pair.getLeft()
            ComponentHelper.updateTooltipWith(
                ChatFormatting.GRAY,
                list,
                "lore.scriptor.coordinate_crystal_3",
                coordinate.x,
                coordinate.y,
                coordinate.z
            )
        }

        if (coordinates.isEmpty()) list.add(
            Component.translatable("lore.scriptor.coordinate_crystal_1").withStyle(
                ChatFormatting.GRAY
            )
        )
        else {
            if (coordinates.size < 4) list.add(
                Component.translatable("lore.scriptor.coordinate_crystal_2").withStyle(
                    ChatFormatting.GRAY
                )
            )
            list.add(Component.translatable("lore.scriptor.crystal_reset").withStyle(ChatFormatting.GRAY))
        }
    }

    override fun use(
        level: Level,
        player: Player,
        interactionHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {
        val result = super.use(level, player, interactionHand)

        if (!level.isClientSide) {
            val itemStack = player.getItemInHand(interactionHand)
            ServerTraceNetwork.requestTraceData(player) { target: Targetable ->
                addCoordinate(
                    itemStack,
                    target.targetBlockPos,
                    target.facing
                )
            }
        }

        return result
    }

    companion object {
        fun addCoordinate(itemStack: ItemStack, pos: BlockPos, direction: Direction) {
            var list = itemStack.get(ScriptorDataComponents.COORDINATES)
            if (list == null) list = ArrayList()
            if (list.size < 4) {
                list = ArrayList(list)
                list.add(java.util.List.of(pos.x, pos.y, pos.z, direction.ordinal))
            }
            itemStack.set(ScriptorDataComponents.COORDINATES, list)
        }

        @JvmStatic
        fun getCoordinates(itemStack: ItemStack): List<BlockPosDirection> {
            val list = ArrayList<BlockPosDirection>()
            val coordinates = itemStack.get(ScriptorDataComponents.COORDINATES)
            if (coordinates != null) for (pos in coordinates) list.add(
                BlockPosDirection(
                    BlockPos(
                        pos[0],
                        pos[1],
                        pos[2]
                    ), Direction.entries[pos[3]]
                )
            )
            return list
        }
    }
}
