package hiiragi283.core.api.function

/**
 * 指定された引数からハッシュ値を生成します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun generateHash(vararg obj: Any?): Int = arrayOf(*obj).fold(0) { result: Int, obj: Any? -> 31 * result + (obj?.hashCode() ?: 0) }

/**
 * 恒等操作を行うブロックを返します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <T> identity(): (T) -> T = { it }

fun <IP, R> (() -> IP).andThen(f: (IP) -> R): () -> R = { this().let(f) }

fun <P1, IP, R> ((P1) -> IP).andThen(f: (IP) -> R): (P1) -> R = { p1: P1 -> this(p1).let(f) }

fun <P1, P2, IP, R> ((P1, P2) -> IP).andThen(f: (IP) -> R): (P1, P2) -> R = { p1: P1, p2: P2 -> this(p1, p2).let(f) }

fun <P1, IP, R> ((IP) -> R).compose(f: (P1) -> IP): (P1) -> R = { p1: P1 -> f(p1).let(this) }

fun <P1, R> ((P1) -> R).partially1(p1: P1): () -> R = { this(p1) }

fun <P1, P2, R> ((P1, P2) -> R).partially1(p1: P1): (P2) -> R = { p2: P2 -> this(p1, p2) }

fun <P1, P2, P3, R> ((P1, P2, P3) -> R).partially1(p1: P1): (P2, P3) -> R = { p2: P2, p3: P3 -> this(p1, p2, p3) }

fun <P1, P2, P3, P4, R> ((P1, P2, P3, P4) -> R).partially1(p1: P1): (P2, P3, P4) -> R = { p2: P2, p3: P3, p4: P4 -> this(p1, p2, p3, p4) }

fun <P1, P2, R> ((P1, P2) -> R).partially2(p1: P1, p2: P2): () -> R = { this(p1, p2) }

fun <P1, P2, P3, R> ((P1, P2, P3) -> R).partially2(p1: P1, p2: P2): (P3) -> R = { p3: P3 -> this(p1, p2, p3) }

// Predicate
fun (() -> Boolean).and(other: () -> Boolean): () -> Boolean = { this() && other() }

fun <P1> ((P1) -> Boolean).and(other: () -> Boolean): (P1) -> Boolean = { p1: P1 -> this(p1) && other() }

fun (() -> Boolean).negate(): () -> Boolean = { !this() }

fun <P1> ((P1) -> Boolean).negate(): (P1) -> Boolean = { p1: P1 -> !this(p1) }

fun <P1, P2> ((P1, P2) -> Boolean).negate(): (P1, P2) -> Boolean = { p1: P1, p2: P2 -> !this(p1, p2) }

fun <P1, P2, P3> ((P1, P2, P3) -> Boolean).negate(): (P1, P2, P3) -> Boolean = { p1: P1, p2: P2, p3: P3 -> !this(p1, p2, p3) }
