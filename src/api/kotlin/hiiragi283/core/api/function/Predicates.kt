package hiiragi283.core.api.function

// () -> Boolean
fun (() -> Boolean).negate(): () -> Boolean = { !this() }

fun (() -> Boolean).and(other: () -> Boolean): () -> Boolean = { this() && other() }

fun (() -> Boolean).or(other: () -> Boolean): () -> Boolean = { this() || other() }

// (P1) -> Boolean
fun <P1> ((P1) -> Boolean).negate(): (P1) -> Boolean = { p1: P1 -> !this(p1) }

fun <P1> ((P1) -> Boolean).and(other: () -> Boolean): (P1) -> Boolean = { p1: P1 -> this(p1) && other() }

fun <P1> ((P1) -> Boolean).or(other: () -> Boolean): (P1) -> Boolean = { p1: P1 -> this(p1) || other() }

// (P1, P2) -> Boolean
fun <P1, P2> ((P1, P2) -> Boolean).negate(): (P1, P2) -> Boolean = { p1: P1, p2: P2 -> !this(p1, p2) }

// (P1, P2, P3) -> Boolean
fun <P1, P2, P3> ((P1, P2, P3) -> Boolean).negate(): (P1, P2, P3) -> Boolean = { p1: P1, p2: P2, p3: P3 -> !this(p1, p2, p3) }
