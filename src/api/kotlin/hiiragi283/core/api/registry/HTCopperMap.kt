package hiiragi283.core.api.registry

import net.minecraft.world.level.block.WeatheringCopper

@JvmRecord
data class HTCopperMap<out T>(val unaffected: T, val exposed: T, val weathered: T, val oxidized: T) : Map<WeatheringCopper.WeatherState, T> {
    override val size: Int get() = 4
    override val keys: Set<WeatheringCopper.WeatherState> get() = WeatheringCopper.WeatherState.entries.toSet()
    override val values: Collection<T> get() = listOf(unaffected, exposed, weathered, oxidized)
    override val entries: Set<Map.Entry<WeatheringCopper.WeatherState, T>>
        get() = keys.mapTo(mutableSetOf()) { key: WeatheringCopper.WeatherState ->
            object : Map.Entry<WeatheringCopper.WeatherState, T> {
                override val key: WeatheringCopper.WeatherState = key
                override val value: T = get(key)
            }
        }

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
