package hiiragi283.lib.copper

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
    /**
     * 指定した[phase]から対応する銅系ブロックを取得します。
     */
    operator fun get(phase: HTCopperPhase): Pair<WEATHERING, WAXED> = weathering[phase] to waxed[phase]
}

inline fun <WAXED, WEATHERING> HTWeatheringCoppers(initWeathering: (HTCopperPhase) -> WEATHERING, initWaxed: (HTCopperPhase) -> WAXED): HTWeatheringCoppers<WAXED, WEATHERING> = HTWeatheringCoppers(HTCopperCollection(initWeathering), HTCopperCollection(initWaxed))
