package hiiragi283.core.common.registry.register

import hiiragi283.core.api.block.type.HTBlockType
import hiiragi283.core.api.item.HTBlockItem
import hiiragi283.core.api.registry.BlockFactory
import hiiragi283.core.api.registry.BlockWithContextFactory
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
        itemProp: Item.Properties = Item.Properties(),
    ): HTSimpleDeferredBlock = register(name, blockProp, ::Block, ::HTBlockItem, itemProp)

    fun <BLOCK : Block> registerSimple(
        name: String,
        blockProp: BlockBehaviour.Properties,
        blockGetter: BlockFactory<BLOCK>,
        itemProp: Item.Properties = Item.Properties(),
    ): HTBasicDeferredBlock<BLOCK> = register(name, blockProp, blockGetter, ::HTBlockItem, itemProp)

    fun <BLOCK : Block> registerSimple(
        name: String,
        blockFactory: () -> BLOCK,
        itemProp: Item.Properties = Item.Properties(),
    ): HTBasicDeferredBlock<BLOCK> = register(name, blockFactory, ::HTBlockItem, itemProp)

    // Type
    fun <TYPE : HTBlockType, BLOCK : Block> registerSimpleTyped(
        name: String,
        blockType: TYPE,
        blockProp: BlockBehaviour.Properties,
        blockFactory: BlockWithContextFactory<TYPE, BLOCK>,
    ): HTBasicDeferredBlock<BLOCK> = registerTyped(name, blockType, blockProp, blockFactory, ::HTBlockItem)

    fun <TYPE : HTBlockType, BLOCK : Block, ITEM : Item> registerTyped(
        name: String,
        blockType: TYPE,
        blockProp: BlockBehaviour.Properties,
        blockFactory: BlockWithContextFactory<TYPE, BLOCK>,
        itemFactory: ItemWithContextFactory<BLOCK, ITEM>,
        itemProp: Item.Properties = Item.Properties(),
    ): HTDeferredBlock<BLOCK, ITEM> = register(
        name,
        { blockFactory(blockType, blockProp) },
        itemFactory,
        itemProp,
    )

    // Basic
    fun <BLOCK : Block, ITEM : Item> register(
        name: String,
        blockProp: BlockBehaviour.Properties,
        blockFactory: BlockFactory<BLOCK>,
        itemFactory: ItemWithContextFactory<BLOCK, ITEM>,
        itemProp: Item.Properties = Item.Properties(),
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
        itemProp: Item.Properties = Item.Properties(),
    ): HTDeferredBlock<BLOCK, ITEM> = registerAdvanced(
        name,
        { _: ResourceLocation -> blockGetter() },
        { block: HTDeferredHolder<Block, BLOCK> -> itemFactory(block.get(), itemProp) },
        ::HTDeferredBlock,
    )

    fun asBlockSequence(): Sequence<HTDeferredOnlyBlock<*>> = firstRegister.asSequence()

    fun asItemSequence(): Sequence<HTDeferredItem<*>> = secondRegister.asSequence()
}
