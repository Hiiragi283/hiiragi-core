package hiiragi283.core.impl.block.entity

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.block.entity.HTAbstractBlockEntity
import hiiragi283.core.impl.registry.HTDeferredBlockEntityType
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.Connection
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.ProblemReporter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.TagValueOutput
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

/**
 * Ragiumで使用される[BlockEntity]の拡張クラス
 * @see mekanism.common.tile.base.TileEntityUpdateable
 */
abstract class HTExtendedBlockEntity(private val type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    BlockEntity(type.get(), pos, state),
    HTAbstractBlockEntity {
    fun getDeferredType(): HTDeferredBlockEntityType<*> = type

    //    Save & Read    //

    final override fun saveAdditional(output: ValueOutput) {
        super.saveAdditional(output)
        writeValue(output)
    }

    final override fun loadAdditional(input: ValueInput) {
        super.loadAdditional(input)
        readValue(input)
    }

    final override fun getUpdatePacket(): ClientboundBlockEntityDataPacket = ClientboundBlockEntityDataPacket.create(this)

    final override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag = getReducedUpdateTag(registries)

    final override fun onDataPacket(net: Connection, valueInput: ValueInput) {
        if (valueInput.keySet().isNotEmpty()) handleUpdateTag(valueInput)
    }

    fun sendUpdatePacket(level: ServerLevel) {
        if (isRemoved) return
        // val payload: HTUpdateBlockEntityPacket = HTUpdateBlockEntityPacket.create(this) ?: return
        // PacketDistributor.sendToPlayersTrackingChunk(level, ChunkPos(blockPos), payload)
    }

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

    protected open fun writeValue(output: ValueOutput) {}

    protected open fun readValue(input: ValueInput) {}

    /**
     * @see mekanism.common.tile.base.TileEntityUpdateable.getReducedUpdateTag
     */
    fun getReducedUpdateTag(provider: HolderLookup.Provider): CompoundTag {
        val reporter = ProblemReporter.ScopedCollector(this.problemPath(), HiiragiCoreAPI.LOGGER)
        val output: TagValueOutput = TagValueOutput.createWithContext(reporter, provider)
        initReducedUpdateTag(output)
        return output.buildResult()
    }

    open fun initReducedUpdateTag(output: ValueOutput) {}

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
