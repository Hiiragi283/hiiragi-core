package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.HTKeyLike
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.HTHasTranslationKey
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

interface HTItemHolderLike<ITEM : Item> :
    HTHolderLike<Item, ITEM>,
    HTHasTranslationKey,
    HTHasText,
    ItemLike {
    override val translationKey: String get() = get().descriptionId

    override fun getText(): Component = get().description

    override fun asItem(): ITEM = get()

    fun interface HolderDelegate :
        HTItemHolderLike<Item>,
        HTKeyLike.HolderDelegate<Item> {
        override fun get(): Item = getHolder().value()
    }
}
