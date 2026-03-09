package hiiragi283.core.common.registry.register

import hiiragi283.core.api.item.HTBlockItem
import hiiragi283.core.api.registry.BlockFactory
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.ItemWithContextFactory
import hiiragi283.core.common.registry.HTBasicDeferredBlockAndItem
import hiiragi283.core.common.registry.HTDeferredBlockAndItem
import hiiragi283.core.common.registry.HTSimpleDeferredBlockAndItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.bus.api.IEventBus
import java.util.function.UnaryOperator

class HTDeferredBlockAndItemRegister(private val blockRegister: HTDeferredBlockRegister, private val itemRegister: HTDeferredItemRegister) {
    constructor(namespace: String) : this(HTDeferredBlockRegister(namespace))

    constructor(blockRegister: HTDeferredBlockRegister) : this(blockRegister, HTDeferredItemRegister(blockRegister.namespace))

    fun registerSimple(
        name: String,
        blockProp: BlockBehaviour.Properties,
        itemProp: UnaryOperator<Item.Properties> = UnaryOperator.identity(),
    ): HTSimpleDeferredBlockAndItem = registerSimple(name, blockProp, ::Block, itemProp)

    fun <BLOCK : Block> registerSimple(
        name: String,
        blockProp: BlockBehaviour.Properties,
        blockFactory: BlockFactory<BLOCK>,
        itemProp: UnaryOperator<Item.Properties> = UnaryOperator.identity(),
    ): HTBasicDeferredBlockAndItem<BLOCK> = register(name, blockProp, blockFactory, ::HTBlockItem, itemProp)

    fun <BLOCK : Block, ITEM : Item> register(
        name: String,
        blockProp: BlockBehaviour.Properties,
        blockFactory: BlockFactory<BLOCK>,
        itemFactory: ItemWithContextFactory<BLOCK, ITEM>,
        itemProp: UnaryOperator<Item.Properties> = UnaryOperator.identity(),
    ): HTDeferredBlockAndItem<BLOCK, ITEM> {
        val blockHolder: HTBlockHolderLike<BLOCK> = blockRegister.registerBlock(name, blockProp, blockFactory)
        val itemHolder: HTItemHolderLike<ITEM> = itemRegister.registerItem(
            name,
            { prop: Item.Properties -> itemFactory(blockHolder.get(), prop) },
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
