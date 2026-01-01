package hiiragi283.core.common.registry.register

import hiiragi283.core.api.item.HTBlockItem
import hiiragi283.core.api.registry.BlockFactory
import hiiragi283.core.api.registry.HTDeferredHolder
import hiiragi283.core.api.registry.HTDoubleDeferredRegister
import hiiragi283.core.api.registry.ItemWithContextFactory
import hiiragi283.core.common.registry.HTBasicDeferredBlock
import hiiragi283.core.common.registry.HTDeferredBlock
import hiiragi283.core.common.registry.HTDeferredItem
import hiiragi283.core.common.registry.HTDeferredOnlyBlock
import hiiragi283.core.common.registry.HTSimpleDeferredBlock
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import java.util.function.UnaryOperator

/**
 * @see mekanism.common.registration.impl.BlockDeferredRegister
 */
class HTDeferredBlockRegister(
    override val firstRegister: HTDeferredOnlyBlockRegister,
    override val secondRegister: HTDeferredItemRegister,
) : HTDoubleDeferredRegister<Block, Item>(firstRegister, secondRegister) {
    constructor(namespace: String) : this(HTDeferredOnlyBlockRegister(namespace), HTDeferredItemRegister(namespace))

    // Simple
    fun registerSimple(
        name: String,
        blockProp: BlockBehaviour.Properties,
        itemProp: UnaryOperator<Item.Properties> = UnaryOperator.identity(),
    ): HTSimpleDeferredBlock = register(name, blockProp, ::Block, ::HTBlockItem, itemProp)

    fun <BLOCK : Block> registerSimple(
        name: String,
        blockProp: BlockBehaviour.Properties,
        blockGetter: BlockFactory<BLOCK>,
        itemProp: UnaryOperator<Item.Properties> = UnaryOperator.identity(),
    ): HTBasicDeferredBlock<BLOCK> = register(name, blockProp, blockGetter, ::HTBlockItem, itemProp)

    fun <BLOCK : Block> registerSimple(
        name: String,
        blockFactory: () -> BLOCK,
        itemProp: UnaryOperator<Item.Properties> = UnaryOperator.identity(),
    ): HTBasicDeferredBlock<BLOCK> = register(name, blockFactory, ::HTBlockItem, itemProp)

    // Basic
    fun <BLOCK : Block, ITEM : Item> register(
        name: String,
        blockProp: BlockBehaviour.Properties,
        blockFactory: BlockFactory<BLOCK>,
        itemFactory: ItemWithContextFactory<BLOCK, ITEM>,
        itemProp: UnaryOperator<Item.Properties> = UnaryOperator.identity(),
    ): HTDeferredBlock<BLOCK, ITEM> = register(
        name,
        { blockFactory(blockProp) },
        itemFactory,
        itemProp,
    )

    fun <BLOCK : Block, ITEM : Item> register(
        name: String,
        blockGetter: () -> BLOCK,
        itemFactory: ItemWithContextFactory<BLOCK, ITEM>,
        itemProp: UnaryOperator<Item.Properties> = UnaryOperator.identity(),
    ): HTDeferredBlock<BLOCK, ITEM> = registerAdvanced(
        name,
        { _: ResourceLocation -> blockGetter() },
        { block: HTDeferredHolder<Block, BLOCK> -> itemFactory(block.get(), itemProp.apply(Item.Properties())) },
        ::HTDeferredBlock,
    )

    fun asBlockSequence(): Sequence<HTDeferredOnlyBlock<*>> = firstRegister.asSequence()

    fun asItemSequence(): Sequence<HTDeferredItem<*>> = secondRegister.asSequence()
}
