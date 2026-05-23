package hiiragi283.core.api.recipe.result

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.tagPrefix
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.storage.item.toResource
import hiiragi283.core.api.toFraction
import hiiragi283.core.api.util.DFUEither
import hiiragi283.core.api.util.HTTextResult
import hiiragi283.core.api.util.getOrElse
import hiiragi283.core.api.util.right
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs
import org.apache.commons.lang3.math.Fraction

interface HTItemResult : HTIdLike {
    companion object {
        @JvmField
        val MAP_CODEC: MapCodec<HTItemResult> = NeoForgeExtraCodecs.dispatchMapOrElse(
            HCRegistries.ITEM_RESULT_SERIALIZER.byNameCodec(),
            HTItemResult::getSerializer,
            Serializer<*>::codec,
            Simple.MAP_CODEC,
        ).xmap(DFUEither<HTItemResult, Simple>::unwrap) { result: HTItemResult ->
            when (result) {
                is Simple -> DFUEither.right(result)
                else -> DFUEither.left(result)
            }
        }

        @JvmField
        val CODEC: Codec<HTItemResult> = Codec.lazyInitialized(MAP_CODEC::codec)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemResult> =
            ByteBufCodecs.registry(HCRegistries.Keys.ITEM_RESULT_SERIALIZER).dispatch(HTItemResult::getSerializer, Serializer<*>::streamCodec)
    }

    fun getSerializer(): Serializer<*>

    fun create(): HTTextResult<ItemStack>

    fun createOrEmpty(): ItemStack = create().getOrElse { ItemStack.EMPTY }

    fun withChance(chance: Float = 1f): HTChancedItemResult = withChance(chance.toFraction())

    fun withChance(chance: Fraction): HTChancedItemResult = HTChancedItemResult(this, chance)

    //    Serializer    //

    @JvmRecord
    data class Serializer<T : HTItemResult>(val codec: MapCodec<T>, val streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>) {
        constructor(codec: MapCodec<T>) : this(codec, ByteBufCodecs.fromCodecWithRegistries(codec.codec()))
    }

    //    Simple    //

    @JvmInline
    value class Simple(private val template: ItemStack) : HTItemResult {
        companion object {
            @JvmField
            val MAP_CODEC: MapCodec<Simple> = ItemStack.CODEC.xmap(::Simple, Simple::template).let { MapCodec.assumeMapUnsafe(it) }

            @JvmField
            val CODEC: Codec<Simple> = Codec.lazyInitialized(MAP_CODEC::codec)

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Simple> = ItemStack.STREAM_CODEC.map(::Simple, Simple::template)

            @JvmField
            val SERIALIZER: Serializer<Simple> = Serializer(MAP_CODEC, STREAM_CODEC)
        }

        override fun getSerializer(): Serializer<*> = SERIALIZER

        override fun create(): HTTextResult<ItemStack> = template.copy().right()

        override fun getId(): ResourceLocation = template.itemHolder.toLike().getId()
    }

    //    Tagged    //

    data class Tagged(val tagKey: TagKey<Item>, val count: Int) : HTItemResult {
        companion object {
            @JvmField
            val CODEC: MapCodec<Tagged> = RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    HTCodecs.tagKey(Registries.ITEM, true).fieldOf(HTConst.TAG).forGetter(Tagged::tagKey),
                    HTCodecs.POSITIVE_INT.optionalFieldOf(HTConst.COUNT, 1).forGetter(Tagged::count),
                ).apply(instance, ::Tagged)
            }

            @JvmField
            val SERIALIZER: Serializer<Tagged> = Serializer(CODEC)
        }

        override fun getSerializer(): Serializer<*> = SERIALIZER

        override fun create(): HTTextResult<ItemStack> = HiiragiCoreAccess.INSTANCE.getFirstHolder(BuiltInRegistries.ITEM.asLookup(), tagKey).map { ItemStack(it.get(), count) }

        override fun getId(): ResourceLocation = tagKey.location()
    }

    //    MaterialPart    //

    @JvmRecord
    data class MaterialPart(val part: HTPart, val material: HTMaterialKey, val count: Int) : HTItemResult {
        companion object {
            @JvmField
            val CODEC: MapCodec<MaterialPart> = RecordCodecBuilder.mapCodec { instance ->
                instance
                    .group(
                        HiiragiCoreAccess.INSTANCE.partCodec
                            .fieldOf("part")
                            .forGetter(MaterialPart::part),
                        HTMaterialKey.CODEC.fieldOf("material").forGetter(MaterialPart::material),
                        HTCodecs.POSITIVE_INT
                            .fieldOf(HTConst.COUNT)
                            .orElse(1)
                            .forGetter(MaterialPart::count),
                    ).apply(instance, ::MaterialPart)
            }

            @JvmField
            val SERIALIZER: Serializer<MaterialPart> = Serializer(CODEC)
        }

        override fun getSerializer(): Serializer<*> = SERIALIZER

        override fun create(): HTTextResult<ItemStack> {
            val tagResult: HTTextResult<ItemStack>? = part.tagPrefix
                ?.itemTagKey(material)
                ?.let { Tagged(it, count) }
                ?.create()
            if (tagResult != null && tagResult.isLeft()) {
                return tagResult
            }
            return HiiragiCoreAccess.INSTANCE
                .getMaterialBlockOrItem(part, material)
                .toResource()
                ?.toStack(count)
                ?.right()
                ?: HTTextResult("No matching item for part ${part.asPartName()} and material ${material.asMaterialId()}")
        }

        override fun getId(): ResourceLocation = part.createId(material)
    }
}
