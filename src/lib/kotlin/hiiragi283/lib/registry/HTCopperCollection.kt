package hiiragi283.lib.registry

import net.minecraft.world.level.block.WeatheringCopper

/**
 * 銅系コンテンツを管理するクラスです。
 * @param T 保持する値のクラス
 * @param unaffected 酸化していない要素
 * @param exposed 酸化が1段階目まで進行した要素
 * @param weathered 酸化が2段階目まで進行した要素
 * @param oxidized 酸化が3段階目まで進行した要素
 * @see HTWeatheringCopperBlocks
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data class HTCopperCollection<out T>(val unaffected: T, val exposed: T, val weathered: T, val oxidized: T) : AbstractCollection<T>() {
    operator fun get(state: WeatheringCopper.WeatherState): T = when (state) {
        WeatheringCopper.WeatherState.UNAFFECTED -> unaffected
        WeatheringCopper.WeatherState.EXPOSED -> exposed
        WeatheringCopper.WeatherState.WEATHERED -> weathered
        WeatheringCopper.WeatherState.OXIDIZED -> oxidized
    }

    fun asSequenceWithState(): Sequence<Pair<WeatheringCopper.WeatherState, T>> = WeatheringCopper.WeatherState.entries.asSequence().map { it to get(it) }

    fun asSequence(): Sequence<T> = WeatheringCopper.WeatherState.entries.asSequence().map(::get)

    override val size: Int = 4

    override fun isEmpty(): Boolean = false

    override fun iterator(): Iterator<T> = asSequence().iterator()
}
