package hiiragi283.core.api.registry

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
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem

/**
 * [ItemLike]とその他諸々を継承した[HTHolderLike.HolderDelegate]の拡張インターフェースです。
 * @param ITEM アイテムのクラス
 * @author Hiiragi Tsubasa
 * @since 0.13.0
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
            .xmap(Holder<Item>::toItemLike, HTItemHolderLike<*>::getHolder)
    }

    interface Simple<ITEM : Item> : HTItemHolderLike<ITEM> {
        override val translationKey: String get() = get().descriptionId

        override fun getText(): Text = get().description
    }
}

//    Extensions    //

/**
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun ItemLike.toItemLike(): HTItemHolderLike<Item> = this.asItem().toLike()

/**
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun <ITEM : Item> ITEM.toLike(): HTItemHolderLike<ITEM> = object : HTItemHolderLike.Simple<ITEM> {
    override fun get(): ITEM = this@toLike

    @Suppress("DEPRECATION")
    override fun getHolder(): Holder<Item> = this@toLike.builtInRegistryHolder()
}

/**
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun Holder<Item>.toItemLike(): HTItemHolderLike<Item> = object : HTItemHolderLike.Simple<Item> {
    override fun get(): Item = this@toItemLike.value()

    override fun getHolder(): Holder<Item> = this@toItemLike.delegate
}

/**
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun <ITEM : Item> DeferredHolder<Item, ITEM>.toItemLike(): HTItemHolderLike<ITEM> = object : HTItemHolderLike.Simple<ITEM> {
    override fun get(): ITEM = this@toItemLike.get()

    override fun getHolder(): Holder<Item> = this@toItemLike.delegate

    override fun getId(): ResourceLocation = this@toItemLike.id
}

/**
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun <ITEM : Item> DeferredItem<ITEM>.toLike(): HTItemHolderLike<ITEM> = object : HTItemHolderLike.Simple<ITEM> {
    override fun get(): ITEM = this@toLike.get()

    override fun getHolder(): Holder<Item> = this@toLike.delegate

    override fun getId(): ResourceLocation = this@toLike.id
}

/**
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun <ITEM : Item> HTHolderLike<Item, ITEM>.toItemLike(): HTItemHolderLike<ITEM> = object : HTItemHolderLike.Simple<ITEM> {
    override fun get(): ITEM = this@toItemLike.get()

    @Suppress("DEPRECATION")
    override fun getHolder(): Holder<Item> =
        (this as? HTHolderLike.HolderDelegate<Item, ITEM>)?.getHolder() ?: this.get().builtInRegistryHolder()

    override fun getResourceKey(): ResourceKey<Item> = this@toItemLike.getResourceKey()

    override fun getId(): ResourceLocation = this@toItemLike.getId()
}
