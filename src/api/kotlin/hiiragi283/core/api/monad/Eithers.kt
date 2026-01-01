package hiiragi283.core.api.monad

import hiiragi283.core.api.function.identity
import com.mojang.datafixers.util.Either as MojEither

fun <U> Either<out U, out U>.unwrap(): U = map(identity(), identity())

fun <A, B> MojEither<A, B>.toHt(): Either<A, B> = this.map({ Either.Left(it) }, { Either.Right(it) })

fun <A, B> Either<A, B>.toMoj(): MojEither<A, B> = this.map({ MojEither.left(it) }, { MojEither.right(it) })
