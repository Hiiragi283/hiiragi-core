package hiiragi283.core.api.registry

import com.mojang.serialization.Codec
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.network.HTStreamCodecs
import hiiragi283.core.api.text.Text
import hiiragi283.core.impl.registry.HTIntrusiveHolderLike
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.registries.DeferredItem

typealias HTSimpleItemHolderLike = HTItemHolderLike<Item>

/**
 * [ItemLike]とその他諸々を継承した[HTHolderLike]の拡張インターフェースです。
 * @param ITEM アイテムのクラス
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
interface HTItemHolderLike<out ITEM : Item> :
    HTHolderLike<Item, ITEM>,
    HTIdLike.Translatable,
    HTItemLike<ITEM> {
    override fun asItem(): ITEM = get()

    companion object {
        @JvmField
        val CODEC: Codec<HTSimpleItemHolderLike> =
            HTCodecs.holderLike(Registries.ITEM).xmap(HTSimpleHolderLike<Item>::toItemLike, identity())

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTSimpleItemHolderLike> =
            HTStreamCodecs.holderLike(Registries.ITEM).map(HTSimpleHolderLike<Item>::toItemLike, identity())
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
fun ItemLike.toItemLike(): HTSimpleItemHolderLike = this.asItem().toLike()

/**
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun <ITEM : Item> ITEM.toLike(): HTItemHolderLike<ITEM> = object : HTIntrusiveHolderLike<Item, ITEM>(), HTItemHolderLike.Simple<ITEM> {
    @Suppress("DEPRECATION")
    override fun getHolder(value: Item): Holder<Item> = value.builtInRegistryHolder()

    override fun get(): ITEM = this@toLike

    override fun toString(): String = this@toLike.toString()
}

/**
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun <ITEM : Item> HTHolderLike<Item, ITEM>.toItemLike(): HTItemHolderLike<ITEM> = HolderLikeItemHolderLike(this)

private class HolderLikeItemHolderLike<ITEM : Item>(holder: HTHolderLike<Item, ITEM>) :
    HTItemHolderLike.Simple<ITEM>,
    HTHolderLike<Item, ITEM> by holder

/**
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun ResourceLocation.toItemLike(): HTSimpleItemHolderLike = DeferredItem.createItem<Item>(this@toItemLike).toLike().toItemLike()

/**
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun ItemStack.getHolderLike(): HTSimpleItemHolderLike = this.itemHolder.toLike().toItemLike()
