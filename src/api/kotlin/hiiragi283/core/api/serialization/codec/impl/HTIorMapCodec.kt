package hiiragi283.core.api.serialization.codec.impl

import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.MapCodec
import com.mojang.serialization.MapLike
import com.mojang.serialization.RecordBuilder
import hiiragi283.core.api.util.Ior
import java.util.stream.Stream

/**
 * [Ior]向けの[MapCodec]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
internal class HTIorMapCodec<A, B>(val left: MapCodec<A>, val right: MapCodec<B>) : MapCodec<Ior<A, B>>() {
    override fun <T : Any> keys(ops: DynamicOps<T>): Stream<T> = Stream.concat(left.keys(ops), right.keys(ops))

    override fun <T : Any> decode(ops: DynamicOps<T>, input: MapLike<T>): DataResult<Ior<A, B>> {
        val leftResult: DataResult<A> = left.decode(ops, input)
        val rightResult: DataResult<B> = right.decode(ops, input)

        val bothResult: DataResult<Ior<A, B>> = leftResult.flatMap { leftIn: A ->
            rightResult.map { rightIn: B -> Ior.Both(leftIn, rightIn) }
        }
        if (bothResult.isSuccess) return bothResult
        if (leftResult.isSuccess) {
            return when {
                rightResult.isSuccess ->
                    leftResult.flatMap { leftIn: A ->
                        rightResult.map { rightIn: B -> Ior.Both(leftIn, rightIn) }
                    }
                else -> leftResult.map { Ior.Left(it) }
            }
        } else {
            return when {
                rightResult.isSuccess -> rightResult.map { Ior.Right(it) }
                else ->
                    DataResult.error {
                        val leftError: String = leftResult.error().orElseThrow().message()
                        val rightError: String = rightResult.error().orElseThrow().message()
                        "Failed to parse ior. Left: $leftError; Right: $rightError;"
                    }
            }
        }
    }

    override fun <T : Any> encode(input: Ior<A, B>, ops: DynamicOps<T>, prefix: RecordBuilder<T>): RecordBuilder<T> = input.fold(
        { left.encode(it, ops, prefix) },
        { right.encode(it, ops, prefix) },
        { left: A, right: B ->
            this.left.encode(left, ops, prefix)
            this.right.encode(right, ops, prefix)
        },
    )
}
