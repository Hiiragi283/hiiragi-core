package hiiragi283.lib.registry

import hiiragi283.lib.block.entity.HTBlockEntity
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType

/**
 * Ragiumで使用される[BlockEntityType]向けの[HTDeferredRegister]の実装クラスです。
 */
class HTDeferredBlockEntityTypeRegister(namespace: String) : HTDeferredRegister<BlockEntityType<*>>(Registries.BLOCK_ENTITY_TYPE, namespace) {
    fun <BE : BlockEntity> registerType(name: String, factory: BlockEntityType.BlockEntitySupplier<BE>): HTDeferredBlockEntityType<BE> {
        val holder = HTDeferredBlockEntityType<BE>(createId(name))
        this.register(name) { _ -> BlockEntityType(factory, setOf()) }
        return holder
    }

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

    fun <BE : HTBlockEntity> registerTick(name: String, factory: BlockEntityType.BlockEntitySupplier<BE>): HTDeferredBlockEntityType<BE> = registerType(name, factory, HTBlockEntity::tickServer, HTBlockEntity::tickClient)
}
