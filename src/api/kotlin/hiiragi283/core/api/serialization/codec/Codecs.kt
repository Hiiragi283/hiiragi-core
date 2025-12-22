package hiiragi283.core.api.serialization.codec

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import hiiragi283.core.api.function.identity
import kotlin.collections.List

//    Codec    //

/**
 * この[Codec][this]を，要素が一つの場合はそのままコーデックする[List]の[Codec]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <A : Any> Codec<A>.listOrElement(): Codec<List<A>> = Codec.either(this.listOf(), this).xmap(
    { either: Either<List<A>, A> -> either.map(identity(), ::listOf) },
    { list: List<A> -> if (list.size == 1) Either.right(list[0]) else Either.left(list) },
)

/**
 * この[Codec][this]を，要素が一つの場合はそのままコーデックする[List]の[Codec]に変換します。
 * @param min リストの[長さ][List.size]の最小値
 * @param max リストの[長さ][List.size]の最大値
 * @return リストの[長さ][List.size]が制限された[List]の[BiCodec]
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <A : Any> Codec<A>.listOrElement(min: Int, max: Int): Codec<List<A>> = Codec.either(this.listOf(min, max), this).xmap(
    { either: Either<List<A>, A> -> either.map(identity(), ::listOf) },
    { list: List<A> -> if (list.size == 1) Either.right(list[0]) else Either.left(list) },
)

//    BiCodec    //

/**
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <T : Any> Result<T>.toData(): DataResult<T> = fold(DataResult<T>::success) { throwable: Throwable ->
    DataResult.error { throwable.message ?: "Thrown exception" }
}
