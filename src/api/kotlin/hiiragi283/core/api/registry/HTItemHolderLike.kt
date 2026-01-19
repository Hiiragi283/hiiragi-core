package hiiragi283.core.api.registry

import hiiragi283.core.api.function.andThen
import hiiragi283.core.api.monad.Either
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.HTHasTranslationKey
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

/**
 * [HTIdLike]と[ItemLike]とその他諸々を継承した[HTIdLike]の拡張インターフェースです。
 * @param ITEM アイテムのクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
interface HTItemHolderLike<ITEM : Item> :
    HTIdLike,
    HTHasTranslationKey,
    HTHasText,
    ItemLike {
    companion object {
        @JvmField
        val KEY_CODEC: BiCodec<ByteBuf, HTItemHolderLike<*>> = VanillaBiCodecs
            .resourceKey(Registries.ITEM)
            .xmap(::Simple, HTItemHolderLike<*>::getItemKey)

        @JvmField
        val HOLDER_CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemHolderLike<*>> = VanillaBiCodecs
            .holder(Registries.ITEM)
            .xmap(Holder<Item>::value.andThen(::Simple), HTItemHolderLike<*>::getItemHolder)
    }

    fun getItemHolder(): Holder<Item>

    fun getItemKey(): ResourceKey<Item> = getItemHolder().unwrapKey().orElseThrow()

    override fun asItem(): ITEM

    /**
     * @since 0.7.0
     */
    interface Delegated<ITEM : Item> : HTItemHolderLike<ITEM> {
        override fun getId(): ResourceLocation = getItemKey().location()

        override val translationKey: String get() = asItem().descriptionId

        override fun getText(): Component = asItem().description
    }

    /**
     * @since 0.8.0
     */
    class Simple(private val contents: Either<ResourceKey<Item>, Item>) : Delegated<Item> {
        constructor(key: ResourceKey<Item>) : this(Either.Left(key))

        constructor(id: ResourceLocation) : this(Registries.ITEM.createKey(id))

        constructor(item: ItemLike) : this(Either.Right(item.asItem()))

        @Suppress("DEPRECATION")
        override fun getItemHolder(): Holder<Item> = contents.map(BuiltInRegistries.ITEM::getHolderOrThrow, Item::builtInRegistryHolder)

        override fun asItem(): Item = getItemHolder().value()
    }
}
