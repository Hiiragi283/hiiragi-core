package hiiragi283.lib.registry

import hiiragi283.lib.item.HTItemLike
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.text.Text
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item

typealias HTSimpleDeferredItem = HTDeferredItem<Item>

class HTDeferredItem<out ITEM : Item> :
    HTDeferredHolder<Item, ITEM>,
    HTIdLike.Translatable,
    HTItemLike<ITEM> {
    constructor(key: ResourceKey<Item>) : super(key)

    constructor(id: Identifier) : super(Registries.ITEM.createKey(id))

    override val translationKey: String get() = get().descriptionId

    override fun getText(): Text = this.toStack().itemName

    override fun asItem(): ITEM = get()
}
