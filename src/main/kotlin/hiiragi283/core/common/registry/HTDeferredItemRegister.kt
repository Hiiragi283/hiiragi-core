package hiiragi283.core.common.registry

import hiiragi283.core.api.function.Identity
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.registry.HTDeferredRegister
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.HTSimpleItemHolderLike
import hiiragi283.core.api.util.Either
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredHolder

typealias ItemWithContextFactory<C, ITEM> = (C, Item.Properties) -> ITEM

class HTDeferredItemRegister(namespace: String) : HTDeferredRegister<Item>(Registries.ITEM, namespace) {
    private val itemEntries: MutableCollection<HTItemHolderLike<*>> = mutableSetOf()

    private fun <ITEM : Item> wrapHolder(holder: DeferredHolder<Item, ITEM>): HTItemHolderLike<ITEM> =
        object : HTItemHolderLike.Simple<ITEM> {
            override fun unwrap(): Either<ResourceKey<Item>, Holder<Item>> = Either.Right(holder.delegate)

            override fun get(): ITEM = holder.get()

            override fun getId(): Identifier = holder.id
        }

    fun <ITEM : Item> registerItem(
        name: String,
        factory: (Item.Properties) -> ITEM,
        operator: Identity<Item.Properties> = identity(),
    ): HTItemHolderLike<ITEM> = delegate
        .register(name) { _: Identifier ->
            Item
                .Properties()
                .setId(createKey(name))
                .let(operator)
                .let(factory)
        }.let(::wrapHolder)
        .also(itemEntries::add)

    fun registerSimpleItem(name: String, operator: Identity<Item.Properties> = identity()): HTSimpleItemHolderLike =
        registerItem(name, ::Item, operator)

    fun <ITEM : Item, C> registerItemWith(
        name: String,
        context: C,
        factory: ItemWithContextFactory<C, ITEM>,
        operator: Identity<Item.Properties> = identity(),
    ): HTItemHolderLike<ITEM> = registerItem(name, factory.partially1(context), operator)

    fun asItemSequence(): Sequence<HTItemHolderLike<*>> = itemEntries.asSequence()
}
