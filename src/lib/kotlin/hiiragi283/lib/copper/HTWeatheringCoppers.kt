package hiiragi283.lib.copper

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.WeatheringCopperBlocks

/**
 * 酸化する銅系コンテンツとさび止めされた銅系コンテンツを束ねたクラスです。
 * @param WAXED さび止めされた銅系コンテンツ
 * @param WEATHERING 酸化する銅系コンテンツ
 * @param weathering さび止めされた銅系コンテンツの一覧
 * @param waxed 酸化する銅系コンテンツの一覧
 * @author Hiiragi Tsubas
 * @since 26.1.0
 */
@JvmRecord
data class HTWeatheringCoppers<out WAXED, out WEATHERING>(val weathering: HTCopperCollection<WEATHERING>, val waxed: HTCopperCollection<WAXED>) {
    constructor(
        unaffected: WEATHERING,
        exposed: WEATHERING,
        weathered: WEATHERING,
        oxidized: WEATHERING,
        waxedUnaffected: WAXED,
        waxedExposed: WAXED,
        waxedWeathered: WAXED,
        waxedOxidized: WAXED,
    ) : this(HTCopperCollection(unaffected, exposed, weathered, oxidized), HTCopperCollection(waxedUnaffected, waxedExposed, waxedWeathered, waxedOxidized))

    /**
     * 指定した[phase]から対応する銅系ブロックを取得します。
     */
    operator fun get(phase: HTCopperPhase): Pair<WEATHERING, WAXED> = weathering[phase] to waxed[phase]
}

inline fun <WAXED, WEATHERING> HTWeatheringCoppers(initWeathering: (HTCopperPhase) -> WEATHERING, initWaxed: (HTCopperPhase) -> WAXED): HTWeatheringCoppers<WAXED, WEATHERING> = HTWeatheringCoppers(HTCopperCollection(initWeathering), HTCopperCollection(initWaxed))

fun WeatheringCopperBlocks.convert(): HTWeatheringCoppers<Block, Block> = HTWeatheringCoppers(
    this.unaffected(),
    this.exposed(),
    this.weathered(),
    this.oxidized(),
    this.waxed(),
    this.waxedExposed(),
    this.waxedWeathered(),
    this.waxedOxidized(),
)
