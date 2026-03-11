package hiiragi283.core.common.registry.register

import hiiragi283.core.api.registry.BlockFactory
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTDeferredRegister
import hiiragi283.core.api.util.Either
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.registries.DeferredHolder

class HTDeferredBlockRegister(namespace: String) : HTDeferredRegister<Block>(Registries.BLOCK, namespace) {
    private val blockEntries: MutableCollection<HTBlockHolderLike<*>> = mutableSetOf()

    private fun <BLOCK : Block> wrapHolder(holder: DeferredHolder<Block, BLOCK>): HTBlockHolderLike<BLOCK> =
        object : HTBlockHolderLike<BLOCK> {
            override fun unwrap(): Either<ResourceKey<Block>, Holder<Block>> = Either.Right(holder.delegate)

            override fun get(): BLOCK = holder.get()

            override fun getId(): ResourceLocation = holder.id

            override fun toString(): String = holder.toString()
        }

    fun <BLOCK : Block> registerBlock(
        name: String,
        blockProp: BlockBehaviour.Properties,
        factory: BlockFactory<BLOCK>,
    ): HTBlockHolderLike<BLOCK> = delegate
        .register(name) { _: ResourceLocation -> factory(blockProp) }
        .let(::wrapHolder)
        .also(blockEntries::add)

    fun registerSimpleBlock(name: String, blockProp: BlockBehaviour.Properties): HTBlockHolderLike<Block> =
        registerBlock(name, blockProp, ::Block)

    fun asBlockSequence(): Sequence<HTBlockHolderLike<*>> = blockEntries.asSequence()
}
