package hiiragi283.core.api.util

import com.mojang.datafixers.util.Either

fun <L> Either<L, *>.leftOrNull(): L? = this.left().orElse(null)

fun <R> Either<*, R>.rightOrNull(): R? = this.right().orElse(null)

fun <U> Either<out U, out U>.unwrap(): U = Either.unwrap(this)
