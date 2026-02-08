package hiiragi283.core.api.serialization.codec

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.monad.Either
import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.serialization.codec.impl.HTEitherMapCodec
import hiiragi283.core.api.serialization.codec.impl.HTEitherStreamCodec
import hiiragi283.core.api.serialization.codec.impl.HTIorMapCodec
import hiiragi283.core.api.serialization.codec.impl.HTIorStreamCodec
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec

/**
 * [MapBiCodec]に関するメソッドを集めたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
object MapBiCodecs {
    /**
     * 指定した[left], [right]から，[Either]の[BiCodec]を返します。
     * @param left [A]を対象とする[BiCodec]
     * @param right [B1]を対象とする[BiCodec]
     * @return [Either]の[MapBiCodec]
     */
    @JvmStatic
    fun <B : ByteBuf, A : Any, B1 : Any> either(left: MapBiCodec<in B, A>, right: MapBiCodec<in B, B1>): MapBiCodec<B, Either<A, B1>> =
        MapBiCodec
            .of(
                HTEitherMapCodec(left.codec, right.codec),
                HTEitherStreamCodec(left.streamCodec, right.streamCodec),
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

    /**
     * 指定した[left], [right]から，[Ior]の[BiCodec]を返します。
     * @param left [L]を対象とする[MapBiCodec]
     * @param right [R]を対象とする[MapBiCodec]
     * @return [Ior]の[MapBiCodec]
     */
    @JvmStatic
    fun <B : ByteBuf, L : Any, R : Any> ior(left: MapBiCodec<in B, L>, right: MapBiCodec<in B, R>): MapBiCodec<B, Ior<L, R>> =
        MapBiCodec.of(
            HTIorMapCodec(left.codec, right.codec),
            HTIorStreamCodec(left.streamCodec, right.streamCodec),
        )

    /**
     * 指定した[instance]を常に返す[MapBiCodec]を返します。
     */
    @JvmStatic
    fun <B : ByteBuf, V : Any> unit(instance: V): MapBiCodec<B, V> = MapBiCodec.of(MapCodec.unit(instance), StreamCodec.unit(instance))
}
