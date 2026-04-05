package hiiragi283.core.api.registry

import hiiragi283.core.api.function.identity
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.toResource
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.util.Either
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

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
    fun getHolder(): Holder<Item> = getHolder(BuiltInRegistries.ITEM::getHolderOrThrow)

    override fun asItem(): ITEM = get()

    /**
     * @author Hiiragi Tsubasa
     * @since 0.14.0
     */
    fun isOf(item: Item): Boolean = this.asItem() == item

    // ItemStack
    fun isOf(stack: ItemStack): Boolean = stack.`is`(this.asItem())

    /**
     * 指定した[個数][count]で[ItemStack]に変換します。
     */
    fun toStack(count: Int = 1): ItemStack = ItemStack(this, count)

    // HTItemResourceType

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
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTSimpleItemHolderLike> =
            VanillaBiCodecs.holderLike(Registries.ITEM).xmap(HTSimpleHolderLike<Item>::toItemLike, identity())
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
fun ResourceLocation.toItemLike(): HTSimpleItemHolderLike = object : HTItemHolderLike.Simple<Item> {
    override fun unwrap(): Either<ResourceKey<Item>, Holder<Item>> = Either.Left(Registries.ITEM.createKey(this@toItemLike))

    override fun get(): Item = BuiltInRegistries.ITEM.getOrThrow(getResourceKey())

    override fun toString(): String = "HTItemHolderLike(id=${this@toItemLike})"
}

/**
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun ItemStack.getHolderLike(): HTSimpleItemHolderLike = this.itemHolder.toLike().toItemLike()
