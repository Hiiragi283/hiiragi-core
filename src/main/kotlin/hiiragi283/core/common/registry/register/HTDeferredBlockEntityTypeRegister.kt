package hiiragi283.core.common.registry.register

import hiiragi283.core.api.registry.HTDeferredRegister
import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType

/**
 * Ragiumで使用される[BlockEntityType]向けの[HTDeferredRegister]の実装クラスです。
 */
@Suppress("TYPE_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class HTDeferredBlockEntityTypeRegister(namespace: String) : HTDeferredRegister<BlockEntityType<*>>(Registries.BLOCK_ENTITY_TYPE, namespace) {
    fun <BE : BlockEntity> registerType(name: String, factory: BlockEntityType.BlockEntitySupplier<BE>): HTDeferredBlockEntityType<BE> {
        val holder = HTDeferredBlockEntityType<BE>(createId(name))
        this.register(name) { _: ResourceLocation -> BlockEntityType.Builder.of(factory).build(null) }
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

    // With supported blocks
    fun <BE : BlockEntity> registerType(name: String, factory: BlockEntityType.BlockEntitySupplier<BE>, blockBuilder: MutableSet<Block>.() -> Unit): HTDeferredBlockEntityType<BE> {
        val holder = HTDeferredBlockEntityType<BE>(createId(name))
        this.register(name) { _: ResourceLocation -> BlockEntityType.Builder.of(factory, *buildSet(blockBuilder).toTypedArray()).build(null) }
        return holder
    }

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

    fun <BE : HTBlockEntity> registerTick(name: String, factory: BlockEntityType.BlockEntitySupplier<BE>, blockBuilder: MutableSet<Block>.() -> Unit): HTDeferredBlockEntityType<BE> = registerType(name, factory, HTBlockEntity::tickServer, HTBlockEntity::tickClient, blockBuilder)
}
