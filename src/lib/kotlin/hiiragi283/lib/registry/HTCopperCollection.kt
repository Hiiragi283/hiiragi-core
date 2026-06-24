package hiiragi283.lib.registry

import hiiragi283.lib.collection.mutableEnumMapOf
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
@JvmRecord
data class HTCopperCollection<out T>(val unaffected: T, val exposed: T, val weathered: T, val oxidized: T) : Collection<T> {
    operator fun get(state: WeatheringCopper.WeatherState): T = when (state) {
        WeatheringCopper.WeatherState.UNAFFECTED -> unaffected
        WeatheringCopper.WeatherState.EXPOSED -> exposed
        WeatheringCopper.WeatherState.WEATHERED -> weathered
        WeatheringCopper.WeatherState.OXIDIZED -> oxidized
    }

    //    Collection    //

    override val size: Int get() = 4
    override fun isEmpty(): Boolean = false

    override fun contains(element: @UnsafeVariance T): Boolean = any { it == element }

    override fun containsAll(elements: Collection<@UnsafeVariance T>): Boolean = elements.all { contains(it) }

    override fun iterator(): Iterator<T> = object : Iterator<T> {
        private var index: Int = 0

        override fun next(): T = WeatheringCopper.WeatherState.entries[index++].let(::get)

        override fun hasNext(): Boolean = index < WeatheringCopper.WeatherState.entries.size
    }

    fun asMap(): Map<WeatheringCopper.WeatherState, T> = WeatheringCopper.WeatherState.entries.associateWithTo(mutableEnumMapOf(), ::get)
}
