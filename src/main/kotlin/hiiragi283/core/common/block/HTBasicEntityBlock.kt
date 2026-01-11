package hiiragi283.core.common.block

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import com.lowdragmc.lowdraglib2.gui.ui.UI
import hiiragi283.core.api.world.getTypedBlockEntity
import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.core.common.block.entity.HTExtendedBlockEntity
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

open class HTBasicEntityBlock(private val type: HTDeferredBlockEntityType<*>, properties: Properties) :
    Block(properties),
    HTBlockWithEntity,
    BlockUIMenuType.BlockUI {
    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult = when {
        level.isClientSide -> InteractionResult.SUCCESS
        player is ServerPlayer -> {
            BlockUIMenuType.openUI(player, pos)
            InteractionResult.CONSUME
        }
        else -> InteractionResult.PASS
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

    //    BlockUIMenuType.BlockUI    //

    final override fun createUI(holder: BlockUIMenuType.BlockUIHolder): ModularUI? {
        val player: Player = holder.player
        val blockEntity: BlockEntity? = player.level().getBlockEntity(holder.pos)
        if (blockEntity is HTExtendedBlockEntity) {
            return blockEntity.createUI(holder)
        }
        return ModularUI(UI.of(), player)
    }
}
