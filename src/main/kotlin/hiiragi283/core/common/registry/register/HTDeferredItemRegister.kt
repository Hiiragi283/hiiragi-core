package hiiragi283.core.common.registry.register

import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.registry.IdToFunction
import hiiragi283.core.api.registry.ItemFactory
import hiiragi283.core.api.registry.ItemWithContextFactory
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.registry.createKey
import hiiragi283.core.api.registry.register.HTDeferredRegister
import hiiragi283.core.common.registry.HTDeferredItem
import hiiragi283.core.common.registry.HTSimpleDeferredItem
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import java.util.function.Supplier
import java.util.function.UnaryOperator

/**
 * @see net.neoforged.neoforge.registries.DeferredRegister.Items
 */
class HTDeferredItemRegister(namespace: String) : HTDeferredRegister<Item>(Registries.ITEM, namespace) {
    fun <ITEM : Item> registerItem(
        name: String,
        factory: ItemFactory<ITEM>,
        operator: UnaryOperator<Item.Properties> = UnaryOperator.identity(),
    ): HTDeferredItem<ITEM> = register(name) { _: ResourceLocation -> factory(operator.apply(Item.Properties())) }

    fun registerSimpleItem(name: String, operator: UnaryOperator<Item.Properties> = UnaryOperator.identity()): HTSimpleDeferredItem =
        registerItem(name, ::Item, operator)

    fun <ITEM : Item, C> registerItemWith(
        name: String,
        context: C,
        factory: ItemWithContextFactory<C, ITEM>,
        operator: UnaryOperator<Item.Properties> = UnaryOperator.identity(),
    ): HTDeferredItem<ITEM> = registerItem(name, factory.partially1(context), operator)

    //    HTDeferredRegister    //

    override fun asSequence(): Sequence<HTDeferredItem<*>> = super.asSequence().filterIsInstance<HTDeferredItem<*>>()

    override fun <I : Item> register(name: String, func: IdToFunction<out I>): HTDeferredItem<I> =
        super.register(name, func) as HTDeferredItem<I>

    override fun <I : Item> register(name: String, sup: Supplier<out I>): HTDeferredItem<I> = super.register(name, sup) as HTDeferredItem<I>

    override fun <I : Item> createHolder(registryKey: RegistryKey<Item>, key: ResourceLocation): HTDeferredItem<I> =
        HTDeferredItem(registryKey.createKey(key))
}
