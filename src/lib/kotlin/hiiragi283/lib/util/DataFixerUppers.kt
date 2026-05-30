package hiiragi283.lib.util

import com.mojang.serialization.DataResult

//    DFUEither <-> Either    //

typealias DFUEither<A, B> = com.mojang.datafixers.util.Either<A, B>

val <A, B> DFUEither<A, B>.kotlin: Either<A, B> get() = this.map({ Either.Left(it) }, { Either.Right(it) })

val <A, B> Either<A, B>.java: DFUEither<A, B> get() = this.fold({ DFUEither.left(it) }, { DFUEither.right(it) })

//    DFUPair <-> Pair    //

typealias DFUPair<A, B> = com.mojang.datafixers.util.Pair<A, B>

val <A, B> DFUPair<A, B>.kotlin: Pair<A, B> get() = this.first to this.second

val <A, B> Pair<A, B>.java: DFUPair<A, B> get() = DFUPair.of(this.first, this.second)

//    DataResult <-> HTTextResult    //

fun <R> DataResult<R>.toText(): HTTextResult<R> = when (this) {
    is DataResult.Error -> HTTextResult(this.message())
    is DataResult.Success -> this.value().right()
}

fun <R> HTTextResult<R>.toData(): DataResult<R> = this.fold({ DataResult.error(it::value) }, { DataResult.success(it) })
