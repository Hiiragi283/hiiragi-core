package hiiragi283.core.common.registry

import hiiragi283.core.api.registry.HTDeferredHolder
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.HTHasTranslationKey
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

typealias HTSimpleDeferredItem = HTDeferredItem<Item>

class HTDeferredItem<ITEM : Item> :
    HTDeferredHolder<Item, ITEM>,
    ItemLike,
    HTHasTranslationKey,
    HTHasText {
    constructor(key: ResourceKey<Item>) : super(key)

    constructor(key: RegistryKey<Item>, id: ResourceLocation) : super(key, id)

    override fun asItem(): ITEM = get()

    override val translationKey: String get() = get().descriptionId

    override fun getText(): Component = get().description
}
