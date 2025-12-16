package hiiragi283.core.common.registry

import hiiragi283.core.api.registry.HTDeferredHolder
import hiiragi283.core.api.registry.HTItemHolderLike
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item

typealias HTSimpleDeferredItem = HTDeferredItem<Item>

class HTDeferredItem<ITEM : Item> :
    HTDeferredHolder<Item, ITEM>,
    HTItemHolderLike<ITEM> {
    constructor(key: ResourceKey<Item>) : super(key)

    constructor(id: ResourceLocation) : super(Registries.ITEM, id)
}
