package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.HTKeyLike
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.HTHasTranslationKey
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

fun interface HTItemHolderLike<ITEM : Item> :
    HTHolderLike<Item, ITEM>,
    HTKeyLike.HolderDelegate<Item>,
    HTHasTranslationKey,
    HTHasText,
    ItemLike {
    override val translationKey: String get() = get().descriptionId

    override fun getText(): Component = get().description

    @Suppress("DEPRECATION")
    override fun getHolder(): Holder<Item> = get().builtInRegistryHolder()

    override fun asItem(): ITEM = get()
}
