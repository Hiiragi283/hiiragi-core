package hiiragi283.core.api.serialization.codec

import com.mojang.datafixers.util.Function3
import com.mojang.datafixers.util.Function4
import com.mojang.datafixers.util.Function5
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.UnaryOperator

/**
 * [MapCodec]と[StreamCodec]を束ねたクラス。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
@ConsistentCopyVisibility
@JvmRecord
data class MapBiCodec<B : ByteBuf, V : Any> private constructor(val codec: MapCodec<V>, val streamCodec: StreamCodec<B, V>) {
    companion object {
        /**
         * 指定した[codec]と[streamCodec]から[MapBiCodec]を作成します。
         * @param B [StreamCodec]で使われるパケットのクラス
         * @param V コーデック対象のクラス
         */
        @JvmStatic
        fun <B : ByteBuf, V : Any> of(codec: MapCodec<V>, streamCodec: StreamCodec<B, V>): MapBiCodec<B, V> = MapBiCodec(codec, streamCodec)

        /**
         * 指定した[codec]から，別の[MapBiCodec]を生成します。
         * @param T1 [factory]の第1引数に使われるクラス
         * @param C 変換後のコーデックの対象となるクラス
         */
        @JvmStatic
        fun <B : ByteBuf, C : Any, T1 : Any> composite(codec: ParameterCodec<in B, C, T1>, factory: Function<T1, C>): MapBiCodec<B, C> = of(
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(codec.toRecordParam()).apply(instance, factory)
            },
            StreamCodec.composite(codec.streamCodec, codec.getter, factory),
        )

        /**
         * 指定した[codec1], [codec2]から，別の[MapBiCodec]を生成します。
         * @param T1 [factory]の第1引数に使われるクラス
         * @param T2 [factory]の第2引数に使われるクラス
         * @param C 変換後のコーデックの対象となるクラス
         */
        @JvmStatic
        fun <B : ByteBuf, C : Any, T1 : Any, T2 : Any> composite(
            codec1: ParameterCodec<in B, C, T1>,
            codec2: ParameterCodec<in B, C, T2>,
            factory: BiFunction<T1, T2, C>,
        ): MapBiCodec<B, C> = of(
            RecordCodecBuilder.mapCodec { instance ->
                instance
                    .group(
                        codec1.toRecordParam(),
                        codec2.toRecordParam(),
                    ).apply(instance, factory)
            },
            StreamCodec.composite(codec1.streamCodec, codec1.getter, codec2.streamCodec, codec2.getter, factory),
        )

        /**
         * 指定した[codec1], [codec2], [codec3]から，別の[MapBiCodec]を生成します。
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
        ): MapBiCodec<B, C> = of(
            RecordCodecBuilder.mapCodec { instance ->
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
         * 指定した[codec1], [codec2], [codec3], [codec4]から，別の[MapBiCodec]を生成します。
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
        ): MapBiCodec<B, C> = of(
            RecordCodecBuilder.mapCodec { instance ->
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
         * 指定した[codec1], [codec2], [codec3], [codec4], [codec5]から，別の[MapBiCodec]を生成します。
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
        ): MapBiCodec<B, C> = of(
            RecordCodecBuilder.mapCodec { instance ->
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
    }

    /**
     * [BiCodec]に変換します。
     */
    fun toCodec(): BiCodec<B, V> = BiCodec.of(codec.codec(), streamCodec)

    /**
     * 指定した[to]と[from]から，別の[MapBiCodec]に変換します。
     * @param S 変換後のコーデックの対象となるクラス
     * @param to [V]から[S]に変換するブロック
     * @param from [S]から[V]に変換するブロック
     * @return [S]を対象とする[MapBiCodec]
     */
    fun <S : Any> xmap(to: Function<V, S>, from: Function<S, V>): MapBiCodec<B, S> = of(codec.xmap(to, from), streamCodec.map(to, from))

    /**
     * 指定した[to]と[from]から，別の[MapBiCodec]に変換します。
     * @param S 変換後のコーデックの対象となるクラス
     * @param to [V]から[S]の[Result]に変換するブロック
     * @param from [S]から[V]の[Result]にに変換するブロック
     * @return [S]を対象とする[MapBiCodec]
     */
    fun <S : Any> flatXmap(to: Function<V, S>, from: Function<S, V>): MapBiCodec<B, S> = of(
        codec.flatXmap({ it.runCatching(to::apply).toData() }, { it.runCatching(from::apply).toData() }),
        streamCodec.map({ it.let(to::apply) }, { it.let(from::apply) }),
    )

    fun validate(validator: UnaryOperator<V>): MapBiCodec<B, V> = flatXmap(validator::apply, validator::apply)

    /**
     * 指定した[getter]から[ParameterCodec]に変換します。
     */
    fun <C : Any> forGetter(getter: Function<C, V>): ParameterCodec<B, C, V> = ParameterCodec.of(this, getter)

    inline fun <R : Any> toSerializer(factory: (MapCodec<V>, StreamCodec<B, V>) -> R): R {
        val (mapCodec: MapCodec<V>, streamCodec: StreamCodec<B, V>) = this
        return factory(mapCodec, streamCodec)
    }
}
