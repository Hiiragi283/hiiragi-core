package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.HTKeyLike
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

interface HTItemHolderLike<ITEM : Item> :
    HTHolderLike<Item, ITEM>,
    ItemLike {
    override fun asItem(): ITEM = get()

    fun interface HolderDelegate :
        HTItemHolderLike<Item>,
        HTKeyLike.HolderDelegate<Item> {
        override fun get(): Item = getHolder().value()
    }
}
