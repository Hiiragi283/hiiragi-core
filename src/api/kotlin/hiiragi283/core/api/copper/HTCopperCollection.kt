package hiiragi283.core.api.copper

/**
 * 銅系コンテンツを管理するクラスです。
 * @param T 保持する値のクラス
 * @param unaffected 酸化していない要素
 * @param exposed 酸化が1段階目まで進行した要素
 * @param weathered 酸化が2段階目まで進行した要素
 * @param oxidized 酸化が3段階目まで進行した要素
 * @see HTWeatheringCoppers
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
data class HTCopperCollection<out T>(val unaffected: T, val exposed: T, val weathered: T, val oxidized: T) : AbstractCollection<T>() {
    operator fun get(phase: HTCopperPhase): T = when (phase) {
        HTCopperPhase.UNAFFECTED -> unaffected
        HTCopperPhase.EXPOSED -> exposed
        HTCopperPhase.WEATHERED -> weathered
        HTCopperPhase.OXIDIZED -> oxidized
    }

    fun asSequenceWithPhase(): Sequence<Pair<HTCopperPhase, T>> = HTCopperPhase.entries.asSequence().map { it to get(it) }

    fun asSequence(): Sequence<T> = HTCopperPhase.entries.asSequence().map(::get)

    override val size: Int = 4

    override fun isEmpty(): Boolean = false

    override fun iterator(): Iterator<T> = asSequence().iterator()

    inline fun <R> map(transform: (T) -> R): HTCopperCollection<R> = HTCopperCollection(
        transform(unaffected),
        transform(exposed),
        transform(weathered),
        transform(oxidized),
    )

    inline fun <U, R> zip(other: HTCopperCollection<U>, transform: (T, U) -> R): HTCopperCollection<R> = HTCopperCollection(
        transform(unaffected, other.unaffected),
        transform(exposed, other.exposed),
        transform(weathered, other.weathered),
        transform(oxidized, other.oxidized),
    )
}

inline fun <T> HTCopperCollection(init: (phase: HTCopperPhase) -> T): HTCopperCollection<T> = HTCopperCollection(
    init(HTCopperPhase.UNAFFECTED),
    init(HTCopperPhase.EXPOSED),
    init(HTCopperPhase.WEATHERED),
    init(HTCopperPhase.OXIDIZED),
)
