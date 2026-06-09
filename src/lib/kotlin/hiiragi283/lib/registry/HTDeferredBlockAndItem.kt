package hiiragi283.lib.registry

import hiiragi283.lib.item.HTBlockItem
import hiiragi283.lib.item.HTItemInstanceBuilder
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.resource.SupplierWithId
import hiiragi283.lib.util.HTTextResult
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

    inline fun toTemplate(builderAction: HTItemInstanceBuilder.() -> Unit = {}): HTTextResult<ItemStackTemplate> = itemHolder.toTemplate(builderAction)

    inline fun toStack(builderAction: HTItemInstanceBuilder.() -> Unit = {}): ItemStack = itemHolder.toStack(builderAction)
}
