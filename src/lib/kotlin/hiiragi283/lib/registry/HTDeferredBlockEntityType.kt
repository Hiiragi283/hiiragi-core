package hiiragi283.lib.registry

import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

/**
 * [BlockEntityType]向けの[HTDeferredHolder]の拡張クラスです。
 * @param BE [BlockEntity]のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTDeferredBlockEntityType<BE : BlockEntity>(key: ResourceKey<BlockEntityType<*>>) : HTDeferredHolder<BlockEntityType<*>, BlockEntityType<BE>>(key) {
    /**
     * 新しい[BlockEntity]のインスタンスを作成します。
     */
    fun create(pos: BlockPos, state: BlockState): BE = get().create(pos, state)

    internal var clientTicker: BlockEntityTicker<in BE>? = null
    internal var serverTicker: BlockEntityTicker<in BE>? = null

    /**
     * [BlockEntityTicker]を取得します。
     * @param isClient クライアント側の場合は`true`
     */
    fun getTicker(isClient: Boolean): BlockEntityTicker<in BE>? = when (isClient) {
        true -> clientTicker
        false -> serverTicker
    }
}
