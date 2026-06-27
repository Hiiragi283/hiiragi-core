package hiiragi283.lib.copper

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.WeatheringCopperBlocks

/**
 * 酸化する銅系コンテンツとさび止めされた銅系コンテンツを束ねたクラスです。
 * @param weathering さび止めされた銅系コンテンツの一覧
 * @param waxed 酸化する銅系コンテンツの一覧
 * @author Hiiragi Tsubas
 * @since 26.1.3
 */
@JvmRecord
data class HTWeatheringCoppers<out T>(val weathering: HTCopperCollection<T>, val waxed: HTCopperCollection<T>) {
    val allCoppers: List<T> get() = weathering + waxed

    /**
     * 指定した[phase]から対応する銅系ブロックを取得します。
     */
    operator fun get(phase: HTCopperPhase): Pair<T, T> = weathering[phase] to waxed[phase]

    inline fun <R> map(transform: (T) -> R): HTWeatheringCoppers<R> = HTWeatheringCoppers(weathering.map(transform), waxed.map(transform))

    inline fun <U, R> zip(other: HTWeatheringCoppers<U>, transform: (T, U) -> R): HTWeatheringCoppers<R> = HTWeatheringCoppers(weathering.zip(other.weathering, transform), waxed.zip(other.waxed, transform))
}

inline fun <T> HTWeatheringCoppers(initWeathering: (HTCopperPhase) -> T, initWaxed: (HTCopperPhase) -> T): HTWeatheringCoppers<T> = HTWeatheringCoppers(HTCopperCollection(initWeathering), HTCopperCollection(initWaxed))

fun WeatheringCopperBlocks.convert(): HTWeatheringCoppers<Block> = HTWeatheringCoppers(
    HTCopperCollection(
        this.unaffected(),
        this.exposed(),
        this.weathered(),
        this.oxidized(),
    ),
    HTCopperCollection(
        this.waxed(),
        this.waxedExposed(),
        this.waxedWeathered(),
        this.waxedOxidized(),
    ),
)
