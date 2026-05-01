package hiiragi283.core.api.item

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.registry.StackTemplate
import hiiragi283.core.api.serialization.network.HTStreamCodecs
import hiiragi283.core.api.text.HTTextResult
import hiiragi283.core.api.text.toText
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.PatchedDataComponentMap
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

@Suppress("DEPRECATION")
@JvmRecord
data class ItemStackTemplate(val item: Holder<Item>, val count: Int, val components: DataComponentPatch) : StackTemplate<Item> {
    companion object {
        @JvmField
        val MAP_CODEC: MapCodec<ItemStackTemplate> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    ItemStack.ITEM_NON_AIR_CODEC.fieldOf(HTConst.ID).forGetter(ItemStackTemplate::item),
                    ExtraCodecs.intRange(1, 99).optionalFieldOf(HTConst.COUNT, 1).forGetter(ItemStackTemplate::count),
                    DataComponentPatch.CODEC
                        .optionalFieldOf("components", DataComponentPatch.EMPTY)
                        .forGetter(ItemStackTemplate::components),
                ).apply(instance, ::ItemStackTemplate)
        }

        @JvmField
        val CODEC: Codec<ItemStackTemplate> = Codec.lazyInitialized(MAP_CODEC::codec)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ItemStackTemplate> = StreamCodec.composite(
            HTStreamCodecs.holder(Registries.ITEM),
            ItemStackTemplate::item,
            ByteBufCodecs.VAR_INT,
            ItemStackTemplate::count,
            DataComponentPatch.STREAM_CODEC,
            ItemStackTemplate::components,
            ::ItemStackTemplate,
        )

        @JvmStatic
        fun fromStack(stack: ItemStack): HTTextResult<ItemStackTemplate> = when {
            stack.isEmpty -> HTTextResult.error("Stack must be non-empty".toText())
            else -> HTTextResult.success(ItemStackTemplate(stack.itemHolder, stack.count, stack.componentsPatch))
        }
    }

    init {
        check(!item.`is`(Items.AIR.builtInRegistryHolder()) && count >= 0) { "Item must be non-empty" }
    }

    constructor(item: ItemLike, count: Int = 1, components: DataComponentPatch = DataComponentPatch.EMPTY) : this(
        item.asItem().builtInRegistryHolder(),
        count,
        components,
    )

    fun create(): ItemStack = ItemStack(typeHolder(), count, components)

    fun apply(additionalPatch: DataComponentPatch): ItemStack = apply(this.count, additionalPatch)

    fun apply(count: Int, additionalPatch: DataComponentPatch): ItemStack {
        val stack = ItemStack(item, count, additionalPatch)
        stack.applyComponents(components)
        return stack
    }

    override fun typeHolder(): Holder<Item> = item.delegate

    override fun getComponents(): DataComponentMap = PatchedDataComponentMap.fromPatch(item.value().components(), components)
}
