package hiiragi283.lib.block.entity

import com.mojang.logging.LogUtils
import hiiragi283.lib.network.HTUpdateBlockEntityPacket
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.Connection
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.ProblemReporter
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.redstone.Orientation
import net.minecraft.world.level.storage.TagValueOutput
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.network.PacketDistributor
import org.slf4j.Logger

abstract class HTExtendedBlockEntity(type: BlockEntityType<*>, worldPosition: BlockPos, blockState: BlockState) :
    BlockEntity(type, worldPosition, blockState),
    HTAbstractBlockEntity {
    companion object {
        @JvmField
        protected val LOGGER: Logger = LogUtils.getLogger()
    }

    //    Extensions    //

    protected open fun writeValue(output: ValueOutput) {}

    protected open fun readValue(input: ValueInput) {}

    fun createReporter(): ProblemReporter = ProblemReporter.ScopedCollector(this.problemPath(), LOGGER)

    fun createReducedUpdateTag(registries: HolderLookup.Provider): CompoundTag = TagValueOutput.createWithContext(createReporter(), registries)
        .also(::writeReducedUpdateTag)
        .buildResult()

    open fun writeReducedUpdateTag(output: ValueOutput) {}

    open fun readUpdateTag(input: ValueInput) {}

    fun sendUpdatePacket(level: ServerLevel) {
        if (isRemoved) return
        val payload: HTUpdateBlockEntityPacket = HTUpdateBlockEntityPacket.create(this) ?: return
        PacketDistributor.sendToPlayersTrackingChunk(level, ChunkPos.containing(blockPos), payload)
    }

    protected fun setOnlySave() {
        setChanged(false)
    }

    override fun setChanged() {
        setChanged(true)
    }

    private var lastSaveTime: Long = 0

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

    /**
     * ブロックのコンパレータ出力を返します。
     */
    open fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int = 0

    /**
     * 隣接ブロックが更新された時に呼び出されます。
     */
    open fun neighborChanged(state: BlockState, level: Level, pos: BlockPos, block: Block, orientation: Orientation?, movedByPiston: Boolean) {}

    //    BlockEntity    //

    final override fun saveAdditional(output: ValueOutput) {
        super.saveAdditional(output)
        writeValue(output)
    }

    final override fun loadAdditional(input: ValueInput) {
        super.loadAdditional(input)
        readValue(input)
    }

    final override fun getUpdatePacket(): ClientboundBlockEntityDataPacket = ClientboundBlockEntityDataPacket.create(this)

    final override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag = createReducedUpdateTag(registries)

    final override fun handleUpdateTag(input: ValueInput) {
        super.handleUpdateTag(input)
        readUpdateTag(input)
        requestModelDataUpdate()
    }

    override fun onDataPacket(net: Connection, valueInput: ValueInput) {
        super.onDataPacket(net, valueInput)
        if (!valueInput.keySet().isEmpty()) {
            readUpdateTag(valueInput)
        }
    }
}
