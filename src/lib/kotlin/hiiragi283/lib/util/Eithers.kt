package hiiragi283.lib.util

import com.mojang.datafixers.util.Either

/**
 * この[Either][this]を[Ior]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <A, B> Either<A, B>.toIor(): Ior<A, B> = this.map({ Ior.Left(it) }, { Ior.Right(it) })

fun <L> Either<L, *>.leftOrNull(): L? = this.left().orElse(null)

fun <R> Either<*, R>.rightOrNull(): R? = this.right().orElse(null)

fun <U> Either<out U, out U>.unwrap(): U = Either.unwrap(this)
