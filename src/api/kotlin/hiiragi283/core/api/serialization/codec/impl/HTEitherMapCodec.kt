package hiiragi283.core.api.serialization.codec.impl

import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.MapCodec
import com.mojang.serialization.MapLike
import com.mojang.serialization.RecordBuilder
import hiiragi283.core.api.monad.Either
import java.util.stream.Stream

/**
 * [Either]向けの[MapCodec]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 * @see com.mojang.serialization.codecs.EitherMapCodec
 */
class HTEitherMapCodec<A, B>(val left: MapCodec<A>, val right: MapCodec<B>) : MapCodec<Either<A, B>>() {
    override fun <T : Any> keys(ops: DynamicOps<T>): Stream<T> = Stream.concat(left.keys(ops), right.keys(ops))

    override fun <T : Any> decode(ops: DynamicOps<T>, input: MapLike<T>): DataResult<Either<A, B>> {
        val leftResult: DataResult<Either<A, B>> = left.decode(ops, input).map { Either.Left(it) }
        if (leftResult.isSuccess) {
            return leftResult
        }
        val rightResult: DataResult<Either<A, B>> = right.decode(ops, input).map { Either.Right(it) }
        if (rightResult.isSuccess) {
            return rightResult
        }
        return leftResult.apply2({ _, rightPair -> rightPair }, rightResult)
    }

    override fun <T : Any> encode(input: Either<A, B>, ops: DynamicOps<T>, prefix: RecordBuilder<T>): RecordBuilder<T> =
        input.map({ left.encode(it, ops, prefix) }, { right.encode(it, ops, prefix) })
}
