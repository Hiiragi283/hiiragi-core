package hiiragi283.core.common.registry.register

import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTDeferredRegister
import hiiragi283.core.api.registry.HTSimpleBlockHolderLike
import hiiragi283.core.impl.registry.HTDeferredHolderLike
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour

class HTDeferredBlockRegister(namespace: String) : HTDeferredRegister<Block>(Registries.BLOCK, namespace) {
    private val blockEntries: MutableCollection<HTBlockHolderLike<*>> = mutableSetOf()

    fun <BLOCK : Block> registerBlock(
        name: String,
        blockProp: BlockBehaviour.Properties,
        factory: BlockFactory<BLOCK>,
    ): HTBlockHolderLike<BLOCK> = delegate
        .register(name) { _: ResourceLocation -> factory(blockProp) }
        .let(::HTDeferredHolderLike)
        .also(blockEntries::add)

    fun registerSimpleBlock(name: String, blockProp: BlockBehaviour.Properties): HTSimpleBlockHolderLike = registerBlock(name, blockProp, ::Block)

    fun asBlockSequence(): Sequence<HTBlockHolderLike<*>> = blockEntries.asSequence()
}
