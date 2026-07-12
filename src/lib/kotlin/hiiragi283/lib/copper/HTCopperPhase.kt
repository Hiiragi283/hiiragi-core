package hiiragi283.lib.copper

import hiiragi283.lib.data.lang.HTLangPatternProvider
import net.minecraft.util.StringRepresentable
import net.minecraft.world.level.block.WeatheringCopper
import net.minecraft.world.level.block.WeatheringCopperCollection

/**
 * 銅系コンテンツの酸化の進行度を表すクラスです。
 *
 * 参照 : [Minecraft - WeatheringCopper.WeatherState][WeatheringCopper.WeatherState]
 *
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
enum class HTCopperPhase(private val prefix: String, provider: HTLangPatternProvider) :
    StringRepresentable,
    HTLangPatternProvider by provider {
    UNAFFECTED("", HTLangPatternProvider.IDENTITY),
    EXPOSED("exposed_", "Exposed %s", "風化した%s"),
    WEATHERED("weathered_", "Weathered %s", "錆びた%s"),
    OXIDIZED("oxidized_", "Oxidized %s", "酸化した%s"),
    ;

    companion object {
        /**
         * @since 26.2.0
         */
        @JvmField
        val STATES: WeatheringCopperCollection.ByState<HTCopperPhase> = WeatheringCopperCollection.ByState(UNAFFECTED, EXPOSED, WEATHERED, OXIDIZED)

        /**
         * @since 26.2.0
         */
        @JvmField
        val COLLECTION: WeatheringCopperCollection<HTCopperPhase> = WeatheringCopperCollection(STATES, STATES)
    }

    constructor(prefix: String, enPattern: String, jaPattern: String) : this(prefix, HTLangPatternProvider.create(enPattern, jaPattern))

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
