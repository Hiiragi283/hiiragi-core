package hiiragi283.lib.registry

import it.unimi.dsi.fastutil.objects.Object2ObjectMap
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
data class HTCopperMap<out T>(val unaffected: T, val exposed: T, val weathered: T, val oxidized: T) : Map<WeatheringCopper.WeatherState, T> {
    override val size: Int get() = 4
    override val keys: Set<WeatheringCopper.WeatherState> get() = WeatheringCopper.WeatherState.entries.toSet()
    override val values: Collection<T> get() = listOf(unaffected, exposed, weathered, oxidized)
    override val entries: Set<Map.Entry<WeatheringCopper.WeatherState, T>>
        get() = keys.mapTo(mutableSetOf()) { key: WeatheringCopper.WeatherState -> Object2ObjectMap.entry(key, get(key)) }

    override fun isEmpty(): Boolean = false

    override fun containsKey(key: WeatheringCopper.WeatherState): Boolean = true

    override fun containsValue(value: @UnsafeVariance T): Boolean = value in values

    override fun get(key: WeatheringCopper.WeatherState): T = when (key) {
        WeatheringCopper.WeatherState.UNAFFECTED -> unaffected
        WeatheringCopper.WeatherState.EXPOSED -> exposed
        WeatheringCopper.WeatherState.WEATHERED -> weathered
        WeatheringCopper.WeatherState.OXIDIZED -> oxidized
    }
}
