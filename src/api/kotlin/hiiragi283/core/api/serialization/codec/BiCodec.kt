package hiiragi283.core.api.serialization.codec

import com.mojang.datafixers.util.Function3
import com.mojang.datafixers.util.Function4
import com.mojang.datafixers.util.Function5
import com.mojang.datafixers.util.Function6
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.serialization.codec.impl.HTOptionalCodec
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import java.util.*
import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.UnaryOperator

/**
 * [Codec]と[StreamCodec]を束ねたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
@ConsistentCopyVisibility
@JvmRecord
data class BiCodec<B : ByteBuf, V : Any> private constructor(val codec: Codec<V>, val streamCodec: StreamCodec<B, V>) {
    companion object {
        /**
         * 指定した[codec]と[streamCodec]から[BiCodec]を作成します。
         * @param B [StreamCodec]で使われるパケットのクラス
         * @param V コーデック対象のクラス
         */
        @JvmStatic
        fun <B : ByteBuf, V : Any> of(codec: Codec<V>, streamCodec: StreamCodec<B, V>): BiCodec<B, V> = BiCodec(codec, streamCodec)

        /**
         * [Boolean]の[BiCodec]
         */
        @JvmField
        val BOOL: BiCodec<ByteBuf, Boolean> = of(Codec.BOOL, ByteBufCodecs.BOOL)

        /**
         * [Short]の[BiCodec]
         */
        @JvmField
        val SHORT: BiCodec<ByteBuf, Short> = of(Codec.SHORT, ByteBufCodecs.SHORT)

        /**
         * [Int]の[BiCodec]
         */
        @JvmField
        val INT: BiCodec<ByteBuf, Int> = of(Codec.INT, ByteBufCodecs.VAR_INT)

        /**
         * [Long]の[BiCodec]
         */
        @JvmField
        val LONG: BiCodec<ByteBuf, Long> = of(Codec.LONG, ByteBufCodecs.VAR_LONG)

        /**
         * [Float]の[BiCodec]
         */
        @JvmField
        val FLOAT: BiCodec<ByteBuf, Float> = of(Codec.FLOAT, ByteBufCodecs.FLOAT)

        /**
         * [Double]の[BiCodec]
         */
        @JvmField
        val DOUBLE: BiCodec<ByteBuf, Double> = of(Codec.DOUBLE, ByteBufCodecs.DOUBLE)

        /**
         * [String]の[BiCodec]
         */
        @JvmField
        val STRING: BiCodec<ByteBuf, String> = of(Codec.STRING, ByteBufCodecs.STRING_UTF8)

        /**
         * 指定した[codec]から，別の[BiCodec]を生成します。
         * @param T1 [factory]の第1引数に使われるクラス
         * @param C 変換後のコーデックの対象となるクラス
         */
        @JvmStatic
        fun <B : ByteBuf, C : Any, T1 : Any> composite(codec: ParameterCodec<in B, C, T1>, factory: Function<T1, C>): BiCodec<B, C> = of(
            RecordCodecBuilder.create { instance ->
                instance.group(codec.toRecordParam()).apply(instance, factory)
            },
            StreamCodec.composite(codec.streamCodec, codec.getter, factory),
        )

        /**
         * 指定した[codec1], [codec2]から，別の[BiCodec]を生成します。
         * @param T1 [factory]の第1引数に使われるクラス
         * @param T2 [factory]の第2引数に使われるクラス
         * @param C 変換後のコーデックの対象となるクラス
         */
        @JvmStatic
        fun <B : ByteBuf, C : Any, T1 : Any, T2 : Any> composite(
            codec1: ParameterCodec<in B, C, T1>,
            codec2: ParameterCodec<in B, C, T2>,
            factory: BiFunction<T1, T2, C>,
        ): BiCodec<B, C> = of(
            RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        codec1.toRecordParam(),
                        codec2.toRecordParam(),
                    ).apply(instance, factory)
            },
            StreamCodec.composite(
                codec1.streamCodec,
                codec1.getter,
                codec2.streamCodec,
                codec2.getter,
                factory,
            ),
        )

        /**
         * 指定した[codec1], [codec2], [codec3]から，別の[BiCodec]を生成します。
         * @param T1 [factory]の第1引数に使われるクラス
         * @param T2 [factory]の第2引数に使われるクラス
         * @param T3 [factory]の第3引数に使われるクラス
         * @param C 変換後のコーデックの対象となるクラス
         */
        @JvmStatic
        fun <B : ByteBuf, C : Any, T1 : Any, T2 : Any, T3 : Any> composite(
            codec1: ParameterCodec<in B, C, T1>,
            codec2: ParameterCodec<in B, C, T2>,
            codec3: ParameterCodec<in B, C, T3>,
            factory: Function3<T1, T2, T3, C>,
        ): BiCodec<B, C> = of(
            RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        codec1.toRecordParam(),
                        codec2.toRecordParam(),
                        codec3.toRecordParam(),
                    ).apply(instance, factory)
            },
            StreamCodec.composite(
                codec1.streamCodec,
                codec1.getter,
                codec2.streamCodec,
                codec2.getter,
                codec3.streamCodec,
                codec3.getter,
                factory,
            ),
        )

        /**
         * 指定した[codec1], [codec2], [codec3], [codec4]から，別の[BiCodec]を生成します。
         * @param T1 [factory]の第1引数に使われるクラス
         * @param T2 [factory]の第2引数に使われるクラス
         * @param T3 [factory]の第3引数に使われるクラス
         * @param T4 [factory]の第4引数に使われるクラス
         * @param C 変換後のコーデックの対象となるクラス
         */
        @JvmStatic
        fun <B : ByteBuf, C : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any> composite(
            codec1: ParameterCodec<in B, C, T1>,
            codec2: ParameterCodec<in B, C, T2>,
            codec3: ParameterCodec<in B, C, T3>,
            codec4: ParameterCodec<in B, C, T4>,
            factory: Function4<T1, T2, T3, T4, C>,
        ): BiCodec<B, C> = of(
            RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        codec1.toRecordParam(),
                        codec2.toRecordParam(),
                        codec3.toRecordParam(),
                        codec4.toRecordParam(),
                    ).apply(instance, factory)
            },
            StreamCodec.composite(
                codec1.streamCodec,
                codec1.getter,
                codec2.streamCodec,
                codec2.getter,
                codec3.streamCodec,
                codec3.getter,
                codec4.streamCodec,
                codec4.getter,
                factory,
            ),
        )

        /**
         * 指定した[codec1], [codec2], [codec3], [codec4], [codec5]から，別の[BiCodec]を生成します。
         * @param T1 [factory]の第1引数に使われるクラス
         * @param T2 [factory]の第2引数に使われるクラス
         * @param T3 [factory]の第3引数に使われるクラス
         * @param T4 [factory]の第4引数に使われるクラス
         * @param T5 [factory]の第5引数に使われるクラス
         * @param C 変換後のコーデックの対象となるクラス
         */
        @JvmStatic
        fun <B : ByteBuf, C : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any> composite(
            codec1: ParameterCodec<in B, C, T1>,
            codec2: ParameterCodec<in B, C, T2>,
            codec3: ParameterCodec<in B, C, T3>,
            codec4: ParameterCodec<in B, C, T4>,
            codec5: ParameterCodec<in B, C, T5>,
            factory: Function5<T1, T2, T3, T4, T5, C>,
        ): BiCodec<B, C> = of(
            RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        codec1.toRecordParam(),
                        codec2.toRecordParam(),
                        codec3.toRecordParam(),
                        codec4.toRecordParam(),
                        codec5.toRecordParam(),
                    ).apply(instance, factory)
            },
            StreamCodec.composite(
                codec1.streamCodec,
                codec1.getter,
                codec2.streamCodec,
                codec2.getter,
                codec3.streamCodec,
                codec3.getter,
                codec4.streamCodec,
                codec4.getter,
                codec5.streamCodec,
                codec5.getter,
                factory,
            ),
        )

        /**
         * 指定した[codec1], [codec2], [codec3], [codec4], [codec5], [codec6]から，別の[BiCodec]を生成します。
         * @param T1 [factory]の第1引数に使われるクラス
         * @param T2 [factory]の第2引数に使われるクラス
         * @param T3 [factory]の第3引数に使われるクラス
         * @param T4 [factory]の第4引数に使われるクラス
         * @param T5 [factory]の第5引数に使われるクラス
         * @param T6 [factory]の第6引数に使われるクラス
         * @param C 変換後のコーデックの対象となるクラス
         */
        @JvmStatic
        fun <B : ByteBuf, C : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, T6 : Any> composite(
            codec1: ParameterCodec<in B, C, T1>,
            codec2: ParameterCodec<in B, C, T2>,
            codec3: ParameterCodec<in B, C, T3>,
            codec4: ParameterCodec<in B, C, T4>,
            codec5: ParameterCodec<in B, C, T5>,
            codec6: ParameterCodec<in B, C, T6>,
            factory: Function6<T1, T2, T3, T4, T5, T6, C>,
        ): BiCodec<B, C> = of(
            RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        codec1.toRecordParam(),
                        codec2.toRecordParam(),
                        codec3.toRecordParam(),
                        codec4.toRecordParam(),
                        codec5.toRecordParam(),
                        codec6.toRecordParam(),
                    ).apply(instance, factory)
            },
            StreamCodec.composite(
                codec1.streamCodec,
                codec1.getter,
                codec2.streamCodec,
                codec2.getter,
                codec3.streamCodec,
                codec3.getter,
                codec4.streamCodec,
                codec4.getter,
                codec5.streamCodec,
                codec5.getter,
                codec6.streamCodec,
                codec6.getter,
                factory,
            ),
        )
    }

    // Encode & Decode
    fun <T : Any> encode(ops: DynamicOps<T>, input: V): DataResult<T> = codec.encodeStart(ops, input)

    fun <T : Any> decode(ops: DynamicOps<T>, input: T): DataResult<V> = codec.parse(ops, input)

    fun encode(buf: B, input: V): Result<Unit> = runCatching { streamCodec.encode(buf, input) }

    fun decode(buf: B): Result<V> = runCatching { streamCodec.decode(buf) }

    // Convert

    /**
     * 指定した[to]と[from]から，別の[BiCodec]に変換します。
     * @param S 変換後のコーデックの対象となるクラス
     * @param to [V]から[S]に変換するブロック
     * @param from [S]から[V]に変換するブロック
     * @return [S]を対象とする[BiCodec]
     */
    fun <S : Any> xmap(to: Function<V, S>, from: Function<S, V>): BiCodec<B, S> = of(codec.xmap(to, from), streamCodec.map(to, from))

    /**
     * 指定した[to]と[from]から，別の[BiCodec]に変換します。
     * @param S 変換後のコーデックの対象となるクラス
     * @param to [V]から[S]に変換するブロック
     * @param from [S]から[V]に変換するブロック
     * @return [S]を対象とする[BiCodec]
     */
    fun <S : Any> flatXmap(to: Function<V, S>, from: Function<S, V>): BiCodec<B, S> = of(
        codec.flatXmap({ it.runCatching(to::apply).toData() }, { it.runCatching(from::apply).toData() }),
        streamCodec.map({ it.let(to::apply) }, { it.let(from::apply) }),
    )

    fun validate(validator: UnaryOperator<V>): BiCodec<B, V> = flatXmap(validator::apply, validator::apply)

    fun <E : Any> dispatch(
        typeKey: String,
        type: Function<E, V>,
        codec: Function<V, MapCodec<out E>>,
        streamCodec: Function<V, StreamCodec<in B, out E>>,
    ): BiCodec<B, E> = of(
        this.codec.dispatch(typeKey, type, codec),
        this.streamCodec.dispatch(type, streamCodec),
    )

    fun <E : Any> dispatch(
        type: Function<E, V>,
        codec: Function<V, MapCodec<out E>>,
        streamCodec: Function<V, StreamCodec<in B, out E>>,
    ): BiCodec<B, E> = dispatch("type", type, codec, streamCodec)

    /**
     * [MapBiCodec]に変換します。
     */
    fun toMap(): MapBiCodec<B, V> = MapBiCodec.of(MapCodec.assumeMapUnsafe(codec), streamCodec)

    /**
     * [B]を[S]に置換した[BiCodec]を返します。
     * @param S [B]を継承したクラス
     */
    fun <S : B> cast(): BiCodec<S, V> = of(codec, streamCodec.cast())

    // MapBiCodec

    /**
     * 指定した[フィールド名][name]から[MapBiCodec]に変換します。
     */
    fun fieldOf(name: String): MapBiCodec<B, V> = MapBiCodec.of(codec.fieldOf(name), streamCodec)

    /**
     * 指定した[フィールド名][name]から[Optional]の[MapBiCodec]に変換します。
     */
    fun optionalFieldOf(name: String): MapBiCodec<B, Optional<V>> = MapBiCodec.of(codec.optionalFieldOf(name), streamCodec.toOptional())

    /**
     * 指定した[フィールド名][name]と[デフォルト値][defaultValue]から[MapBiCodec]に変換します。
     */
    fun optionalFieldOf(name: String, defaultValue: V): MapBiCodec<B, V> =
        MapBiCodec.of(codec.optionalFieldOf(name, defaultValue), streamCodec)

    /**
     * 指定した[フィールド名][name]と[デフォルト値][defaultValue]から[MapBiCodec]に変換します。
     */
    fun optionalOrElseField(name: String, defaultValue: V): MapBiCodec<B, V> =
        MapBiCodec.of(codec.fieldOf(name).orElse(defaultValue), streamCodec)

    // List

    /**
     * [List]の[BiCodec]に変換します。
     */
    fun listOf(): BiCodec<B, List<V>> = of(codec.listOf(), streamCodec.listOf())

    /**
     * [List]の[BiCodec]に変換します。
     * @param min リストの[長さ][List.size]の最小値
     * @param max リストの[長さ][List.size]の最大値
     * @return リストの[長さ][List.size]が制限された[List]の[BiCodec]
     */
    fun listOf(min: Int, max: Int): BiCodec<B, List<V>> = of(codec.listOf(min, max), streamCodec.listOf())

    /**
     * 要素が一つの場合はそのままコーデックする[List]の[BiCodec]に変換します。
     */
    fun listOrElement(): BiCodec<B, List<V>> = of(codec.listOrElement(), streamCodec.listOf())

    /**
     * 要素が一つの場合はそのままコーデックする[List]の[BiCodec]に変換します。
     * @param min リストの[長さ][List.size]の最小値
     * @param max リストの[長さ][List.size]の最大値
     * @return リストの[長さ][List.size]が制限された[List]の[BiCodec]
     */
    fun listOrElement(min: Int, max: Int): BiCodec<B, List<V>> = of(codec.listOrElement(min, max), streamCodec.listOf())

    // Optional

    /**
     * [Optional]の[BiCodec]に変換します。
     */
    fun toOptional(): BiCodec<B, Optional<V>> = of(HTOptionalCodec(codec), streamCodec.toOptional())
}

private fun <B : ByteBuf, V : Any> StreamCodec<B, V>.listOf(): StreamCodec<B, List<V>> = apply(ByteBufCodecs.list())

private fun <B : ByteBuf, V : Any> StreamCodec<B, V>.toOptional(): StreamCodec<B, Optional<V>> = ByteBufCodecs.optional(this)
