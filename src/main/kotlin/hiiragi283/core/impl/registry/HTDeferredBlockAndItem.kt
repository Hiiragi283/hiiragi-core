package hiiragi283.core.impl.registry

import hiiragi283.core.api.item.HTBlockItem
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.core.api.text.Text
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block

typealias HTSimpleDeferredBlockAndItem = HTBasicDeferredBlockAndItem<Block>

typealias HTBasicDeferredBlockAndItem<BLOCK> = HTDeferredBlockAndItem<BLOCK, HTBlockItem<BLOCK>>

class HTDeferredBlockAndItem<BLOCK : Block, ITEM : Item>(blockHolder: HTBlockHolderLike<BLOCK>, val itemHolder: HTItemHolderLike<ITEM>) :
    HTBlockHolderLike<BLOCK> by blockHolder,
    HTHasTranslationKey,
    HTHasText,
    ItemLike {
    override val translationKey: String get() = get().descriptionId

    override fun getText(): Text = get().name

    override fun asItem(): ITEM = itemHolder.asItem()
}
