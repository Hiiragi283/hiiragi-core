package hiiragi283.lib.block

import hiiragi283.lib.block.entity.HTBlockEntity
import hiiragi283.lib.block.entity.HTExtendedBlockEntity
import hiiragi283.lib.registry.HTDeferredBlockEntityType
import hiiragi283.lib.registry.HTDeferredMenuType
import hiiragi283.lib.text.Text
import hiiragi283.lib.world.getTypedBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionResult
import net.minecraft.world.Nameable
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.redstone.Orientation
import net.minecraft.world.phys.BlockHitResult

/**
 * Hiiragi Seriesで使用される[HTBlockWithEntity]の実装クラスです。
 *
 * 参考 : [Mekanism - BlockTile](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/common/block/prefab/BlockTile.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
open class HTBasicEntityBlock(private val type: HTDeferredBlockEntityType<*>, properties: Properties) :
    Block(properties),
    HTBlockWithEntity {

    override fun useWithoutItem(state: BlockState, level: Level, pos: BlockPos, player: Player, hitResult: BlockHitResult): InteractionResult {
        val blockEntity: HTExtendedBlockEntity = level.getTypedBlockEntity(pos) ?: return InteractionResult.PASS
        val menuType: HTDeferredMenuType.WithContext<*, *>? = getMenuType()
        if (level.isClientSide) {
            return when {
                menuType == null -> InteractionResult.PASS
                else -> InteractionResult.SUCCESS
            }
        }
        val name: Text = when (blockEntity) {
            is Nameable -> blockEntity.name
            else -> state.block.name
        }
        return menuType
            ?.openMenu(player, name, blockEntity) { it.writeBlockPos(blockEntity.blockPos) }
            ?: InteractionResult.PASS
    }

    protected open fun getMenuType(): HTDeferredMenuType.WithContext<*, *>? = null

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

    final override fun neighborChanged(state: BlockState, level: Level, pos: BlockPos, block: Block, orientation: Orientation?, movedByPiston: Boolean) {
        super.neighborChanged(state, level, pos, block, orientation, movedByPiston)
        level.getTypedBlockEntity<HTExtendedBlockEntity>(pos)?.neighborChanged(state, level, pos, block, orientation, movedByPiston)
    }

    final override fun getBlockEntityType(): HTDeferredBlockEntityType<*> = type
}
