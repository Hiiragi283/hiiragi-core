package hiiragi283.core.api.serialization.codec

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.monad.toIor
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

/**
 * [MapBiCodec]に関するメソッドを集めたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
object MapBiCodecs {
    /**
     * 指定した[first], [second]から，[Either]の[BiCodec]を返します。
     * @param first [F]を対象とする[MapBiCodec]
     * @param second [S]を対象とする[MapBiCodec]
     * @return [Either]の[MapBiCodec]
     */
    @JvmStatic
    fun <B : ByteBuf, F : Any, S : Any> either(first: MapBiCodec<in B, F>, second: MapBiCodec<in B, S>): MapBiCodec<B, Either<F, S>> =
        MapBiCodec.of(
            Codec.mapEither(first.codec, second.codec),
            ByteBufCodecs.either(first.streamCodec, second.streamCodec),
        )

    /**
     * 指定した[first], [second]から，[Pair]の[BiCodec]を返します。
     * @param first [F]を対象とする[MapBiCodec]
     * @param second [S]を対象とする[MapBiCodec]
     * @return [Pair]の[MapBiCodec]
     */
    @JvmStatic
    fun <B : ByteBuf, F : Any, S : Any> pair(first: MapBiCodec<in B, F>, second: MapBiCodec<in B, S>): MapBiCodec<B, Pair<F, S>> =
        MapBiCodec.composite(first.forGetter(Pair<F, S>::first), second.forGetter(Pair<F, S>::second), ::Pair)

    @JvmStatic
    fun <B : ByteBuf, F : Any, S : Any, T : Any> triple(
        first: MapBiCodec<in B, F>,
        second: MapBiCodec<in B, S>,
        third: MapBiCodec<in B, T>,
    ): MapBiCodec<B, Triple<F, S, T>> = MapBiCodec.composite(
        first.forGetter(Triple<F, S, T>::first),
        second.forGetter(Triple<F, S, T>::second),
        third.forGetter(Triple<F, S, T>::third),
        ::Triple,
    )

    /**
     * 指定した[left], [right]から，[Ior]の[BiCodec]を返します。
     * @param left [L]を対象とする[Optional]の[MapBiCodec]
     * @param right [R]を対象とする[Optional]の[MapBiCodec]
     * @return [Ior]の[MapBiCodec]
     */
    @JvmStatic
    fun <B : ByteBuf, L : Any, R : Any> ior(
        left: MapBiCodec<in B, Optional<L>>,
        right: MapBiCodec<in B, Optional<R>>,
    ): MapBiCodec<B, Ior<L, R>> = pair(left, right).flatXmap(
        { pair: Pair<Optional<L>, Optional<R>> ->
            (pair.first.getOrNull() to pair.second.getOrNull()).toIor() ?: error("Cannot serialize empty ior")
        },
        { ior: Ior<L, R> ->
            ior.fold(
                { Optional.of(it) to Optional.empty() },
                { Optional.empty<L>() to Optional.of(it) },
                { leftIn: L, rightIn: R -> Optional.of(leftIn) to Optional.of(rightIn) },
            )
        },
    )

    /**
     * 指定した[instance]を常に返す[MapBiCodec]を返します。
     */
    @JvmStatic
    fun <B : ByteBuf, V : Any> unit(instance: V): MapBiCodec<B, V> = MapBiCodec.of(MapCodec.unit(instance), StreamCodec.unit(instance))
}
