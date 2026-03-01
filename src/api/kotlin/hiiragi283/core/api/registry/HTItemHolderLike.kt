package hiiragi283.core.api.registry

import hiiragi283.core.api.function.andThen
import hiiragi283.core.api.resource.HTIdLike
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
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

/**
 * [HTIdLike]と[ItemLike]とその他諸々を継承した[HTIdLike]の拡張インターフェースです。
 * @param ITEM アイテムのクラス
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 */
interface HTItemHolderLike<ITEM : Item> :
    ItemLike,
    HTIdLike,
    HTHasTranslationKey,
    HTHasText {
    /**
     * 保持している[アイテム][ITEM]を取得します。
     */
    override fun asItem(): ITEM

    /**
     * 保持しているアイテムの[Holder]を取得します。
     */
    fun getItemHolder(): Holder<Item>

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
            .xmap(Holder<Item>::value.andThen(::of), HTItemHolderLike<*>::getItemHolder)

        /**
         * 指定した[holder]から[HTItemHolderLike]の新しいインスタンスを作成します。
         */
        @JvmStatic
        fun of(holder: Holder<Item>): HTItemHolderLike<Item> = object : Simple<Item> {
            override fun asItem(): Item = holder.value()

            override fun getItemHolder(): Holder<Item> = holder.delegate
        }

        /**
         * 指定した[item]から[HTItemHolderLike]の新しいインスタンスを作成します。
         */
        @JvmStatic
        fun <ITEM : Item> of(item: ITEM): HTItemHolderLike<ITEM> = object : Simple<ITEM> {
            override fun asItem(): ITEM = item

            @Suppress("DEPRECATION")
            override fun getItemHolder(): Holder<Item> = item.builtInRegistryHolder()
        }
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 0.11.0
     */
    interface Simple<ITEM : Item> : HTItemHolderLike<ITEM> {
        override fun getId(): ResourceLocation = getItemHolder().toLike().getId()

        override val translationKey: String get() = asItem().descriptionId

        override fun getText(): Text = asItem().description
    }
}

//    Extensions    //

fun <ITEM : Item> HTItemHolderLike<ITEM>.toLike(): HTHolderLike.HolderDelegate<Item, ITEM> =
    object : HTHolderLike.HolderDelegate<Item, ITEM> {
        override fun get(): ITEM = this@toLike.asItem()

        override fun getHolder(): Holder<Item> = this@toLike.getItemHolder()
    }
