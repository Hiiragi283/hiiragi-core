package hiiragi283.lib.recipe.result

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.lib.HTConstants
import hiiragi283.lib.HTRegistries
import hiiragi283.lib.math.toFraction
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs
import org.apache.commons.lang3.math.Fraction

interface HTItemResult : HTIdLike {
    companion object {
        @JvmField
        val MAP_CODEC: MapCodec<HTItemResult> = NeoForgeExtraCodecs.dispatchMapOrElse(
            HTRegistries.ITEM_RESULT_SERIALIZER.byNameCodec(),
            HTItemResult::getSerializer,
            Serializer<*>::codec,
            Simple.MAP_CODEC,
        ).xmap(Either<HTItemResult, Simple>::unwrap) { result: HTItemResult ->
            when (result) {
                is Simple -> Either.right(result)
                else -> Either.left(result)
            }
        }

        @JvmField
        val CODEC: Codec<HTItemResult> = Codec.lazyInitialized(MAP_CODEC::codec)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemResult> =
            ByteBufCodecs.registry(HTRegistries.Keys.ITEM_RESULT_SERIALIZER).dispatch(HTItemResult::getSerializer, Serializer<*>::streamCodec)
    }

    fun getSerializer(): Serializer<*>

    fun create(): DataResult<ItemStack>

    fun createOrEmpty(): ItemStack = create().resultOrPartial().orElseGet(ItemStack::EMPTY)

    fun withChance(chance: Float = 1f): HTChancedItemResult = withChance(chance.toFraction())

    fun withChance(chance: Fraction): HTChancedItemResult = HTChancedItemResult(this, chance)

    //    Serializer    //

    @JvmRecord
    data class Serializer<T : HTItemResult>(val codec: MapCodec<T>, val streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>) {
        constructor(codec: MapCodec<T>) : this(codec, ByteBufCodecs.fromCodecWithRegistries(codec.codec()))
    }

    //    Simple    //

    @JvmInline
    value class Simple(private val template: ItemStackTemplate) : HTItemResult {
        companion object {
            @JvmField
            val MAP_CODEC: MapCodec<Simple> = ItemStackTemplate.MAP_CODEC.xmap(::Simple, Simple::template)

            @JvmField
            val CODEC: Codec<Simple> = Codec.lazyInitialized(MAP_CODEC::codec)

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Simple> = ItemStackTemplate.STREAM_CODEC.map(::Simple, Simple::template)

            @JvmField
            val SERIALIZER: Serializer<Simple> = Serializer(MAP_CODEC, STREAM_CODEC)
        }

        override fun getSerializer(): Serializer<*> = SERIALIZER

        override fun create(): DataResult<ItemStack> = DataResult.success(template.create())

        override fun getId(): Identifier = template.typeHolder().unwrapKey().orElseThrow().identifier()
    }

    //    Tagged    //

    data class Tagged(val tagKey: TagKey<Item>, val count: Int) : HTItemResult {
        companion object {
            @JvmField
            val CODEC: MapCodec<Tagged> = RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    HTCodecs.tagKey(Registries.ITEM, true).fieldOf(HTConstants.TAG).forGetter(Tagged::tagKey),
                    HTCodecs.POSITIVE_INT.optionalFieldOf(HTConstants.COUNT, 1).forGetter(Tagged::count),
                ).apply(instance, ::Tagged)
            }

            @JvmField
            val SERIALIZER: Serializer<Tagged> = Serializer(CODEC)
        }

        override fun getSerializer(): Serializer<*> = SERIALIZER

        override fun create(): DataResult<ItemStack> = BuiltInRegistries.ITEM
            .getTagOrEmpty(tagKey)
            .firstOrNull() // TODO
            ?.let { ItemStack(it, count) }
            ?.let { DataResult.success(it) }
            ?: DataResult.error { "Could not find elements from tag ${getId()}" }

        override fun getId(): Identifier = tagKey.location()
    }
}
