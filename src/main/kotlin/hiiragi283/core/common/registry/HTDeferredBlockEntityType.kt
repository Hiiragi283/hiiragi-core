package hiiragi283.core.common.registry

import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.registry.createKey
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

data class HTDeferredBlockEntityType<BE : BlockEntity>(private val key: ResourceKey<BlockEntityType<*>>) :
    HTHolderLike<BlockEntityType<*>, BlockEntityType<BE>> {
    constructor(id: ResourceLocation) : this(Registries.BLOCK_ENTITY_TYPE.createKey(id))

    override fun getResourceKey(): ResourceKey<BlockEntityType<*>> = key

    @Suppress("UNCHECKED_CAST")
    override fun get(): BlockEntityType<BE> {
        val rawType: BlockEntityType<*> = BuiltInRegistries.BLOCK_ENTITY_TYPE.get(key) ?: error("Trying to access unbound value: $key")
        return rawType as BlockEntityType<BE>
    }

    fun create(pos: BlockPos, state: BlockState): BE = get().create(pos, state) ?: error("Failed to create Block Entity at $pos")

    internal var clientTicker: BlockEntityTicker<in BE>? = null
    internal var serverTicker: BlockEntityTicker<in BE>? = null

    fun getTicker(isClient: Boolean): BlockEntityTicker<in BE>? = when (isClient) {
        true -> clientTicker
        false -> serverTicker
    }
}
