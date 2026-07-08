package hiiragi283.lib.block.entity

import hiiragi283.lib.HTConstants
import hiiragi283.lib.serialization.readOption
import hiiragi283.lib.text.Text
import hiiragi283.lib.transfer.indices
import hiiragi283.lib.transfer.item.ItemResourceHandler
import hiiragi283.lib.world.HTItemDropHelper
import java.util.UUID
import net.minecraft.core.BlockPos
import net.minecraft.core.UUIDUtil
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Nameable
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.item.ItemUtil

/**
 * [HTExtendedBlockEntity]の拡張クラスです。
 *
 * 参考 : [Mekanism - TileEntityMekanism](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/common/tile/base/TileEntityMekanism.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTBlockEntity(type: BlockEntityType<*>, pos: BlockPos, blockState: BlockState) :
    HTExtendedBlockEntity(type, pos, blockState),
    Nameable,
    HTOwnedBlockEntity,
    HTSoundPlayerBlockEntity {
    //    Ticking    //

    companion object {
        @JvmStatic
        fun tickClient(
            level: Level,
            pos: BlockPos,
            state: BlockState,
            blockEntity: HTBlockEntity,
        ) {
            blockEntity.onUpdateClient(level, pos, state)
            blockEntity.ticks++
        }

        @JvmStatic
        fun tickServer(
            level: Level,
            pos: BlockPos,
            state: BlockState,
            blockEntity: HTBlockEntity,
        ) {
            val serverLevel: ServerLevel = level as? ServerLevel ?: return
            val shouldUpdate: Boolean = blockEntity.onUpdateServer(serverLevel, pos, state)
            blockEntity.ticks++
            if (shouldUpdate) {
                blockEntity.sendUpdatePacket(serverLevel)
            }
        }
    }

    var ticks: Int = 0
        protected set

    /**
     * クライアント側でのティック処理を行います。
     */
    protected open fun onUpdateClient(level: Level, pos: BlockPos, state: BlockState) {}

    /**
     * サーバー側でのティック処理を行います。
     * @return クライアント側へ更新を同期する場合は`true`
     */
    protected abstract fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean

    //    Save & Read    //

    override fun writeValue(output: ValueOutput) {
        super.writeValue(output)
        // Custom Name
        output.storeNullable("custom_name", ComponentSerialization.CODEC, this.customName)
        // Owner
        output.storeNullable(HTConstants.OWNER, UUIDUtil.CODEC, ownerId)
    }

    override fun readValue(input: ValueInput) {
        super.readValue(input)
        // Custom Name
        input.readOption("custom_name", ComponentSerialization.CODEC).onSome(::customName::set)
        // Owner
        input.readOption(HTConstants.OWNER, UUIDUtil.CODEC).onSome(::ownerId::set)
    }

    //    Nameable    //

    private var customName: Text? = null

    final override fun getName(): Text = customName ?: blockState.block.name

    final override fun getCustomName(): Text? = customName

    //    HTOwnedBlockEntity    //

    var ownerId: UUID? = null

    override fun getOwner(): UUID? = ownerId

    //    Capability    //

    override fun preRemoveSideEffects(pos: BlockPos, state: BlockState) {
        val level: Level = this.level ?: return
        onBlockRemoved(state, level, pos)
    }

    /**
     * ブロックが削除されたときに呼び出されます。
     */
    open fun onBlockRemoved(state: BlockState, level: Level, pos: BlockPos) {}

    /**
     * アイテムをドロップします。
     * @since 26.1.3
     */
    protected fun dropItems(level: Level, pos: BlockPos, handler: ItemResourceHandler, range: Iterable<Int> = handler.indices) {
        for (i: Int in range) {
            ItemUtil.getStack(handler, i).let { HTItemDropHelper.dropStackAt(level, pos, it) }
        }
    }
}
