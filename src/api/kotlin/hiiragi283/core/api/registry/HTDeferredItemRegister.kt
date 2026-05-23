package hiiragi283.core.api.registry

import hiiragi283.core.api.function.Identity
import hiiragi283.core.api.function.identity
import java.util.function.Function
import java.util.function.Supplier
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item

typealias ItemWithContextFactory<C, ITEM> = (C, Item.Properties) -> ITEM

class HTDeferredItemRegister(namespace: String) : HTDeferredRegister<Item>(Registries.ITEM, namespace) {
    fun <ITEM : Item> registerItem(name: String, supplier: Supplier<ITEM>): HTDeferredItem<ITEM> = this.register(name, supplier)

    fun <ITEM : Item> registerItem(name: String, factory: (Item.Properties) -> ITEM, operator: Identity<Item.Properties> = identity()): HTDeferredItem<ITEM> = this.register(name) { _ -> Item.Properties().let(operator).let(factory) }

    fun registerSimpleItem(name: String, operator: Identity<Item.Properties> = identity()): HTSimpleDeferredItem = this.registerItem(name, ::Item, operator)

    fun <ITEM : Item, C> registerItemWith(
        name: String,
        context: C,
        factory: ItemWithContextFactory<C, ITEM>,
        operator: Identity<Item.Properties> = identity(),
    ): HTDeferredItem<ITEM> = registerItem(name, { factory(context, it) }, operator)

    //    HTDeferredRegister    //

    override fun <I : Item> createHolder(registryKey: RegistryKey<Item>, key: ResourceLocation): HTDeferredItem<I> = HTDeferredItem(key)

    override fun <I : Item> register(name: String, sup: Supplier<out I>): HTDeferredItem<I> = super.register(name, sup) as HTDeferredItem<I>

    override fun <I : Item> register(name: String, func: Function<ResourceLocation, out I>): HTDeferredItem<I> = super.register(name, func) as HTDeferredItem<I>

    override fun asSequence(): Sequence<HTDeferredItem<*>> = super.asSequence().filterIsInstance<HTDeferredItem<*>>()
}
