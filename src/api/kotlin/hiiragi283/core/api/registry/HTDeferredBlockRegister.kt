package hiiragi283.core.api.registry

import java.util.function.Function
import java.util.function.Supplier
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour

typealias BlockFactory<BLOCK> = (BlockBehaviour.Properties) -> BLOCK

typealias BlockWithContextFactory<C, BLOCK> = (C, BlockBehaviour.Properties) -> BLOCK

class HTDeferredBlockRegister(namespace: String) : HTDeferredRegister<Block>(Registries.BLOCK, namespace) {
    fun <BLOCK : Block> registerBlock(name: String, blockProp: BlockBehaviour.Properties, factory: BlockFactory<BLOCK>): HTDeferredBlock<BLOCK> = this.register(name) { _ -> blockProp.let(factory) }

    fun <BLOCK : Block> registerBlock(name: String, blockProp: Supplier<BlockBehaviour.Properties>, factory: BlockFactory<BLOCK>): HTDeferredBlock<BLOCK> = this.register(name) { _ -> blockProp.get().let(factory) }

    fun registerSimpleBlock(name: String, blockProp: BlockBehaviour.Properties): HTSimpleDeferredBlock = this.registerBlock(name, blockProp, ::Block)

    fun registerSimpleBlock(name: String, blockProp: Supplier<BlockBehaviour.Properties>): HTSimpleDeferredBlock = this.registerBlock(name, blockProp, ::Block)

    //    HTDeferredRegister    //

    override fun <I : Block> createHolder(registryKey: RegistryKey<Block>, key: ResourceLocation): HTDeferredBlock<I> = HTDeferredBlock(key)

    override fun <I : Block> register(name: String, sup: Supplier<out I>): HTDeferredBlock<I> = super.register(name, sup) as HTDeferredBlock<I>

    override fun <I : Block> register(name: String, func: Function<ResourceLocation, out I>): HTDeferredBlock<I> = super.register(name, func) as HTDeferredBlock<I>

    override fun asSequence(): Sequence<HTDeferredBlock<*>> = super.asSequence().filterIsInstance<HTDeferredBlock<*>>()
}
