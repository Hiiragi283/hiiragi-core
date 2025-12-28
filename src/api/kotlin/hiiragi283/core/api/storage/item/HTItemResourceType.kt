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
 * [HTResourceType.DataComponent]を[アイテム][Item]向けに実装したクラスです。
 * @param stack 内部で保持しているスタック
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
@ConsistentCopyVisibility
@JvmRecord
data class HTItemResourceType private constructor(private val stack: ItemStack) : HTResourceType.DataComponent<Item> {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemResourceType> =
            BiCodec
                .of(ItemStack.SINGLE_ITEM_CODEC, ItemStack.STREAM_CODEC)
                .xmap(::HTItemResourceType, HTItemResourceType::stack)

        @JvmStatic
        fun ofNullable(item: ItemLike): HTItemResourceType? = ItemStack(item).let(::of)

        @JvmStatic
        fun of(item: ItemLike): HTItemResourceType = ofNullable(item) ?: error("Item must not be empty")

        @JvmStatic
        fun of(stack: ItemStack): HTItemResourceType? = when {
            stack.isEmpty -> null
            else -> HTItemResourceType(stack.copyWithCount(1))
        }
    }

    fun toStack(count: Int = 1): ItemStack = stack.copyWithCount(count)

    override fun equals(other: Any?): Boolean = when (other) {
        is HTItemResourceType -> ItemStack.isSameItemSameComponents(this.stack, other.stack)
        else -> false
    }

    override fun hashCode(): Int = ItemStack.hashItemAndComponents(stack)

    //    HTResourceType    //

    override fun componentsPatch(): DataComponentPatch = stack.componentsPatch

    override fun type(): Item = stack.item

    override fun getText(): Component = stack.hoverName

    override fun getHolder(): Holder<Item> = stack.itemHolder

    override fun getComponents(): DataComponentMap = stack.components
}
