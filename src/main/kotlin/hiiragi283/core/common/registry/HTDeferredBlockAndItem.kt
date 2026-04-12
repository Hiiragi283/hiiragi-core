package hiiragi283.core.common.registry

import hiiragi283.core.api.item.HTBlockItem
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.core.api.text.Text
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block

typealias HTSimpleDeferredBlockAndItem = HTBasicDeferredBlockAndItem<Block>

typealias HTBasicDeferredBlockAndItem<BLOCK> = HTDeferredBlockAndItem<BLOCK, HTBlockItem<BLOCK>>

class HTDeferredBlockAndItem<BLOCK : Block, ITEM : Item>(blockHolder: HTBlockHolderLike<BLOCK>, val itemHolder: HTItemHolderLike<ITEM>) :
    HTBlockHolderLike<BLOCK> by blockHolder,
    HTHasTranslationKey,
    HTHasText,
    ItemLike {
    fun isOf(item: Item): Boolean = itemHolder.isOf(item)

    fun isOf(stack: ItemStack): Boolean = itemHolder.isOf(stack)

    fun toStack(count: Int = 1): ItemStack = itemHolder.toStack(count)

    fun toResource(): HTItemResourceType? = itemHolder.toResource()

    fun toResource(patch: DataComponentPatch): HTItemResourceType? = itemHolder.toResource(patch)

    override val translationKey: String get() = get().descriptionId

    override fun getText(): Text = get().name

    override fun asItem(): ITEM = itemHolder.asItem()
}
