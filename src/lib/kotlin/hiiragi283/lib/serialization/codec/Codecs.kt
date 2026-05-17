package hiiragi283.lib.serialization.codec

import com.mojang.serialization.Codec
import hiiragi283.lib.util.DFUEither
import java.util.function.UnaryOperator

//    List    //

/**
 * この[Codec][this]を[List]の[Codec]に変換します。
 * @param range リストの[長さ][List.size]の範囲
 * @return リストの[長さ][List.size]が制限された[List]の[Codec]
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
fun <A : Any> Codec<A>.listOf(range: IntRange): Codec<List<A>> = this.listOf(range.first, range.last)

/**
 * この[Codec][this]を，要素が一つの場合はそのままコーデックする[List]の[Codec]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <A : Any> Codec<A>.listOrElement(): Codec<List<A>> = Codec.either(this.listOf(), this).xmap(
    { either: DFUEither<List<A>, A> -> either.map(UnaryOperator.identity(), ::listOf) },
    { list: List<A> -> if (list.size == 1) DFUEither.right(list[0]) else DFUEither.left(list) },
)

/**
 * この[Codec][this]を，要素が一つの場合はそのままコーデックする[List]の[Codec]に変換します。
 * @param range リストの[長さ][List.size]の範囲
 * @return リストの[長さ][List.size]が制限された[List]の[Codec]
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
fun <A : Any> Codec<A>.listOrElement(range: IntRange): Codec<List<A>> = this.listOrElement(range.first, range.last)

/**
 * この[Codec][this]を，要素が一つの場合はそのままコーデックする[List]の[Codec]に変換します。
 * @param min リストの[長さ][List.size]の最小値
 * @param max リストの[長さ][List.size]の最大値
 * @return リストの[長さ][List.size]が制限された[List]の[Codec]
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <A : Any> Codec<A>.listOrElement(min: Int, max: Int): Codec<List<A>> = Codec.either(this.listOf(min, max), this).xmap(
    { either: DFUEither<List<A>, A> -> either.map(UnaryOperator.identity(), ::listOf) },
    { list: List<A> -> if (list.size == 1) DFUEither.right(list[0]) else DFUEither.left(list) },
)

//    Set    //

/**
 * この[Codec][this]を[Set]の[Codec]に変換します。
 * @return [Set]の[Codec]
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
fun <A : Any> Codec<List<A>>.setOf(): Codec<Set<A>> = this.xmap(List<A>::toSet, Set<A>::toList)
