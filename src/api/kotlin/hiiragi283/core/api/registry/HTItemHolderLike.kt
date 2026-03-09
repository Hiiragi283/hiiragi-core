package hiiragi283.core.api.registry

import hiiragi283.core.api.function.andThen
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.toResource
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.core.api.text.Text
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

/**
 * [ItemLike]とその他諸々を継承した[HTHolderLike.HolderDelegate]の拡張インターフェースです。
 * @param ITEM アイテムのクラス
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 */
interface HTItemHolderLike<ITEM : Item> :
    HTHolderLike.HolderDelegate<Item, ITEM>,
    HTHasTranslationKey,
    HTHasText,
    ItemLike {
    override fun asItem(): ITEM = get()

    /**
     * 指定した[個数][count]で[ItemStack]に変換します。
     */
    fun toStack(count: Int = 1): ItemStack = ItemStack(this, count)

    /**
     * [HTItemResourceType]に変換します。
     */
    fun toResource(): HTItemResourceType? = toStack().toResource()

    /**
     * 指定した[patch]で[HTItemResourceType]に変換します。
     */
    fun toResource(patch: DataComponentPatch): HTItemResourceType? {
        val stack: ItemStack = toStack()
        stack.applyComponents(patch)
        return stack.toResource()
    }

    companion object {
        /**
         * [Holder]に基づいた[HTItemHolderLike]の[BiCodec]
         */
        @JvmField
        val HOLDER_CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemHolderLike<*>> = VanillaBiCodecs
            .holder(Registries.ITEM)
            .xmap(Holder<Item>::value.andThen(::of), HTItemHolderLike<*>::getHolder)

        /**
         * 指定した[holder]から[HTItemHolderLike]の新しいインスタンスを作成します。
         */
        @JvmStatic
        fun of(holder: Holder<Item>): HTItemHolderLike<Item> = object : Simple<Item> {
            override fun get(): Item = holder.value()

            override fun getHolder(): Holder<Item> = holder.delegate
        }

        /**
         * 指定した[item]から[HTItemHolderLike]の新しいインスタンスを作成します。
         */
        @JvmStatic
        fun <ITEM : Item> of(item: ITEM): HTItemHolderLike<ITEM> = object : Simple<ITEM> {
            override fun get(): ITEM = item

            @Suppress("DEPRECATION")
            override fun getHolder(): Holder<Item> = item.builtInRegistryHolder()
        }
    }

    interface Simple<ITEM : Item> : HTItemHolderLike<ITEM> {
        override val translationKey: String get() = get().descriptionId

        override fun getText(): Text = get().description
    }
}

//    Extensions    //

fun Item.toLike(): HTItemHolderLike<Item> = HTItemHolderLike.of(this)
