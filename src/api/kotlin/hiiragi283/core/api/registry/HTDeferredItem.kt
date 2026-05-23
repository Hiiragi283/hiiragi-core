package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.text.Text
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

typealias HTSimpleDeferredItem = HTDeferredItem<Item>

class HTDeferredItem<out ITEM : Item> :
    HTDeferredHolder<Item, ITEM>,
    HTIdLike.Translatable,
    ItemLike {
    constructor(key: ResourceKey<Item>) : super(key)

    constructor(id: ResourceLocation) : super(Registries.ITEM.createKey(id))

    override val translationKey: String get() = get().descriptionId

    override fun getText(): Text = get().description

    override fun asItem(): ITEM = get()

    fun toStack(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): ItemStack = ItemStack(this, count, patch)
}
