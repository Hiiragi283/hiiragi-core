package hiiragi283.core.common.registry.register

import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.registry.HTDeferredRegister
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.HTSimpleItemHolderLike
import hiiragi283.core.impl.registry.HTDeferredHolderLike
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredHolder
import java.util.function.Supplier
import java.util.function.UnaryOperator

typealias ItemWithContextFactory<C, ITEM> = (C, Item.Properties) -> ITEM

class HTDeferredItemRegister(namespace: String) : HTDeferredRegister<Item>(Registries.ITEM, namespace) {
    private val itemEntries: MutableCollection<HTItemHolderLike<*>> = mutableSetOf()

    fun <ITEM : Item> registerItem(name: String, factory: Supplier<ITEM>): HTItemHolderLike<ITEM> = delegate
        .register(name, factory)
        .let(::DeferredItemLike)
        .also(itemEntries::add)

    fun <ITEM : Item> registerItem(
        name: String,
        factory: (Item.Properties) -> ITEM,
        operator: UnaryOperator<Item.Properties> = UnaryOperator.identity(),
    ): HTItemHolderLike<ITEM> = delegate
        .register(name) { _: ResourceLocation -> factory(operator.apply(Item.Properties())) }
        .let(::DeferredItemLike)
        .also(itemEntries::add)

    fun registerSimpleItem(name: String, operator: UnaryOperator<Item.Properties> = UnaryOperator.identity()): HTSimpleItemHolderLike = registerItem(name, ::Item, operator)

    fun <ITEM : Item, C> registerItemWith(
        name: String,
        context: C,
        factory: ItemWithContextFactory<C, ITEM>,
        operator: UnaryOperator<Item.Properties> = UnaryOperator.identity(),
    ): HTItemHolderLike<ITEM> = registerItem(name, factory.partially1(context), operator)

    fun asItemSequence(): Sequence<HTItemHolderLike<*>> = itemEntries.asSequence()

    private class DeferredItemLike<ITEM : Item>(holder: DeferredHolder<Item, ITEM>) :
        HTDeferredHolderLike<Item, ITEM>(holder),
        HTItemHolderLike.Simple<ITEM>
}
