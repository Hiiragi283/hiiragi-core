package hiiragi283.core.common.registry

import hiiragi283.core.api.function.Identity
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.item.HTBlockItem
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.bus.api.IEventBus

class HTDeferredBlockAndItemRegister(private val blockRegister: HTDeferredBlockRegister, private val itemRegister: HTDeferredItemRegister) {
    constructor(namespace: String) : this(HTDeferredBlockRegister(namespace))

    constructor(blockRegister: HTDeferredBlockRegister) : this(blockRegister, HTDeferredItemRegister(blockRegister.namespace))

    fun registerSimple(
        name: String,
        blockProp: BlockBehaviour.Properties,
        itemProp: Identity<Item.Properties> = identity(),
    ): HTSimpleDeferredBlockAndItem = registerSimple(name, blockProp, ::Block, itemProp)

    fun <BLOCK : Block> registerSimple(
        name: String,
        blockProp: BlockBehaviour.Properties,
        blockFactory: BlockFactory<BLOCK>,
        itemProp: Identity<Item.Properties> = identity(),
    ): HTBasicDeferredBlockAndItem<BLOCK> = register(name, blockProp, blockFactory, ::HTBlockItem, itemProp)

    fun <BLOCK : Block, ITEM : Item> register(
        name: String,
        blockProp: BlockBehaviour.Properties,
        blockFactory: BlockFactory<BLOCK>,
        itemFactory: ItemWithContextFactory<BLOCK, ITEM>,
        itemProp: Identity<Item.Properties> = identity(),
    ): HTDeferredBlockAndItem<BLOCK, ITEM> {
        val blockHolder: HTBlockHolderLike<BLOCK> = blockRegister.registerBlock(name, blockProp, blockFactory)
        val itemHolder: HTItemHolderLike<ITEM> = itemRegister.registerItem(
            name,
            { prop: Item.Properties -> itemFactory(blockHolder.get(), prop.useBlockDescriptionPrefix()) },
            itemProp,
        )
        return HTDeferredBlockAndItem(blockHolder, itemHolder)
    }

    fun asBlockSequence(): Sequence<HTBlockHolderLike<*>> = blockRegister.asBlockSequence()

    fun asItemSequence(): Sequence<HTItemHolderLike<*>> = itemRegister.asItemSequence()

    fun register(bus: IEventBus) {
        blockRegister.register(bus)
        itemRegister.register(bus)
    }
}
