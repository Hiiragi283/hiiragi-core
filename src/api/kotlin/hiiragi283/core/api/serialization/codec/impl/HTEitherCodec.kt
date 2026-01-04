package hiiragi283.core.api.serialization.codec.impl

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import hiiragi283.core.api.monad.Either

/**
 * [Either]向けの[Codec]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 * @see com.mojang.serialization.codecs.EitherCodec
 * @see com.mojang.serialization.codecs.XorCodec
 */
class HTEitherCodec<A, B>(val left: Codec<A>, val right: Codec<B>, val strict: Boolean) : Codec<Either<A, B>> {
    override fun <T : Any> encode(input: Either<A, B>, ops: DynamicOps<T>, prefix: T): DataResult<T> =
        input.map({ left.encode(it, ops, prefix) }, { right.encode(it, ops, prefix) })

    override fun <T : Any> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<Either<A, B>, T>> {
        val leftResult: DataResult<Pair<Either<A, B>, T>> = left
            .decode(ops, input)
            .map { pair: Pair<A, T> -> pair.mapFirst { Either.Left(it) } }
        val rightResult: DataResult<Pair<Either<A, B>, T>> = right
            .decode(ops, input)
            .map { pair: Pair<B, T> -> pair.mapFirst { Either.Right(it) } }
        if (leftResult.isSuccess && rightResult.isSuccess && strict) {
            return DataResult.error {
                "Both alternatives read successfully, ca not pick the correct one; " +
                    "Left: ${leftResult.result().get()}, Right: ${rightResult.result().get()}"
            }
        }
        if (leftResult.isSuccess) {
            return leftResult
        }
        if (rightResult.isSuccess) {
            return rightResult
        }

        if (strict) {
            return leftResult.apply2({ _, rightPair -> rightPair }, rightResult)
        } else {
            if (leftResult.hasResultOrPartial()) {
                return leftResult
            }
            if (rightResult.hasResultOrPartial()) {
                return rightResult
            }
            return DataResult.error {
                "Failed to parse either. Left: ${leftResult.error().orElseThrow().message()};" +
                    "Right: ${rightResult.error().orElseThrow().message()}"
            }
        }
    }
}
