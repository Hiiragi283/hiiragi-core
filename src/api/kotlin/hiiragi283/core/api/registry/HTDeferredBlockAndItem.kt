package hiiragi283.core.api.registry

import hiiragi283.core.api.item.HTBlockItem
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.SupplierWithId
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block

typealias HTSimpleDeferredBlockAndItem = HTBasicDeferredBlockAndItem<Block>

typealias HTBasicDeferredBlockAndItem<BLOCK> = HTDeferredBlockAndItem<BLOCK, HTBlockItem<BLOCK>>

data class HTDeferredBlockAndItem<out BLOCK : Block, out ITEM : Item>(val blockHolder: HTDeferredBlock<BLOCK>, val itemHolder: HTDeferredItem<ITEM>) :
    SupplierWithId<BLOCK>,
    HTIdLike.Translatable by itemHolder,
    ItemLike by itemHolder {
    override fun get(): BLOCK = blockHolder.get()

    fun toStack(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): ItemStack = itemHolder.toStack(count, patch)
}
