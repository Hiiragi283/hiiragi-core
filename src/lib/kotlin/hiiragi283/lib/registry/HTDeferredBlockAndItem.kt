package hiiragi283.lib.registry

import hiiragi283.lib.item.HTBlockItem
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.resource.SupplierWithId
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block

typealias HTSimpleDeferredBlockAndItem = HTBasicDeferredBlockAndItem<Block>

typealias HTBasicDeferredBlockAndItem<BLOCK> = HTDeferredBlockAndItem<BLOCK, HTBlockItem<BLOCK>>

data class HTDeferredBlockAndItem<out BLOCK : Block, out ITEM : Item>(val blockHolder: HTDeferredBlock<BLOCK>, val itemHolder: HTDeferredItem<ITEM>) :
    SupplierWithId<BLOCK>,
    HTIdLike.Translatable by itemHolder,
    ItemLike by itemHolder {
    override fun get(): BLOCK = blockHolder.get()

    fun toTemplate(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): Result<ItemStackTemplate> = itemHolder.toTemplate(count, patch)

    fun toStack(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): ItemStack = itemHolder.toStack(count, patch)
}
