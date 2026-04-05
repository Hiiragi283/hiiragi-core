package hiiragi283.core.api.block

import hiiragi283.core.api.data.lang.HTLangPatternProvider
import net.minecraft.util.StringRepresentable
import net.minecraft.world.level.block.WeatheringCopper
import net.minecraft.world.level.material.MapColor

/**
 * 酸化状態を管理するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.14.0
 */
enum class HTWeatheringLevel(private val prefix: String, provider: HTLangPatternProvider) :
    StringRepresentable,
    HTLangPatternProvider by provider {
    UNAFFECTED("", "%s", "%s"),
    EXPOSED("exposed_", "Exposed %s", "風化した%s"),
    WEATHERED("weathered_", "Weathered %s", "錆びた%s"),
    OXIDIZED("oxidized_", "Oxidized %s", "酸化した%s"),
    ;

    constructor(prefix: String, enPattern: String, jaPattern: String) : this(prefix, HTLangPatternProvider.create(enPattern, jaPattern))

    val state: WeatheringCopper.WeatherState get() = when (this) {
        UNAFFECTED -> WeatheringCopper.WeatherState.UNAFFECTED
        EXPOSED -> WeatheringCopper.WeatherState.EXPOSED
        WEATHERED -> WeatheringCopper.WeatherState.WEATHERED
        OXIDIZED -> WeatheringCopper.WeatherState.OXIDIZED
    }
    val mapColor: MapColor get() = when (this) {
        UNAFFECTED -> MapColor.COLOR_ORANGE
        EXPOSED -> MapColor.TERRACOTTA_LIGHT_GRAY
        WEATHERED -> MapColor.WARPED_STEM
        OXIDIZED -> MapColor.WARPED_NYLIUM
    }

    fun applyPrefix(path: String): String = "$prefix$path"

    override fun getSerializedName(): String = name.lowercase()
}
