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
import net.minecraft.world.item.ItemStack
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
            .xmap(::of, HTItemHolderLike<*>::getItemKey)

        @JvmField
        val HOLDER_CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemHolderLike<*>> = VanillaBiCodecs
            .holder(Registries.ITEM)
            .xmap(Holder<Item>::value.andThen(::of), HTItemHolderLike<*>::getItemHolder)

        /**
         * 指定した[id]から[HTItemHolderLike]の新しいインスタンスを作成します。
         * @since 0.8.0
         */
        @JvmStatic
        fun of(id: ResourceLocation): HTItemHolderLike<*> = of(Registries.ITEM.createKey(id))

        /**
         * 指定した[key]から[HTItemHolderLike]の新しいインスタンスを作成します。
         * @since 0.8.0
         */
        @JvmStatic
        fun of(key: ResourceKey<Item>): HTItemHolderLike<*> = Simple(Either.Left(key))

        /**
         * 指定した[item]から[HTItemHolderLike]の新しいインスタンスを作成します。
         * @since 0.8.0
         */
        @JvmStatic
        fun of(item: ItemLike): HTItemHolderLike<*> = Simple(Either.Right(item.asItem()))
    }

    fun getItemHolder(): Holder<Item>

    fun getItemKey(): ResourceKey<Item> = getItemHolder().unwrapKey().orElseThrow()

    override fun asItem(): ITEM

    fun toStack(count: Int = 1): ItemStack = ItemStack(this, count)

    /**
     * @since 0.7.0
     */
    interface Delegated<ITEM : Item> : HTItemHolderLike<ITEM> {
        override fun getId(): ResourceLocation = getItemKey().location()

        override val translationKey: String get() = asItem().descriptionId

        override fun getText(): Component = asItem().description
    }

    /**
     * @suppress
     */
    @JvmInline
    private value class Simple(private val contents: Either<ResourceKey<Item>, Item>) : Delegated<Item> {
        @Suppress("DEPRECATION")
        override fun getItemHolder(): Holder<Item> = contents.map(BuiltInRegistries.ITEM::getHolderOrThrow, Item::builtInRegistryHolder)

        override fun asItem(): Item = getItemHolder().value()
    }
}
