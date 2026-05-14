package hiiragi283.lib.registry

import java.util.function.Function
import java.util.function.Supplier
import java.util.function.UnaryOperator
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item

typealias ItemWithContextFactory<C, ITEM> = (C, Item.Properties) -> ITEM

class HTDeferredItemRegister(namespace: String) : HTDeferredRegister<Item>(Registries.ITEM, namespace) {
    fun <ITEM : Item> registerItem(name: String, factory: (Item.Properties) -> ITEM, operator: UnaryOperator<Item.Properties> = UnaryOperator.identity()): HTDeferredItem<ITEM> = this.register(name) { id: Identifier -> Item.Properties().setId(createKey(id)).let(operator::apply).let(factory) }

    fun registerSimpleItem(name: String, operator: UnaryOperator<Item.Properties> = UnaryOperator.identity()): HTSimpleDeferredItem = this.registerItem(name, ::Item, operator)

    fun <ITEM : Item, C> registerItemWith(
        name: String,
        context: C,
        factory: ItemWithContextFactory<C, ITEM>,
        operator: UnaryOperator<Item.Properties> = UnaryOperator.identity(),
    ): HTDeferredItem<ITEM> = registerItem(name, { factory(context, it) }, operator)

    //    HTDeferredRegister    //

    override fun <I : Item> createHolder(registryKey: ResourceKey<out Registry<Item>>, key: Identifier): HTDeferredItem<I> = HTDeferredItem(key)

    override fun <I : Item> register(name: String, sup: Supplier<out I>): HTDeferredItem<I> = super.register(name, sup) as HTDeferredItem<I>

    override fun <I : Item> register(name: String, func: Function<Identifier, out I>): HTDeferredItem<I> = super.register(name, func) as HTDeferredItem<I>

    override fun asSequence(): Sequence<HTDeferredItem<*>> = super.asSequence().filterIsInstance<HTDeferredItem<*>>()
}
