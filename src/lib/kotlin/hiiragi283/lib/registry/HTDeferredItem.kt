package hiiragi283.lib.registry

import hiiragi283.lib.item.createItemTemplate
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.text.Text
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.ItemLike

typealias HTSimpleDeferredItem = HTDeferredItem<Item>

class HTDeferredItem<out ITEM : Item> :
    HTDeferredHolder<Item, ITEM>,
    HTIdLike.Translatable,
    ItemLike {
    constructor(key: ResourceKey<Item>) : super(key)

    constructor(id: Identifier) : super(Registries.ITEM.createKey(id))

    override val translationKey: String get() = get().descriptionId

    override fun getText(): Text = this.toStack().itemName

    override fun asItem(): ITEM = get()

    fun toTemplate(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): Result<ItemStackTemplate> = createItemTemplate(this, count, patch)

    fun toStack(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): ItemStack = ItemStack(this, count, patch)
}
