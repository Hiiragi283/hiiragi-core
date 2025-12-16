package hiiragi283.core.api.stack

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.capabilities.ItemCapability

@JvmInline
value class ImmutableItemStack private constructor(private val stack: ItemStack) : ImmutableComponentStack<Item, ImmutableItemStack> {
    companion object {
        @JvmStatic
        private val ITEM_STACK_CODEC: BiCodec<RegistryFriendlyByteBuf, ItemStack> =
            BiCodec.composite(
                VanillaBiCodecs.holder(Registries.ITEM).fieldOf(HTConst.ID).forGetter(ItemStack::getItemHolder),
                BiCodecs.POSITIVE_INT.optionalOrElseField(HTConst.COUNT, 1).forGetter(ItemStack::getCount),
                VanillaBiCodecs.COMPONENT_PATCH.forGetter(ItemStack::getComponentsPatch),
                ::ItemStack,
            )

        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, ImmutableItemStack> =
            ITEM_STACK_CODEC.flatXmap(ItemStack::toImmutableOrThrow, ImmutableItemStack::stack)

        @JvmStatic
        fun ofNullable(item: ItemLike, count: Int = 1): ImmutableItemStack? = ItemStack(item, count).toImmutable()

        @JvmStatic
        fun of(item: ItemLike, count: Int = 1): ImmutableItemStack = ItemStack(item, count).toImmutableOrThrow()

        @JvmStatic
        fun of(stack: ItemStack): ImmutableItemStack? = when (stack.isEmpty) {
            true -> null
            false -> ImmutableItemStack(stack.copy())
        }
    }

    fun unwrap(): ItemStack = stack.copy()

    fun <T : Any> plus(type: DataComponentType<T>, value: T?): ImmutableItemStack {
        val mutable: ItemStack = unwrap()
        mutable.set(type, value)
        return ImmutableItemStack(mutable)
    }

    fun <T : Any> minus(type: DataComponentType<T>): ImmutableItemStack {
        val mutable: ItemStack = unwrap()
        mutable.remove(type)
        return ImmutableItemStack(mutable)
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun <T, C : Any> getCapability(capability: ItemCapability<T, C?>, context: C?): T? = stack.getCapability(capability, context)

    fun <T> getCapability(capability: ItemCapability<T, Void?>): T? = getCapability(capability, null)

    //    ImmutableComponentStack    //

    override fun componentsPatch(): DataComponentPatch = stack.componentsPatch

    override fun getType(): Item = stack.item

    override fun getAmount(): Int = stack.count

    override fun copyWithAmount(amount: Int): ImmutableItemStack? = unwrap().copyWithCount(amount).toImmutable()

    override fun getText(): Component = stack.hoverName

    override fun getHolder(): Holder<Item> = stack.itemHolder

    override fun getComponents(): DataComponentMap = stack.components
}
