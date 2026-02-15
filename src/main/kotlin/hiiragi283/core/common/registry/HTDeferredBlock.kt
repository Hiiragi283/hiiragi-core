package hiiragi283.core.common.registry

import hiiragi283.core.api.item.HTBlockItem
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTDeferredHolder
import hiiragi283.core.api.registry.HTDoubleDeferredHolder
import hiiragi283.core.api.registry.HTItemHolderLike
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

typealias HTBasicDeferredBlock<BLOCK> = HTDeferredBlock<BLOCK, HTBlockItem<BLOCK>>

typealias HTSimpleDeferredBlock = HTBasicDeferredBlock<Block>

class HTDeferredBlock<BLOCK : Block, ITEM : Item>(first: HTDeferredOnlyBlock<BLOCK>, second: HTDeferredItem<ITEM>) :
    HTDoubleDeferredHolder<Block, BLOCK, Item, ITEM>(
        first,
        second,
    ),
    HTBlockHolderLike<BLOCK>,
    HTItemHolderLike<ITEM> {
    constructor(first: HTDeferredHolder<Block, BLOCK>, second: HTDeferredHolder<Item, ITEM>) : this(
        HTDeferredOnlyBlock(first.id),
        HTDeferredItem(second.id),
    )

    constructor(id: ResourceLocation) : this(HTDeferredOnlyBlock(id), HTDeferredItem(id))

    val itemHolder: HTDeferredItem<ITEM> = second

    override fun asBlock(): BLOCK = get()

    override fun getBlockHolder(): Holder<Block> = delegate

    override fun asItem(): ITEM = getSecond()

    override fun getItemHolder(): Holder<Item> = second.delegate

    override val translationKey: String get() = get().descriptionId

    override fun getText(): Component = get().name
}
