package hiiragi283.core.api.monad

import hiiragi283.core.api.function.identity

fun <U> Either<out U, out U>.unwrap(): U = map(identity(), identity())
