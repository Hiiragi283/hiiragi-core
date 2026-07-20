package hiiragi283.core.api.registry

import hiiragi283.core.api.item.HTBlockItem
import hiiragi283.core.api.util.Identity
import hiiragi283.core.api.util.identity
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
        val blockHolder: HTDeferredBlock<BLOCK> = blockRegister.registerBlock(name, blockProp, blockFactory)
        val itemHolder: HTDeferredItem<ITEM> = itemRegister.registerItem(
            name,
            { prop: Item.Properties -> itemFactory(blockHolder.get(), prop) },
            itemProp,
        )
        return HTDeferredBlockAndItem(blockHolder, itemHolder)
    }

    fun asBlockSequence(): Sequence<HTDeferredBlock<*>> = blockRegister.asSequence()

    fun asItemSequence(): Sequence<HTDeferredItem<*>> = itemRegister.asSequence()

    fun register(bus: IEventBus) {
        blockRegister.register(bus)
        itemRegister.register(bus)
    }
}
