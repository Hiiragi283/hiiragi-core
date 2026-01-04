package hiiragi283.core.api.serialization.codec.impl

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import java.util.Optional

/**
 * [Optional]向けの[Codec]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 * @see net.minecraft.util.ExtraCodecs.optionalEmptyMap
 */
class HTOptionalCodec<A : Any>(val codec: Codec<A>) : Codec<Optional<A>> {
    override fun <T : Any> encode(input: Optional<A>, ops: DynamicOps<T>, prefix: T): DataResult<T> = when {
        input.isEmpty -> DataResult.success(ops.emptyMap())
        else -> codec.encode(input.get(), ops, prefix)
    }

    override fun <T : Any> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<Optional<A>, T>> = codec
        .decode(ops, input)
        .mapOrElse(
            { pair: Pair<A, T> -> pair.mapFirst(Optional<A>::of) },
            { Pair.of(Optional.empty(), input) },
        ).let { DataResult.success(it) }
}
