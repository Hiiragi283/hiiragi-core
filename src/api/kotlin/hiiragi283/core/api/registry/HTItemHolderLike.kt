package hiiragi283.core.api.registry

import hiiragi283.core.api.function.andThen
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.toResource
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.HTHasTranslationKey
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
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
    ItemLike,
    HTIdLike,
    HTHasTranslationKey,
    HTHasText {
    override fun asItem(): ITEM

    fun getItemHolder(): Holder<Item>

    fun toStack(count: Int = 1): ItemStack = ItemStack(this, count)

    fun toResource(): HTItemResourceType? = toStack().toResource()

    fun toResource(patch: DataComponentPatch): HTItemResourceType? {
        val stack: ItemStack = toStack()
        stack.applyComponents(patch)
        return stack.toResource()
    }

    companion object {
        @JvmField
        val HOLDER_CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemHolderLike<*>> = VanillaBiCodecs
            .holder(Registries.ITEM)
            .xmap(Holder<Item>::value.andThen(::of), HTItemHolderLike<*>::getItemHolder)

        /**
         * 指定した[holder]から[HTItemHolderLike]の新しいインスタンスを作成します。
         * @since 0.10.1
         */
        @JvmStatic
        fun of(holder: Holder<Item>): HTItemHolderLike<Item> = object : Simple<Item> {
            override fun asItem(): Item = holder.value()

            override fun getItemHolder(): Holder<Item> = holder
        }

        /**
         * 指定した[item]から[HTItemHolderLike]の新しいインスタンスを作成します。
         * @since 0.8.0
         */
        @JvmStatic
        fun <ITEM : Item> of(item: ITEM): HTItemHolderLike<ITEM> = object : Simple<ITEM> {
            override fun asItem(): ITEM = item

            @Suppress("DEPRECATION")
            override fun getItemHolder(): Holder<Item> = item.builtInRegistryHolder()
        }
    }

    interface Simple<ITEM : Item> : HTItemHolderLike<ITEM> {
        override fun getId(): ResourceLocation = getItemHolder().toLike().getId()

        override val translationKey: String get() = asItem().descriptionId

        override fun getText(): Component = asItem().description
    }
}
