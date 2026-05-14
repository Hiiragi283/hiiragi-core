package hiiragi283.lib.registry

import hiiragi283.lib.item.HTBlockItem
import hiiragi283.lib.item.HTItemLike
import hiiragi283.lib.resource.HTIdLike
import java.util.function.Supplier
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

typealias HTSimpleDeferredBlockAndItem = HTBasicDeferredBlockAndItem<Block>

typealias HTBasicDeferredBlockAndItem<BLOCK> = HTDeferredBlockAndItem<BLOCK, HTBlockItem<BLOCK>>

data class HTDeferredBlockAndItem<BLOCK : Block, ITEM : Item>(val blockHolder: HTDeferredBlock<BLOCK>, val itemHolder: HTDeferredItem<ITEM>) :
    Supplier<BLOCK> by blockHolder,
    HTIdLike.Translatable by itemHolder,
    HTItemLike<ITEM> by itemHolder
