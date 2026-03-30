package hiiragi283.core.api.registry

import hiiragi283.core.api.function.identity
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.util.Either
import net.minecraft.core.Holder
import net.minecraft.core.TypedInstance
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.transfer.item.ItemResource

typealias HTSimpleItemHolderLike = HTItemHolderLike<Item>

/**
 * [ItemLike]とその他諸々を継承した[HTHolderLike]の拡張インターフェースです。
 * @param ITEM アイテムのクラス
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
interface HTItemHolderLike<ITEM : Item> :
    HTHolderLike<Item, ITEM>,
    HTHasTranslationKey,
    HTHasText,
    ItemLike {
    fun getHolder(): Holder<Item> = getHolder(BuiltInRegistries.ITEM)

    override fun asItem(): ITEM = get()

    fun isOf(item: Item): Boolean = this.asItem() == item

    fun isOf(instance: TypedInstance<Item>): Boolean = instance.`is`(this.asItem())

    // ItemStackTemplate

    /**
     * 指定した[個数][count]で[ItemStackTemplate]に変換します。
     */
    fun toTemplate(count: Int = 1): ItemStackTemplate = ItemStackTemplate(this.getHolder(), count)

    /**
     * 指定した[個数][count]と[patch]で[ItemStackTemplate]に変換します。
     */
    fun toTemplate(count: Int = 1, patch: DataComponentPatch): ItemStackTemplate = ItemStackTemplate(this.getHolder(), count, patch)

    // ItemStack

    /**
     * 指定した[個数][count]で[ItemStack]に変換します。
     */
    fun toStack(count: Int = 1): ItemStack = ItemStack(this, count)

    // ItemResource

    /**
     * [ItemResource]に変換します。
     */
    fun toResource(): ItemResource = ItemResource.of(this)

    /**
     * 指定した[patch]で[ItemResource]に変換します。
     */
    fun toResource(patch: DataComponentPatch): ItemResource = ItemResource.of(this, patch)

    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTSimpleItemHolderLike> =
            VanillaBiCodecs.holderLike(Registries.ITEM).xmap(HTSimpleHolderLike<Item>::toItemLike, identity())
    }

    interface Simple<ITEM : Item> : HTItemHolderLike<ITEM> {
        override val translationKey: String get() = get().descriptionId

        override fun getText(): Text = toStack().itemName
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
@Suppress("DEPRECATION")
fun <ITEM : Item> ITEM.toLike(): HTItemHolderLike<ITEM> = object : HTItemHolderLike.Simple<ITEM> {
    override fun unwrap(): Either<ResourceKey<Item>, Holder<Item>> = Either.Right(this@toLike.builtInRegistryHolder())

    override fun get(): ITEM = this@toLike

    override fun toString(): String = this@toLike.toString()
}

/**
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun <ITEM : Item> HTHolderLike<Item, ITEM>.toItemLike(): HTItemHolderLike<ITEM> = object : HTItemHolderLike.Simple<ITEM> {
    override fun unwrap(): Either<ResourceKey<Item>, Holder<Item>> = this@toItemLike.unwrap()

    override fun get(): ITEM = this@toItemLike.get()

    override fun toString(): String = this@toItemLike.toString()
}

/**
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun TypedInstance<Item>.itemHolderLike(): HTSimpleItemHolderLike = this.holderLike().toItemLike()
