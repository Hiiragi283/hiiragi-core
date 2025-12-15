package hiiragi283.core.api.tuple

import com.mojang.datafixers.util.Either

fun <U> unwrapEither(either: Either<out U, out U>): U = Either.unwrap(either)
