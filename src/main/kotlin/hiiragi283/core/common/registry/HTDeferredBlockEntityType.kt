package hiiragi283.core.common.registry

import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class HTDeferredBlockEntityType<BE : BlockEntity> : HTBasicHolderLike<BlockEntityType<*>, BlockEntityType<BE>> {
    constructor(key: ResourceKey<BlockEntityType<*>>) : super(key)

    constructor(id: Identifier) : super(Registries.BLOCK_ENTITY_TYPE, id)

    @Suppress("UNCHECKED_CAST")
    override fun get(): BlockEntityType<BE> = BuiltInRegistries.BLOCK_ENTITY_TYPE.getValueOrThrow(key) as BlockEntityType<BE>

    fun create(pos: BlockPos, state: BlockState): BE = get().create(pos, state)

    internal var clientTicker: BlockEntityTicker<in BE>? = null
    internal var serverTicker: BlockEntityTicker<in BE>? = null

    fun getTicker(isClient: Boolean): BlockEntityTicker<in BE>? = when (isClient) {
        true -> clientTicker
        false -> serverTicker
    }

    override fun toString(): String = "HTDeferredBlockEntityType(key=$key)"
}
