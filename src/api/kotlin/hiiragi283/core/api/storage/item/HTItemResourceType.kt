package hiiragi283.core.api.storage.item

import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.storage.resource.HTResourceType
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

/**
 * [アイテム][Item]向けの[HTResourceType.DataComponent]の実装クラスです。
 * @param stack 内部で保持しているスタック
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
class HTItemResourceType private constructor(private val stack: ItemStack) : HTResourceType.DataComponent<Item> {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemResourceType> =
            BiCodec
                .of(ItemStack.SINGLE_ITEM_CODEC, ItemStack.STREAM_CODEC)
                .xmap(::HTItemResourceType, HTItemResourceType::stack)

        /**
         * 指定した[item]を[HTItemResourceType]に変換します。
         * @return [ItemStack.isEmpty]が`true`の場合は`null`
         */
        @JvmStatic
        fun ofNullable(item: ItemLike): HTItemResourceType? = ItemStack(item).let(::of)

        /**
         * 指定した[item]を[HTItemResourceType]に変換します。
         * @throws IllegalStateException [ItemStack.isEmpty]が`true`の場合
         */
        @JvmStatic
        fun of(item: ItemLike): HTItemResourceType = ofNullable(item) ?: error("Item must not be empty")

        /**
         * 指定した[stack]を[HTItemResourceType]に変換します。
         * @return [ItemStack.isEmpty]が`true`の場合は`null`
         */
        @JvmStatic
        fun of(stack: ItemStack): HTItemResourceType? = when {
            stack.isEmpty -> null
            else -> HTItemResourceType(stack.copyWithCount(1))
        }
    }

    /**
     * 指定した[count]から[ItemStack]に変換します。
     */
    fun toStack(count: Int = 1): ItemStack = stack.copyWithCount(count)

    override fun equals(other: Any?): Boolean = when (other) {
        is HTItemResourceType -> ItemStack.isSameItemSameComponents(this.stack, other.stack)
        else -> false
    }

    override fun hashCode(): Int = ItemStack.hashItemAndComponents(stack)

    override fun toString(): String = stack.toString()

    operator fun component1(): Holder<Item> = getHolder()

    operator fun component2(): DataComponentPatch = componentsPatch()

    //    HTResourceType    //

    override fun componentsPatch(): DataComponentPatch = stack.componentsPatch

    override fun type(): Item = stack.item

    override fun getText(): Component = stack.hoverName

    override fun getHolder(): Holder<Item> = stack.itemHolder

    override fun getComponents(): DataComponentMap = stack.components
}
