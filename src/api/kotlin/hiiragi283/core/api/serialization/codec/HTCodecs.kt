package hiiragi283.core.api.serialization.codec

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import hiiragi283.core.api.fraction
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.util.Either
import hiiragi283.core.api.util.Ior
import hiiragi283.core.api.util.java
import hiiragi283.core.api.util.kotlin
import hiiragi283.core.impl.serialization.codec.HTIngredientCodec
import hiiragi283.core.impl.serialization.codec.HTIorMapCodec
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryCodecs
import net.minecraft.core.UUIDUtil
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import org.apache.commons.lang3.math.Fraction
import java.util.UUID
import java.util.function.Function
import kotlin.enums.enumEntries

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[Codec]と[MapCodec]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
data object HTCodecs {
    @JvmField
    val FRACTION: Codec<Fraction> = xor(Codec.STRING, Codec.INT)
        .xmap(
            { either: Either<String, Int> -> either.fold(Fraction::getFraction, ::fraction) },
            { fraction: Fraction ->
                when (fraction.denominator) {
                    1 -> Either.Right(fraction.numerator)
                    else -> Either.Left(fraction.toString())
                }
            },
        )

    @JvmField
    val INGREDIENT: Codec<Ingredient> = HTIngredientCodec.ITEM

    @JvmField
    val FLUID_INGREDIENT: MapCodec<FluidIngredient> = HTIngredientCodec.FLUID

    @JvmField
    val TEXT: Codec<Text> = ComponentSerialization.CODEC

    @JvmField
    val UUID: Codec<UUID> = UUIDUtil.CODEC

    @JvmStatic
    fun <K : Any, V : Any> mapOf(keyCodec: Codec<K>, valueCodec: Codec<V>): Codec<Map<K, V>> = Codec.unboundedMap(keyCodec, valueCodec)

    /**
     * @since 0.17.0
     */
    @JvmStatic
    fun <L, R> either(left: Codec<L>, right: Codec<R>): Codec<Either<L, R>> = Codec.either(left, right).xmap({ it.kotlin }, { it.java })

    /**
     * @since 0.17.0
     */
    @JvmStatic
    fun <L, R> xor(left: Codec<L>, right: Codec<R>): Codec<Either<L, R>> = Codec.xor(left, right).xmap({ it.kotlin }, { it.java })

    /**
     * @since 0.17.0
     */
    @JvmStatic
    fun <L, R> either(left: MapCodec<L>, right: MapCodec<R>): MapCodec<Either<L, R>> = Codec.mapEither(left, right).xmap({ it.kotlin }, { it.java })

    /**
     * 指定した[left], [right]から，[Ior]の[MapCodec]を返します。
     * @param left [L]を対象とする[MapCodec]
     * @param right [R]を対象とする[MapCodec]
     * @return [Ior]の[MapCodec]
     */
    @JvmStatic
    fun <L, R> ior(left: MapCodec<L>, right: MapCodec<R>): MapCodec<Ior<L, R>> = HTIorMapCodec(left, right)

    /**
     * [Enum]の[Codec]を返します。
     * @param V [Enum]を実装したクラス
     * @param factory [V]を[String]に変換するブロック
     * @return [factory]に基づいた[Codec]
     */
    @JvmStatic
    inline fun <reified V : Enum<V>> stringEnum(factory: Function<V, String?>): Codec<V> = Codec.stringResolver(factory) { name: String -> enumEntries<V>().firstOrNull { factory.apply(it) == name } }

    //    Ranged    //

    /**
     * @see ExtraCodecs.intRangeWithMessage
     */
    @JvmStatic
    fun <N> numberRange(codec: Codec<N>, range: ClosedRange<N>): Codec<N> where N : Number, N : Comparable<N> = codec.validate { number: N ->
        when (number) {
            in range -> DataResult.success(number)
            else -> DataResult.error { "Value must be within range $range: $number" }
        }
    }

    /**
     * `0`以上の値を対象とする[Int]の[Codec]
     */
    @JvmField
    val NON_NEGATIVE_INT: Codec<Int> = ExtraCodecs.NON_NEGATIVE_INT

    /**
     * `0`以上の値を対象とする[Long]の[Codec]
     * @see mekanism.api.SerializerHelper.POSITIVE_LONG_CODEC
     */
    @JvmField
    val NON_NEGATIVE_LONG: Codec<Long> = numberRange(Codec.LONG, 0..Long.MAX_VALUE)

    /**
     * `0`以上の値を対象とする[Fraction]の[Codec]
     */
    @JvmField
    val NON_NEGATIVE_FRACTION: Codec<Fraction> = FRACTION.validate { fraction: Fraction ->
        when {
            fraction < Fraction.ZERO -> DataResult.error { "Value must be non-negative: $fraction" }
            else -> DataResult.success(fraction)
        }
    }

    /**
     * `1`以上の値を対象とする[Int]の[Codec]
     */
    @JvmField
    val POSITIVE_INT: Codec<Int> = ExtraCodecs.POSITIVE_INT

    /**
     * `1`以上の値を対象とする[Long]の[Codec]
     * @see mekanism.api.SerializerHelper.POSITIVE_LONG_CODEC
     */
    @JvmField
    val POSITIVE_LONG: Codec<Long> = numberRange(Codec.LONG, 1..Long.MAX_VALUE)

    //    Registry    //

    /**
     * 指定した[registryKey]から[ResourceKey]の[Codec]を返します。
     * @param T レジストリの要素のクラス
     */
    @JvmStatic
    fun <T : Any> resourceKey(registryKey: RegistryKey<T>): Codec<ResourceKey<T>> = ResourceKey.codec(registryKey)

    /**
     * 指定した[registryKey]から[TagKey]の[Codec]を返します。
     * @param T レジストリの要素のクラス
     * @param withHash 変換後の文字列の先頭に'#'をつけるかどうか
     */
    @JvmStatic
    fun <T : Any> tagKey(registryKey: RegistryKey<T>, withHash: Boolean): Codec<TagKey<T>> = when (withHash) {
        true -> TagKey.hashedCodec(registryKey)
        false -> TagKey.codec(registryKey)
    }

    /**
     * 指定した[registryKey]から[Holder]の[Codec]を返します。
     * @param T レジストリの要素のクラス
     */
    @JvmStatic
    fun <T : Any> holder(registryKey: RegistryKey<T>): Codec<Holder<T>> = RegistryFixedCodec.create(registryKey).validate { DataResult.success(it.delegate) }

    /**
     * 指定した[registryKey]から[HolderSet]の[Codec]を返します。
     * @param T レジストリの要素のクラス
     */
    @JvmStatic
    fun <T : Any> holderSet(registryKey: RegistryKey<T>): Codec<HolderSet<T>> = RegistryCodecs.homogeneousList(registryKey)
}
