package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.registry.holderLike
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.core.api.serialization.codec.MapBiCodecs
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.util.Ior
import hiiragi283.core.api.util.toIorOrThrow
import net.minecraft.core.HolderSet
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import java.util.Optional
import kotlin.jvm.optionals.getOrDefault

class HTItemResult(private val contents: Ior<ItemStackTemplate, Pair<HolderSet<Item>, Int>>) : HTRecipeResult<ItemStack> {
    companion object {
        @JvmStatic
        private val TEMPLATE_CODEC: MapBiCodec<RegistryFriendlyByteBuf, ItemStackTemplate> =
            MapBiCodec.of(ItemStackTemplate.MAP_CODEC, ItemStackTemplate.STREAM_CODEC)

        @JvmStatic
        private val PAIR_CODEC: MapBiCodec<RegistryFriendlyByteBuf, Pair<HolderSet<Item>, Int>> = MapBiCodecs.pair(
            VanillaBiCodecs.holderSet(Registries.ITEM).fieldOf(HTConst.ITEMS),
            BiCodecs.POSITIVE_INT.optionalFieldOf(HTConst.COUNT, 1),
        )

        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemResult> = BiCodec.composite(
            MapBiCodecs.ior(TEMPLATE_CODEC, PAIR_CODEC).forGetter(HTItemResult::contents),
            ::HTItemResult,
        )

        /**
         * [HTItemResult]の新しいインスタンスを作成します。
         * @since 0.8.0
         */
        @HTBuilderMarker
        @JvmStatic
        fun create(builderAction: Builder.() -> Unit): HTItemResult = Builder().apply(builderAction).build()
    }

    override fun create(): ItemStack = contents.fold(
        ItemStackTemplate::create,
        { (holderSet: HolderSet<Item>, count: Int) ->
            create(holderSet, count) ?: error("Could not create ItemStack from $holderSet")
        },
        { template: ItemStackTemplate, (holderSet: HolderSet<Item>, count: Int) ->
            create(holderSet, count) ?: template.create()
        },
    )

    private fun create(holderSet: HolderSet<Item>, count: Int): ItemStack? = holderSet.firstOrNull()?.let { ItemStack(it, count) }

    override fun getId(): Identifier = contents.fold(
        { it.holderLike().getId() },
        { (holderSet: HolderSet<Item>, _) -> getId(holderSet).orElseThrow { error("Holder Set $holderSet has no tag key") } },
        { template: ItemStackTemplate, (holderSet: HolderSet<Item>, _) ->
            getId(holderSet).getOrDefault(template.holderLike().getId())
        },
    )

    private fun getId(holderSet: HolderSet<Item>): Optional<Identifier> = holderSet.unwrapKey().map(TagKey<Item>::location)

    //    Builder    //

    /**
     * [HTItemResult]のビルダークラスです。
     * @author Hiiragi Tsubasa
     * @since 0.10.0
     */
    class Builder {
        var item: Item? = null
        var patch: DataComponentPatch? = null
        var holderSet: HolderSet<Item>? = null
        var count: Int = 1

        fun build(): HTItemResult {
            val template: ItemStackTemplate? = item?.let { ItemStackTemplate(it, count, patch ?: DataComponentPatch.EMPTY) }
            return holderSet
                ?.let { it to count }
                .let { template to it }
                .toIorOrThrow("Either item or holder set required for result")
                .let(::HTItemResult)
        }
    }
}
