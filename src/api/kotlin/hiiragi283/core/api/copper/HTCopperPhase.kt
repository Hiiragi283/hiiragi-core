package hiiragi283.core.api.copper

import hiiragi283.core.api.data.lang.HTLangPatternProvider
import net.minecraft.util.StringRepresentable
import net.minecraft.world.level.block.WeatheringCopper

/**
 * 銅系コンテンツの酸化の進行度を表すクラスです。
 *
 * 参照 : [Minecraft - WeatheringCopper.WeatherState][WeatheringCopper.WeatherState]
 *
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
enum class HTCopperPhase(private val prefix: String, provider: HTLangPatternProvider) :
    StringRepresentable,
    HTLangPatternProvider by provider {
    UNAFFECTED("", HTLangPatternProvider.IDENTITY),
    EXPOSED("exposed_", "Exposed %s", "風化した%s"),
    WEATHERED("weathered_", "Weathered %s", "錆びた%s"),
    OXIDIZED("oxidized_", "Oxidized %s", "酸化した%s"),
    ;

    constructor(prefix: String, enPattern: String, jaPattern: String) : this(prefix, HTLangPatternProvider(enPattern, jaPattern))

    fun createPath(name: String): String = "$prefix$name"

    fun createWaxedPath(name: String): String = "waxed_${createPath(name)}"

    fun toState(): WeatheringCopper.WeatherState = when (this) {
        UNAFFECTED -> WeatheringCopper.WeatherState.UNAFFECTED
        EXPOSED -> WeatheringCopper.WeatherState.EXPOSED
        WEATHERED -> WeatheringCopper.WeatherState.WEATHERED
        OXIDIZED -> WeatheringCopper.WeatherState.OXIDIZED
    }

    override fun getSerializedName(): String = name.lowercase()
}

fun WeatheringCopper.WeatherState.toPhase(): HTCopperPhase = when (this) {
    WeatheringCopper.WeatherState.UNAFFECTED -> HTCopperPhase.UNAFFECTED
    WeatheringCopper.WeatherState.EXPOSED -> HTCopperPhase.EXPOSED
    WeatheringCopper.WeatherState.WEATHERED -> HTCopperPhase.WEATHERED
    WeatheringCopper.WeatherState.OXIDIZED -> HTCopperPhase.OXIDIZED
}
