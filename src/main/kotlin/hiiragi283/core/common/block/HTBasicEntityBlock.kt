package hiiragi283.core.common.block

import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.world.getTypedBlockEntity
import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.core.common.block.entity.HTExtendedBlockEntity
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.util.HTStackSlotHelper
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

open class HTBasicEntityBlock(private val type: HTDeferredBlockEntityType<*>, properties: Properties) :
    Block(properties),
    HTBlockWithEntity {
    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): ItemInteractionResult {
        val result: ItemInteractionResult = super.useItemOn(stack, state, level, pos, player, hand, hitResult)
        if (stack.isEmpty) return result
        if (!player.isShiftKeyDown) {
            val blockEntity: HTBlockEntity = level.getTypedBlockEntity(pos) ?: return result
            if (!level.isClientSide) {
                for (tank: HTFluidTank in blockEntity.getFluidTanks(blockEntity.getFluidSideFor()).reversed()) {
                    if (HTStackSlotHelper.interact(player, hand, stack, tank)) {
                        player.inventory.setChanged()
                        return ItemInteractionResult.sidedSuccess(false)
                    }
                }
            }
        }
        return result
    }

    override fun setPlacedBy(
        level: Level,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        stack: ItemStack,
    ) {
        super.setPlacedBy(level, pos, state, placer, stack)
        level.getTypedBlockEntity<HTBlockEntity>(pos)?.ownerId = placer?.uuid
    }

    final override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        if (!state.`is`(newState.block)) {
            level.getTypedBlockEntity<HTBlockEntity>(pos)?.let { blockEntity: HTBlockEntity ->
                blockEntity.onBlockRemoved(state, level, pos)
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston)
    }

    final override fun triggerEvent(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        id: Int,
        param: Int,
    ): Boolean {
        super.triggerEvent(state, level, pos, id, param)
        return level.getBlockEntity(pos)?.triggerEvent(id, param) ?: false
    }

    final override fun neighborChanged(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        neighborBlock: Block,
        neighborPos: BlockPos,
        movedByPiston: Boolean,
    ) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston)
        level.getTypedBlockEntity<HTExtendedBlockEntity>(pos)?.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston)
    }

    final override fun getBlockEntityType(): HTDeferredBlockEntityType<*> = type
}
