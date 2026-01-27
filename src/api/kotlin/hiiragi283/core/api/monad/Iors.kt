package hiiragi283.core.api.monad

/**
 * この[Either][this]を[Ior]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <A, B> Either<A, B>.toIor(): Ior<A, B> = this.map({ Ior.Left(it) }, { Ior.Right(it) })

/**
 * この[Pair][this]を[Ior]に変換します。
 * @return [Pair]自体が`null`の場合，または左右の値が`null`の場合は`null`
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <A : Any, B : Any> Pair<A?, B?>?.toIor(): Ior<A, B>? {
    val (first: A?, second: B?) = this ?: return null
    return when {
        first != null -> when {
            second != null -> Ior.Both(first, second)
            else -> Ior.Left(first)
        }
        else -> when {
            second != null -> Ior.Right(second)
            else -> null
        }
    }
}

/**
 * この[Pair][this]を[Ior]に変換します。
 * @throws IllegalStateException [Pair]自体が`null`の場合，または左右の値が`null`の場合
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
fun <A : Any, B : Any> Pair<A?, B?>.toIorOrThrow(message: Any = "Either left or right value required"): Ior<A, B> =
    this.toIor() ?: error(message)
