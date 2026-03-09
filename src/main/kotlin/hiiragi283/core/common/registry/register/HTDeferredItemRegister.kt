package hiiragi283.core.common.registry.register

import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.registry.HTDeferredRegister
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.ItemFactory
import hiiragi283.core.api.registry.ItemWithContextFactory
import hiiragi283.core.api.registry.toItemLike
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredHolder
import java.util.function.Supplier
import java.util.function.UnaryOperator

class HTDeferredItemRegister(namespace: String) : HTDeferredRegister<Item>(Registries.ITEM, namespace) {
    private val itemEntries: MutableCollection<HTItemHolderLike<*>> = mutableSetOf()

    fun <ITEM : Item> registerItem(name: String, factory: Supplier<ITEM>): HTItemHolderLike<ITEM> = delegate
        .register(name, factory)
        .let(DeferredHolder<Item, ITEM>::toItemLike)
        .also(itemEntries::add)

    fun <ITEM : Item> registerItem(
        name: String,
        factory: ItemFactory<ITEM>,
        operator: UnaryOperator<Item.Properties> = UnaryOperator.identity(),
    ): HTItemHolderLike<ITEM> = delegate
        .register(name) { _: ResourceLocation -> factory(operator.apply(Item.Properties())) }
        .let(DeferredHolder<Item, ITEM>::toItemLike)
        .also(itemEntries::add)

    fun registerSimpleItem(name: String, operator: UnaryOperator<Item.Properties> = UnaryOperator.identity()): HTItemHolderLike<Item> =
        registerItem(name, ::Item, operator)

    fun <ITEM : Item, C> registerItemWith(
        name: String,
        context: C,
        factory: ItemWithContextFactory<C, ITEM>,
        operator: UnaryOperator<Item.Properties> = UnaryOperator.identity(),
    ): HTItemHolderLike<ITEM> = registerItem(name, factory.partially1(context), operator)

    fun asItemSequence(): Sequence<HTItemHolderLike<*>> = itemEntries.asSequence()
}
