package hiiragi283.core.common.block.entity

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import com.lowdragmc.lowdraglib2.syncdata.holder.blockentity.ISyncPersistRPCBlockEntity
import com.lowdragmc.lowdraglib2.syncdata.storage.FieldManagedStorage
import com.lowdragmc.lowdraglib2.syncdata.storage.IManagedStorage
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

/**
 * Ragiumで使用される[BlockEntity]の拡張クラス
 */
abstract class HTExtendedBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    BlockEntity(type.get(), pos, state),
    ISyncPersistRPCBlockEntity {
    abstract fun createUI(holder: BlockUIMenuType.BlockUIHolder): ModularUI

    //    Save & Read    //

    private val syncStorage = FieldManagedStorage(this)

    final override fun getSyncStorage(): IManagedStorage = syncStorage

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun setBlockState(blockState: BlockState) {
        super.setBlockState(blockState)
        onUpdatedState(blockState)
    }

    final override fun setLevel(level: Level) {
        super.setLevel(level)
        onUpdateLevel(level, blockPos)
    }

    final override fun setRemoved() {
        super.setRemoved()
        val level: Level = this.level ?: return
        onRemove(level, blockPos)
    }

    //    HTContentListener    //

    protected fun setOnlySave() {
        setChanged(false)
    }

    override fun setChanged() {
        setChanged(true)
    }

    private var lastSaveTime: Long = 0

    /**
     * @see mekanism.common.tile.base.TileEntityUpdateable.setChanged
     */
    protected open fun setChanged(updateComparator: Boolean) {
        val level: Level = this.getLevel() ?: return
        val time: Long = level.gameTime
        if (lastSaveTime != time) {
            level.blockEntityChanged(blockPos)
            lastSaveTime = time
        }
        if (updateComparator && !level.isClientSide) markDirtyComparator()
    }

    protected open fun markDirtyComparator() {}

    //    Extensions    //

    /**
     * [BlockEntity.setBlockState]の後で呼び出されます。
     */
    open fun onUpdatedState(state: BlockState) {}

    /**
     * [BlockEntity.setLevel]の後で呼び出されます。
     */
    open fun onUpdateLevel(level: Level, pos: BlockPos) {}

    /**
     * [BlockEntity.setRemoved]の後で呼び出されます。
     */
    open fun onRemove(level: Level, pos: BlockPos) {}

    /**
     * ブロックのコンパレータ出力を返します。
     */
    open fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int = 0

    /**
     * 隣接ブロックが更新された時に呼び出されます。
     */
    open fun neighborChanged(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        neighborBlock: Block,
        neighborPos: BlockPos,
        movedByPiston: Boolean,
    ) {
    }
}
