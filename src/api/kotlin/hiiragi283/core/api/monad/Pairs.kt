package hiiragi283.core.api.monad

inline fun <A, B, C> Pair<A, B>.mapFirst(first: (A) -> C): Pair<C, B> = first(this.first) to this.second

inline fun <A, B, C> Pair<A, B>.mapSecond(second: (B) -> C): Pair<A, C> = this.first to second(this.second)
