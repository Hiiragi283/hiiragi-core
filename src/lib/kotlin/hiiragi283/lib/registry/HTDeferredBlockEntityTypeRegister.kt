package hiiragi283.lib.registry

import hiiragi283.lib.block.entity.HTBlockEntity
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType

/**
 * [BlockEntityType]向けの[HTDeferredRegister]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTDeferredBlockEntityTypeRegister(namespace: String) : HTDeferredRegister<BlockEntityType<*>>(Registries.BLOCK_ENTITY_TYPE, namespace) {
    /**
     * 新しい[BlockEntityType]を登録します。
     * @param BE [BlockEntity]のクラス
     * @param name [BlockEntity]のIDのパス
     * @param factory [BlockEntity]を作成するブロック
     * @return 新しい[HTDeferredBlockEntityType]のインスタンス
     */
    fun <BE : BlockEntity> registerType(name: String, factory: BlockEntityType.BlockEntitySupplier<BE>): HTDeferredBlockEntityType<BE> {
        val holder = HTDeferredBlockEntityType<BE>(createKey(name))
        this.register(name) { _ -> BlockEntityType(factory, setOf()) }
        return holder
    }

    /**
     * tick処理付きの新しい[BlockEntityType]を登録します。
     * @param BE [BlockEntity]のクラス
     * @param name [BlockEntity]のIDのパス
     * @param factory [BlockEntity]を作成するブロック
     * @param serverTicker サーバー側のtick処理
     * @param clientTicker クライアント側のtick処理
     * @return 新しい[HTDeferredBlockEntityType]のインスタンス
     */
    fun <BE : BlockEntity> registerType(
        name: String,
        factory: BlockEntityType.BlockEntitySupplier<BE>,
        serverTicker: BlockEntityTicker<in BE>?,
        clientTicker: BlockEntityTicker<in BE>? = null,
    ): HTDeferredBlockEntityType<BE> {
        val holder: HTDeferredBlockEntityType<BE> = registerType(name, factory)
        holder.clientTicker = clientTicker
        holder.serverTicker = serverTicker
        return holder
    }

    /**
     * tick処理付きの新しい[BlockEntityType]を登録します。
     * @param BE [BlockEntity]のクラス
     * @param name [BlockEntity]のIDのパス
     * @param factory [BlockEntity]を作成するブロック
     * @return 新しい[HTDeferredBlockEntityType]のインスタンス
     */
    fun <BE : HTBlockEntity> registerTick(name: String, factory: BlockEntityType.BlockEntitySupplier<BE>): HTDeferredBlockEntityType<BE> = registerType(name, factory, HTBlockEntity::tickServer, HTBlockEntity::tickClient)

    // With supported blocks
    /**
     * 新しい[BlockEntityType]を登録します。
     * @param BE [BlockEntity]のクラス
     * @param name [BlockEntity]のIDのパス
     * @param factory [BlockEntity]を作成するブロック
     * @param blockBuilder この[BlockEntity]を作成可能なブロックの一覧
     * @return 新しい[HTDeferredBlockEntityType]のインスタンス
     */
    fun <BE : BlockEntity> registerType(name: String, factory: BlockEntityType.BlockEntitySupplier<BE>, blockBuilder: MutableSet<Block>.() -> Unit): HTDeferredBlockEntityType<BE> {
        val holder = HTDeferredBlockEntityType<BE>(createKey(name))
        this.register(name) { _ -> BlockEntityType(factory, buildSet(blockBuilder)) }
        return holder
    }

    /**
     * tick処理付きの新しい[BlockEntityType]を登録します。
     * @param BE [BlockEntity]のクラス
     * @param name [BlockEntity]のIDのパス
     * @param factory [BlockEntity]を作成するブロック
     * @param serverTicker サーバー側のtick処理
     * @param clientTicker クライアント側のtick処理
     * @param blockBuilder この[BlockEntity]を作成可能なブロックの一覧
     * @return 新しい[HTDeferredBlockEntityType]のインスタンス
     */
    fun <BE : BlockEntity> registerType(
        name: String,
        factory: BlockEntityType.BlockEntitySupplier<BE>,
        serverTicker: BlockEntityTicker<in BE>?,
        clientTicker: BlockEntityTicker<in BE>? = null,
        blockBuilder: MutableSet<Block>.() -> Unit,
    ): HTDeferredBlockEntityType<BE> {
        val holder: HTDeferredBlockEntityType<BE> = registerType(name, factory, blockBuilder)
        holder.clientTicker = clientTicker
        holder.serverTicker = serverTicker
        return holder
    }

    /**
     * tick処理付きの新しい[BlockEntityType]を登録します。
     * @param BE [BlockEntity]のクラス
     * @param name [BlockEntity]のIDのパス
     * @param factory [BlockEntity]を作成するブロック
     * @param blockBuilder この[BlockEntity]を作成可能なブロックの一覧
     * @return 新しい[HTDeferredBlockEntityType]のインスタンス
     */
    fun <BE : HTBlockEntity> registerTick(name: String, factory: BlockEntityType.BlockEntitySupplier<BE>, blockBuilder: MutableSet<Block>.() -> Unit): HTDeferredBlockEntityType<BE> = registerType(name, factory, HTBlockEntity::tickServer, HTBlockEntity::tickClient, blockBuilder)
}
