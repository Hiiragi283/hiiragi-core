package hiiragi283.lib.registry

import hiiragi283.lib.item.HTItemInstanceBuilder
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.text.Text
import hiiragi283.lib.util.HTTextResult
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

    inline fun toTemplate(builderAction: HTItemInstanceBuilder.() -> Unit = {}): HTTextResult<ItemStackTemplate> = HTItemInstanceBuilder.buildTemplate {
        this.item += this@HTDeferredItem
        builderAction()
    }

    inline fun toStack(builderAction: HTItemInstanceBuilder.() -> Unit = {}): ItemStack = HTItemInstanceBuilder.buildStack {
        this.item += this@HTDeferredItem
        builderAction()
    }
}
